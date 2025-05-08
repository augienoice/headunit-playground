package com.example.headunitplayground.core.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class VinReceiver(private val onVinReceived: (String?) -> Unit) : BroadcastReceiver() {
    companion object {
        const val VIN_PACKAGE_NAME = "com.garmin.android.vin"
        const val VIN_SERVICE_NAME = "com.garmin.android.vin.VinService"
        const val EXTRA_VIN = "com.garmin.android.vin.extra.VIN"
        const val ACTION_GET_VIN = "com.garmin.android.vin.action.GET_VIN"
        const val ACTION_GET_VIN_RESULT = "com.garmin.android.vin.action.GET_VIN_RESULT"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "onReceive action: ${intent?.action}", Toast.LENGTH_SHORT).show()
        if (intent?.action?.equals(ACTION_GET_VIN_RESULT) == true) {
            val vin = intent.getStringExtra(EXTRA_VIN)
            Toast.makeText(context, "onReceive: $vin", Toast.LENGTH_SHORT).show()
            onVinReceived(vin)
        }
    }
}