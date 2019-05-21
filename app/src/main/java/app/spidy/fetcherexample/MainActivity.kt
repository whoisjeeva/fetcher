package app.spidy.fetcherexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.spidy.fetcher.Fetcher
import app.spidy.fetcher.models.Argument

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fetcher = Fetcher()

        val argument = Argument()
        argument.params["name"] = "Jeeva"
        argument.isStream = true
        fetcher.post("http://httpbin.org/post", argument)
            .ifFailed {
                Log.d("test", "FAILED: $it")
            }
            .ifStream {
                Log.d("test", it.toString())
            }
            .ifSucceed {
                Log.d("test", "SUCCEED: $it")
            }
            .ifException {
                Log.d("test", "EXCEPTION: $it")
            }
    }
}
