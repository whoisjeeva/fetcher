package app.spidy.fetcherexample

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import app.spidy.fetcher.Fetcher
import app.spidy.universal.debug
import java.io.File
import java.io.RandomAccessFile

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    val fetcher = Fetcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionHandler.requestStorage(this, "need storage permission") {
            download()
        }
    }

    private fun download() {
        val downloadLocation = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = RandomAccessFile("$downloadLocation${File.separator}test.mov", "rw")
        file.seek(0)
        val url = "https://sohowww.nascom.nasa.gov/gallery/Movies/animation/Solarwind_snd.mov"
        fetcher.get(url, isStream = true)
                .ifFailedOrException {
                    debug("hello", "FAILED.")
                }
                .ifStream { buffer, byteSize ->
                    if (byteSize == -1) {
                        file.close()
                        debug(TAG, "DONE.")
                    } else {
                        file.write(buffer, 0, byteSize)
                        debug(TAG, "DOWNLOADING... ($byteSize)")
                    }
                }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PermissionHandler.STORAGE_PERMISSION_CODE ||
                requestCode == PermissionHandler.LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PermissionHandler.execute()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
