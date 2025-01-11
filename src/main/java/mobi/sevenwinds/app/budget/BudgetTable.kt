package mobi.sevenwinds.app.budget

import mobi.sevenwinds.app.author.Author
import mobi.sevenwinds.app.author.AuthorEntity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object BudgetTable : IntIdTable("budget") {
    val year = integer("year")
    val month = integer("month")
    val amount = integer("amount")
    val type = enumerationByName("type", 100, BudgetType::class)
    val author = reference("author_id", Author).nullable()
}

class BudgetEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BudgetEntity>(BudgetTable)

    var year by BudgetTable.year
    var month by BudgetTable.month
    var amount by BudgetTable.amount
    var type by BudgetTable.type
    var author by AuthorEntity.optionalReferencedOn(BudgetTable.author)

    fun toResponse(): BudgetResponse {
        return BudgetResponse(year, month, amount, type,  AuthorResponse(author?.id?.value, author?.fullName, author?.createdAt.toString()))
    }

}
