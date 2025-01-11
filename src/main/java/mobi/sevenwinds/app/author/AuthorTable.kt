package mobi.sevenwinds.app.author

import org.jetbrains.exposed.dao.*

object Author : IntIdTable() {
    val fullName = varchar("full_name", 255)
    val createdAt = datetime("created_at")
}

class AuthorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthorEntity>(Author)

    var fullName by Author.fullName
    var createdAt by Author.createdAt
}

