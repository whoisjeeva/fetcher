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
        fetcher.put("http://httpbin.org/put", argument)
            .ifFailedOrException {
                Log.d("test", "FAILED")
            }
            .ifSucceed {
                Log.d("test", "SUCCEED: ${it.text}")
            }
    }
}
