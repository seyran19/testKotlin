package mobi.sevenwinds.app.author


import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object AuthorService {
    fun createAuthor(request: AuthorCreateRequest): AuthorResponse {
        return transaction {
            val author = AuthorEntity.new {
                fullName = request.fullName
                createdAt = DateTime()
            }
            AuthorResponse(author.id.value, author.fullName, author.createdAt.toString())
        }
    }
}

