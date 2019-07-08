package app.spidy.fetcher

import app.spidy.fetcher.models.Response
import app.spidy.fetcher.utils.onUiThread
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.HttpUrl
import okhttp3.MultipartBody
import java.io.InputStream
import java.lang.Exception
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.concurrent.thread

class Fetcher {
    /**
     * TODO: create a separate method for file upload
     */

    private val httpClient = OkHttpClient()

    fun urlDecode(url: String): String {
        return URLDecoder.decode(url, "UTF-8")
    }

    fun urlEncode(url: String): String {
        return URLEncoder.encode(url, "UTF-8")
    }

    fun get(url: String,
            params: HashMap<String, Any?> = hashMapOf(),
            headers: HashMap<String, Any?> = hashMapOf("User-Agent" to "Fetcher/" + BuildConfig.VERSION_NAME),
            isStream: Boolean = false): ResponsePool {
        val urlBuilder = HttpUrl.parse(url)!!.newBuilder()
        val request = __get_request(urlBuilder, params, headers)
        val responsePool = ResponsePool()
        responsePool.caller = Caller(execute(request, responsePool, isStream))

        return responsePool
    }

    private fun __get_request(urlBuilder: HttpUrl.Builder, params: HashMap<String, Any?>, headers: HashMap<String, Any?>): Request {
        val request = Request.Builder()

        for ((key, value) in headers) {
            request.addHeader(key, value.toString())
        }
        for ((key, value) in params) {
            urlBuilder.addQueryParameter(key, value.toString())
        }
        return request.url(urlBuilder.build().toString()).get().build()
    }


    fun post(url: String,
             params: HashMap<String, Any?> = hashMapOf(),
             headers: HashMap<String, Any?> = hashMapOf("User-Agent" to "Fetcher/" + BuildConfig.VERSION_NAME),
             isStream: Boolean = false): ResponsePool {
        val request = __post_request(url, params, headers)
        val responsePool = ResponsePool()
        responsePool.caller = Caller(execute(request, responsePool, isStream))

        return responsePool
    }

    private fun __post_request(url: String, params: HashMap<String, Any?>, headers: HashMap<String, Any?>): Request {
        if (params.isEmpty()) {
            params["__fetcher"] = BuildConfig.VERSION_NAME
        }

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
        val request = Request.Builder()

        for ((key, value) in params) {
            requestBody.addFormDataPart(key, value.toString())
        }
        for ((key, value) in headers) {
            request.addHeader(key, value.toString())
        }

        return request.url(url).post(requestBody.build()).build()
    }


    fun head(url: String,
             params: HashMap<String, Any?> = hashMapOf(),
             headers: HashMap<String, Any?> = hashMapOf("User-Agent" to "Fetcher/" + BuildConfig.VERSION_NAME),
             isStream: Boolean = false): ResponsePool {
        val urlBuilder = HttpUrl.parse(url)!!.newBuilder()
        val request = __head_request(urlBuilder, params, headers)
        val responsePool = ResponsePool()

        responsePool.caller = Caller(execute(request, responsePool, isStream))

        return responsePool
    }

    private fun __head_request(urlBuilder: HttpUrl.Builder, params: HashMap<String, Any?>, headers: HashMap<String, Any?>): Request {
        val request = Request.Builder()

        for ((key, value) in headers) {
            request.addHeader(key, value.toString())
        }
        for ((key, value) in params) {
            urlBuilder.addQueryParameter(key, value.toString())
        }
        return request.url(urlBuilder.build().toString()).head().build()
    }


    fun put(url: String,
            params: HashMap<String, Any?> = hashMapOf(),
            headers: HashMap<String, Any?> = hashMapOf("User-Agent" to "Fetcher/" + BuildConfig.VERSION_NAME),
            isStream: Boolean = false): ResponsePool {
        val request = __put_request(url, params, headers)
        val responsePool = ResponsePool()

        responsePool.caller = Caller(execute(request, responsePool, isStream))

        return responsePool
    }

