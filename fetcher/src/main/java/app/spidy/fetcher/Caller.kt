package app.spidy.fetcher

import okhttp3.Call

class Caller(private val call: Call) {
    val isCanceled = call.isCanceled
    val isExecuted = call.isExecuted

    fun cancel() {
        call.cancel()
    }
}