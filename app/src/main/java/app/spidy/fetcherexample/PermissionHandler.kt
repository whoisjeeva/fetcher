package app.spidy.fetcherexample

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog

object PermissionHandler {

    const val STORAGE_PERMISSION_CODE = 1
    const val LOCATION_PERMISSION_CODE = 2
    private lateinit var m: () -> Unit


    fun requestStorage(context: Context, reason: String, callback: () -> Unit) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            callback()
            return
        }

        m = callback


        if (ActivityCompat.shouldShowRequestPermissionRationale((context as Activity), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(reason)
            builder.setPositiveButton("Ok") { dialog, index ->
                dialog.cancel()
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
            }
            builder.setNegativeButton("Cancel") { dialog, index ->
                dialog.cancel()
            }
            builder.create().show()
        } else {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
        }
    }


    fun requestLocation(context: Context, reason: String, callback: () -> Unit) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            callback()
            return
        }

        m = callback


        if (ActivityCompat.shouldShowRequestPermissionRationale((context as Activity), Manifest.permission.ACCESS_FINE_LOCATION)) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(reason)
            builder.setPositiveButton("OK") { dialog, index ->
                dialog.cancel()
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
            }
            builder.setNegativeButton("Cancel") { dialog, index ->
                dialog.cancel()
            }
            builder.create().show()
        } else {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
        }
    }

    fun execute() {
        m()
    }
}