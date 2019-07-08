package app.spidy.fetcher.models

data class Listener(
    var ifFailed: ((Response) -> Unit)? = null,
    var ifSucceed: ((Response) -> Unit)? = null,
    var ifException: ((String?) -> Unit)? = null,
    var ifStream: ((buffer: ByteArray?, bytes: Int?) -> Unit)? = null,
    var ifFailedOrException: (() -> Unit)? = null
)