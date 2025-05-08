package com.example.headunitplayground.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.headunitplayground.R
import com.example.headunitplayground.core.broadcast.VinReceiver
import com.example.headunitplayground.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AppListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startVinService()
        registerVinReceiver()

        checkAllInstalledAppPackageName()
    }

    private fun startVinService() {
        val vinIntent = Intent().apply {
            component = ComponentName(VinReceiver.VIN_PACKAGE_NAME, VinReceiver.VIN_SERVICE_NAME)
            action = VinReceiver.ACTION_GET_VIN
        }

        applicationContext.startService(vinIntent)
    }

    private fun registerVinReceiver() {
        val vinReceiver = VinReceiver {
            binding.tvVin.text = "My VIN: $it"
        }
        val vinIntentFilter = IntentFilter().apply {
            addAction(VinReceiver.ACTION_GET_VIN_RESULT)
        }

        ContextCompat.registerReceiver(
            applicationContext,
            vinReceiver,
            vinIntentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    private fun checkVinServiceExist() {
        val pm = packageManager
        val services = pm.getPackageInfo(VinReceiver.VIN_PACKAGE_NAME, PackageManager.GET_SERVICES).services
        services?.forEach { serviceInfo ->
            if (serviceInfo.exported) {
                Toast.makeText(this, "Exported service: ${serviceInfo.name}", Toast.LENGTH_SHORT).show()
                Log.d("ServiceInfo", "Exported service: ${serviceInfo.name}")
            }
        }
    }

    private fun checkAllInstalledAppPackageName() {
        val pm = packageManager
        val apps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0L))
        } else {
            pm.getInstalledApplications(0)
        }

        // Filter the list of applications to include only those with "garmin" in the package name
        val garminApps = apps.filter { app ->
            app.packageName.contains("garmin", ignoreCase = true)
        }.map { Pair(it.loadLabel(pm).toString(), it.packageName) }.toMutableList()

        adapter = AppListAdapter(garminApps)
        binding.rvApp.layoutManager = LinearLayoutManager(this)
        binding.rvApp.adapter = adapter
    }

    fun bindService() {
        val intent = Intent().apply {
            component = ComponentName(VinReceiver.VIN_PACKAGE_NAME, VinReceiver.VIN_SERVICE_NAME)
        }
        val success = bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Log.d("VinServiceBinder", "bindService connected")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d("VinServiceBinder", "bindService disconnected")
            }
        }, Context.BIND_AUTO_CREATE)
        if (success) {
            Log.d("VinServiceBinder", "bindService success")
        } else {
            Log.d("VinServiceBinder", "bindService failed")
        }
    }

    fun startForegroundService() {
        val vinIntent = Intent().apply {
            component = ComponentName(VinReceiver.VIN_PACKAGE_NAME, VinReceiver.VIN_SERVICE_NAME)
            action = VinReceiver.ACTION_GET_VIN
        }

        applicationContext.startForegroundService(vinIntent)
    }
}