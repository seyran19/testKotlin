package mobi.sevenwinds.app.author

import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route


fun NormalOpenAPIRoute.author() {
    route("/authors") {
        route("/add").post<Unit, AuthorResponse, AuthorCreateRequest>(info("Добавить автора")) { _, body ->
            val response = AuthorService.createAuthor(body)
            respond(response)
        }
    }
}
data class AuthorCreateRequest(
    val fullName: String)

data class AuthorResponse(val id: Int, val fullName: String, val createdAt: String)