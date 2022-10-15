package com.example.myapplication.app_usage_history

import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.os.UserManager
import android.provider.Settings
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityAppUsageHistoryBinding
import com.example.myapplication.utils.FileUtils.TAG
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


class AppUsageHistoryActivity : AppCompatActivity() {

    private var youtubeApp: String? = null
    private lateinit var binding: ActivityAppUsageHistoryBinding
    var foregroundAppPackageName: String? = null
    val currentTime = System.currentTimeMillis()

    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a")
    val dateString = simpleDateFormat.format(9897546853323L)


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAppUsageHistoryBinding.inflate(layoutInflater)


        setContentView(binding.root)


        if (checkUsageStatsPermission().not()) {
            Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                startActivity(this)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val userManager = getSystemService(USER_SERVICE) as UserManager
                if (userManager.isUserUnlocked) {
                    checkAppUsage()
                }
            }
        }

        binding.btnKill.setOnClickListener {
            try {
                val data: ByteArray = Base64.decode("Y29tLmlhcnQuY2hyb21lY2FzdGFwcHM7TkVXU19BTkRfTUFHQVpJTkVT", Base64.DEFAULT)
                val str = String(data, StandardCharsets.UTF_8)
                Log.d(TAG, "onCreate: $str")
            } catch (e: java.lang.Exception) {
                "base64"
            }
            youtubeApp?.let {
                val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
                activityManager.killBackgroundProcesses(it)


            }
        }
    }

    private fun checkAppUsage() {

        /*val queryEvents = usageStatsManager.queryEvents(System.currentTimeMillis() - 5000, System.currentTimeMillis())

        val eventsList = mutableListOf<UsageEvents.Event>()
        while (queryEvents.hasNextEvent()) {
            val usageEvent = UsageEvents.Event()
            queryEvents.getNextEvent(usageEvent)
            eventsList.add(usageEvent)
        }

        val sortedEvents = eventsList.sortedBy { it.timeStamp }*/

        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val queryUsageStats =
            usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, 0, currentTime)
                .sortedBy { it.totalTimeInForeground }

        queryUsageStats.forEach { usageStats: UsageStats? ->
            Log.d(
                "AppUsageHistory",
                "checkAppUsage: ${usageStats?.packageName} -> Seconds: ${
                    usageStats?.totalTimeInForeground?.let {
                        TimeUnit.MILLISECONDS.toSeconds(
                            it
                        )
                    }
                }"
            )
            usageStats?.packageName?.let {
                if (it.contains("youtube", true)) {
                    youtubeApp = it
                }
            }

        }

        /*  val usageEvent = UsageEvents.Event()
          while (usageEvents.hasNextEvent()) {
              usageEvents.getNextEvent(usageEvent)
              if (usageEvent.packageName.contentEquals("youtube", true)) {
                  youtubeApp = usageEvent.packageName
              }

          }*/
    }


    private fun getNonSystemAppsList(): Map<String, String> {
        val appInfos = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        val appInfoMap = HashMap<String, String>()
        for (appInfo in appInfos) {
            if (appInfo.flags != ApplicationInfo.FLAG_SYSTEM) {
                appInfoMap[appInfo.packageName] =
                    packageManager.getApplicationLabel(appInfo).toString()
            }
        }
        return appInfoMap
    }

    @Suppress("DEPRECATION")
    private fun checkUsageStatsPermission(): Boolean {
        val appOpsManager = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpsManager.unsafeCheckOpNoThrow(
                "android:get_usage_stats",
                android.os.Process.myUid(),
                packageName
            )
        } else {
            appOpsManager.checkOpNoThrow(
                "android:get_usage_stats",
                android.os.Process.myUid(),
                packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }


    fun amKillProcess(process: String) {
        val am = this.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = am.runningAppProcesses
        for (runningProcess in runningProcesses) {
            if (runningProcess.processName.contains("youtube", ignoreCase = true)) {
                Process.sendSignal(runningProcess.pid, Process.SIGNAL_KILL)
            }
            /*if (runningProcess.processName == process) {
                Process.sendSignal(runningProcess.pid, Process.SIGNAL_KILL)
            }*/
        }
    }


    fun killBackgroundProcesses(processName: String) {
        // mIsScanning = true;
        val am = this.getSystemService(ACTIVITY_SERVICE) as ActivityManager

        var packageName: String? = null
        try {
            packageName = if (processName.indexOf(":") == -1) {
                processName
            } else {
                processName.split(":").toTypedArray()[0]
            }
            am.killBackgroundProcesses(packageName)
            //
            /*val forceStopPackage: Method = am.javaClass
                .getDeclaredMethod("forceStopPackage", String::class.java)
            forceStopPackage.setAccessible(true)
            forceStopPackage.invoke(am, packageName)*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}