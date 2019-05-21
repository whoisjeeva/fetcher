package app.spidy.fetcher.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

fun Context.toast(s: Any?, isShort: Boolean = true) {
    val duration = if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
    Toast.makeText(this, s.toString(), duration).show()
}

fun Context.onUiThread(callback: (context: Context) -> Unit) {
    Handler(this.mainLooper).post {
        callback(this)
    }
}

fun onUiThread(callback: () -> Unit) {
    Handler(Looper.getMainLooper()).post {
        callback()
    }
}