package app.spidy.fetcher

import app.spidy.fetcher.models.Listener
import app.spidy.fetcher.models.Response

class ResponsePool {
    val listener = Listener()
    var caller: Caller? = null

    fun ifFailed(callback: (Response) -> Unit): ResponsePool {
        listener.ifFailed = callback
        return this
    }
    fun ifSucceed(callback: (Response) -> Unit): Caller {
        listener.ifSucceed = callback
        return caller!!
    }
    fun ifException(callback: (String?) -> Unit): ResponsePool {
        listener.ifException = callback
        return this
    }

    fun ifStream(callback: (buffer: ByteArray?, byteSize: Int) -> Unit): ResponsePool {
        listener.ifStream = callback
        return this
    }

    fun ifFailedOrException(callback: () -> Unit): ResponsePool {
        listener.ifFailedOrException = callback
        return this
    }
}