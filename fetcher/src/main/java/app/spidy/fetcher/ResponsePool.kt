package app.spidy.fetcher

import app.spidy.fetcher.models.Listener
import app.spidy.fetcher.models.Response

class ResponsePool {
    val listener = Listener()

    fun ifFailed(callback: (Response) -> Unit): ResponsePool {
        listener.ifFailed = callback
        return this
    }
    fun ifSucceed(callback: (Response) -> Unit) {
        listener.ifSucceed = callback
    }
    fun ifException(callback: (String?) -> Unit): ResponsePool {
        listener.ifException = callback
        return this
    }

    fun ifStream(callback: (ByteArray?) -> Unit): ResponsePool {
        listener.ifStream = callback
        return this
    }

    fun ifFailedOrException(callback: () -> Unit): ResponsePool {
        listener.ifFailedOrException = callback
        return this
    }
}