    private fun __put_request(url: String, params: HashMap<String, Any?>, headers: HashMap<String, Any?>): Request {
        if (params.isEmpty()) {
            params["__fetcher"] = BuildConfig.VERSION_NAME
        }

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
        val request = Request.Builder()

        for ((key, value) in params) {
            requestBody.addFormDataPart(key, value.toString())
        }
        for ((key, value) in headers) {
            request.addHeader(key, value.toString())
        }

        return request.url(url).put(requestBody.build()).build()
    }

    private fun execute(
        request: Request,
        responsePool: ResponsePool,
        isStream: Boolean): Call {
        val call = httpClient.newCall(request)
        thread {
            var waitingTime = 0
            while(responsePool.listener.ifSucceed == null) {
                Thread.sleep(100)
                waitingTime += 100
                if (waitingTime > 2000) break
            }

            try {
                val serverResponse = call.execute()
                val inputStream: InputStream? = if (isStream) serverResponse.body()?.byteStream() else null
                val response = Response(
                    serverResponse.isRedirect,
                    serverResponse.code(),
                    serverResponse.message()
                )
                val serverHeaders = serverResponse.headers()
                serverHeaders.names().forEach {
                    response.headers[it.toLowerCase()] = serverHeaders[it]
                }

                response.isSuccessful = serverResponse.isSuccessful
                if (serverResponse.isSuccessful) {
                    val buffer = ByteArray(8192)
                    if (isStream) {
                        while (true) {
                            val bytes = inputStream?.read(buffer)
                            if (bytes == -1 || bytes == null) {
                                onUiThread { responsePool.listener.ifStream?.invoke(null) }
                                break
                            }
                            onUiThread { responsePool.listener.ifStream?.invoke(buffer) }
                        }
                    } else {
                        response.content = serverResponse.body()?.bytes()
                        response.content?.also {
                            response.text = String(it)
                        }
                    }
                    onUiThread { responsePool.listener.ifSucceed?.invoke(response) }
                } else {
                    onUiThread {
                        responsePool.listener.ifFailed?.invoke(response)
                        responsePool.listener.ifFailedOrException?.invoke()
                    }
                }
            } catch (e: Exception) {
                onUiThread {
                    responsePool.listener.ifException?.invoke(e.message)
                    responsePool.listener.ifFailedOrException?.invoke()
                }
            }
        }

        return call
    }


    inner class Classic {
        fun get(url: String,
                params: HashMap<String, Any?> = hashMapOf(),
                headers: HashMap<String, Any?> = hashMapOf("User-Agent" to "Fetcher/" + BuildConfig.VERSION_NAME)): Response {
            val urlBuilder = HttpUrl.parse(url)!!.newBuilder()
            val request = __get_request(urlBuilder, params, headers)
            return execute(request)
        }


        fun post(url: String, params: HashMap<String, Any?> = hashMapOf(),
                 headers: HashMap<String, Any?> = hashMapOf("User-Agent" to "Fetcher/" + BuildConfig.VERSION_NAME)): Response {
            val request = __post_request(url, params, headers)
            return execute(request)
        }

        fun head(url: String, params: HashMap<String, Any?> = hashMapOf(),
                 headers: HashMap<String, Any?> = hashMapOf("User-Agent" to "Fetcher/" + BuildConfig.VERSION_NAME)): Response {
            val urlBuilder = HttpUrl.parse(url)!!.newBuilder()
            val request = __head_request(urlBuilder, params, headers)

            return execute(request)
        }


        fun put(url: String, params: HashMap<String, Any?> = hashMapOf(),
                headers: HashMap<String, Any?> = hashMapOf("User-Agent" to "Fetcher/" + BuildConfig.VERSION_NAME)): Response {
            val request = __put_request(url, params, headers)
            return execute(request)
        }


        private fun execute(request: Request): Response {
            val serverResponse = httpClient.newCall(request).execute()
            val response = Response(
                serverResponse.isRedirect,
                serverResponse.code(),
                serverResponse.message()
            )
            val serverHeaders = serverResponse.headers()
            serverHeaders.names().forEach {
                response.headers[it.toLowerCase()] = serverHeaders[it]
            }

            response.isSuccessful = serverResponse.isSuccessful
            if (response.isSuccessful) {
                response.content = serverResponse.body()?.bytes()
                response.content?.also {
                    response.text = String(it)
                }
            }
            return response
        }
    }
}