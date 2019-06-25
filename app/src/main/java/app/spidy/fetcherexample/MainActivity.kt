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


        fetcher.get("https://camo.githubusercontent.com/c1d74ecd1ddf3252ecfc5893ffaaef67ddde373d/68747470733a2f2f692e6962622e636f2f747042374471502f626c61636b2d6765726d616e2d73686570686572642d642d342e6a7067")
            .ifSucceed {
                Log.d("hello", it.toString())
            }
    }
}
