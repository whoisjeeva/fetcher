package app.spidy.fetcher

import app.spidy.fetcher.models.Response

class ResponsePool {
    var __ifFailed: ((Response) -> Unit)? = null
    var __ifSucceed: ((Response) -> Unit)? = null
    var __ifException: ((String?) -> Unit)? = null
    var __ifStream: ((ByteArray?) -> Unit)? = null
    var __ifFailedOrException: (() -> Unit)? = null

    fun ifFailed(callback: (Response) -> Unit): ResponsePool {
        __ifFailed = callback
        return this
    }
    fun ifSucceed(callback: (Response) -> Unit): ResponsePool {
        __ifSucceed = callback
        return this
    }
    fun ifException(callback: (String?) -> Unit): ResponsePool {
        __ifException = callback
        return this
    }

    fun ifStream(callback: (ByteArray?) -> Unit): ResponsePool {
        __ifStream = callback
        return this
    }

    fun ifFailedOrException(callback: () -> Unit): ResponsePool {
        __ifFailedOrException = callback
        return this
    }
}