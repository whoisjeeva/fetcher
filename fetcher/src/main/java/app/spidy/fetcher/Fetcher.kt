package app.spidy.fetcher

import app.spidy.fetcher.models.Argument
import app.spidy.fetcher.models.Response
import app.spidy.fetcher.utils.onUiThread
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.HttpUrl
import okhttp3.MultipartBody
import java.io.InputStream
import java.lang.Exception
import kotlin.concurrent.thread

class Fetcher {

    private val httpClient = OkHttpClient()

    fun get(url: String, argument: Argument = Argument()): ResponsePool {
        val urlBuilder = HttpUrl.parse(url)!!.newBuilder()
        val request = __get_request(urlBuilder, argument)
        val responsePool = ResponsePool()

        thread {
            while(responsePool.__ifFailed == null || responsePool.__ifSucceed == null) {
                Thread.sleep(100)
            }

            try {
                val serverResponse = httpClient.newCall(request).execute()
                val inputStream: InputStream? = if (argument.isStream) serverResponse.body()?.byteStream() else null
                val response = Response(
                    serverResponse.isRedirect,
                    serverResponse.code(),
                    serverResponse.message()
                )
                val serverHeaders = serverResponse.headers()
                serverHeaders.names().forEach {
                    response.headers[it.toLowerCase()] = serverHeaders[it]
                }

                if (serverResponse.isSuccessful) {
                    val buffer = ByteArray(8192)
                    if (argument.isStream) {
                        while (true) {
                            val bytes = inputStream?.read(buffer)
                            if (bytes == -1 || bytes == null) {
                                responsePool.__ifStream?.invoke(null)
                                break
                            }
                            responsePool.__ifStream?.invoke(buffer)
                        }
                    } else {
                        response.text = serverResponse.body()?.string()
                    }
                    onUiThread { responsePool.__ifSucceed?.invoke(response) }
                } else {
                    onUiThread { responsePool.__ifFailed?.invoke(response) }
                }
            } catch (e: Exception) {
                onUiThread { responsePool.__ifException?.invoke(e.message) }
            }
        }

        return responsePool
    }

    private fun __get_request(urlBuilder: HttpUrl.Builder, argument: Argument): Request {
        val request = Request.Builder()

        for ((key, value) in argument.headers) {
            request.addHeader(key, value.toString())
        }
        for ((key, value) in argument.params) {
            urlBuilder.addQueryParameter(key, value.toString())
        }
        return request.url(urlBuilder.build().toString()).get().build()
    }


    fun post(url: String, argument: Argument = Argument()): ResponsePool {
        val request = __post_request(url, argument)
        val responsePool = ResponsePool()

        thread {
            while(responsePool.__ifFailed == null || responsePool.__ifSucceed == null) {
                Thread.sleep(100)
            }

            try {
                val serverResponse = httpClient.newCall(request).execute()
                val inputStream: InputStream? = if (argument.isStream) serverResponse.body()?.byteStream() else null
                val response = Response(
                    serverResponse.isRedirect,
                    serverResponse.code(),
                    serverResponse.message()
                )
                val serverHeaders = serverResponse.headers()
                serverHeaders.names().forEach {
                    response.headers[it.toLowerCase()] = serverHeaders[it]
                }

                if (serverResponse.isSuccessful) {
                    val buffer = ByteArray(8192)
                    if (argument.isStream) {
                        while (true) {
                            val bytes = inputStream?.read(buffer)
                            if (bytes == -1 || bytes == null) {
                                responsePool.__ifStream?.invoke(null)
                                break
                            }
                            responsePool.__ifStream?.invoke(buffer)
                        }
                    } else {
                        response.text = serverResponse.body()?.string()
                    }
                    onUiThread { responsePool.__ifSucceed?.invoke(response) }
                } else {
                    onUiThread { responsePool.__ifFailed?.invoke(response) }
                }
            } catch (e: Exception) {
                onUiThread { responsePool.__ifException?.invoke(e.message) }
            }
        }

        return responsePool
    }

    private fun __post_request(url: String, argument: Argument): Request {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
        val request = Request.Builder()

        for ((key, value) in argument.params) {
            requestBody.addFormDataPart(key, value.toString())
        }
        for ((key, value) in argument.headers) {
            request.addHeader(key, value.toString())
        }

        return request.url(url).post(requestBody.build()).build()
    }
}