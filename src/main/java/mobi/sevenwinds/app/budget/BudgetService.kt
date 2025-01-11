package mobi.sevenwinds.app.budget

import javassist.NotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobi.sevenwinds.app.author.Author
import mobi.sevenwinds.app.author.AuthorEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


object BudgetService {
    suspend fun addRecord(body: BudgetRecord): BudgetResponse = withContext(Dispatchers.IO) {
        transaction {

            val authorEntity = body.authorId?.let { authorId ->
                AuthorEntity.findById(authorId) ?: throw NotFoundException("Author with id $authorId not found")
            }

            val entity = BudgetEntity.new {
                this.year = body.year
                this.month = body.month
                this.amount = body.amount
                this.type = body.type
                this.author = authorEntity
            }

            return@transaction entity.toResponse()
        }
    }

    suspend fun getYearStats(param: BudgetYearParam): BudgetYearStatsResponse = withContext(Dispatchers.IO) {
        transaction {
            var baseQuery = BudgetTable
                .join(Author, JoinType.LEFT, BudgetTable.author, Author.id)
                .select { BudgetTable.year eq param.year }

            param.authorName?.let { nameFilter ->
                baseQuery = baseQuery.andWhere {
                    Author.fullName.lowerCase() like "%${nameFilter.toLowerCase()}%"
                }
            }


            val total = baseQuery.count()

            val paginatedQuery = baseQuery
                .orderBy(BudgetTable.month to SortOrder.ASC, BudgetTable.amount to SortOrder.DESC)
                .limit(param.limit.coerceAtLeast(0), param.offset.coerceAtLeast(0))

            val data = BudgetEntity.wrapRows(paginatedQuery).map { it.toResponse() }

            val sumByType = BudgetTable
                .slice(BudgetTable.type, BudgetTable.amount.sum())
                .select { BudgetTable.year eq param.year }
                .groupBy(BudgetTable.type)
                .associate { it[BudgetTable.type].name to (it[BudgetTable.amount.sum()] ?: 0) }

            return@transaction BudgetYearStatsResponse(
                total = total,
                totalByType = sumByType,
                items = data
            )
        }
    }
}
