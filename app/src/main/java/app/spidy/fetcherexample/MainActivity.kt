package app.spidy.fetcherexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.spidy.fetcher.Fetcher
import app.spidy.fetcher.models.Argument
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fetcher = Fetcher().Classic()

        thread {
            val response = fetcher.get("http://httpbin.org/get", headers = hashMapOf(
                "User-Agent" to "hello"
            ))
            Log.d("hello", response.text.toString())
        }
    }
}
