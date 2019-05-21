package app.spidy.fetcher.models

data class Response(
    val isRedirect: Boolean,
    val statusCode: Int,
    val message: String,
    var text: String? = null,
    val headers: HashMap<String, String?> = hashMapOf()
)