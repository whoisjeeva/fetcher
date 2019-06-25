package app.spidy.fetcherexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.spidy.fetcher.Fetcher

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fetcher = Fetcher()
        val headers = hashMapOf<String, Any?>("User-Agent" to "hello")
        fetcher.get("http://httpbin.org/get", headers = headers)
            .ifSucceed {
                Log.d("hello", it.text.toString())
            }
    }
}
