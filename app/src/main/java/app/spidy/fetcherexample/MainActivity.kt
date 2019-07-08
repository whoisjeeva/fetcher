package app.spidy.fetcherexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.spidy.fetcher.Fetcher
import app.spidy.universal.debug

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fetcher = Fetcher()

        val url = "https://sohowww.nascom.nasa.gov/gallery/Movies/animation/Solarwind_snd.mov"
        fetcher.get(url, isStream = true, byteSize = 1024)
                .ifFailedOrException {
                    debug("hello", "FAILED.")
                }
                .ifStream { buffer, byteSize ->
                    debug("hello", byteSize)
                }
    }
}
