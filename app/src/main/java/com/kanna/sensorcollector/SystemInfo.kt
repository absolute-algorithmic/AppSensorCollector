package com.kanna.sensorcollector

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.provider.Settings
import android.os.SystemClock
import java.security.MessageDigest
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.sqrt

class SystemInfo(application: Application) {
    val id: String = sha256Hash(Settings.Secure.ANDROID_ID + Build.FINGERPRINT)
    val messageType: String = "systemInfo"
    val build: BuildInfo = BuildInfo()
    val perfBench: List<String> = PerfBench().benchmarkMultiple(10)
    val screen: ScreenInfo = ScreenInfo(application)
    val sensorList: List<Int> = getAvailableSensors(application)

    private fun getAvailableSensors(application: Application): List<Int> {
        val sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return sensorManager.getSensorList(Sensor.TYPE_ALL).map { sensor -> sensor.type }
    }

    private fun sha256Hash(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray())

        val hexString = StringBuilder()
        for (byte in hashBytes) {
            val hex = String.format("%02x", byte)
            hexString.append(hex)
        }

        return hexString.toString()
    }
}

class BuildInfo {
    val manufacturer: String = Build.MANUFACTURER
    val hardware: String = Build.HARDWARE
    val model: String = Build.MODEL
    val bootloader: String = Build.BOOTLOADER
    val version: VersionInfo = VersionInfo()
    val product: String = Build.PRODUCT
    val tags: String = Build.TAGS
    val type: String = Build.TYPE
    val user: String = Build.USER
    val display: String = Build.DISPLAY
    val board: String = Build.BOARD
    val brand: String = Build.BRAND
    val device: String = Build.DEVICE
    val fingerprint: String = Build.FINGERPRINT
    val host: String = Build.HOST
    val id: String = Build.ID
}

class VersionInfo {
    val release: String = Build.VERSION.RELEASE
    val codename: String = Build.VERSION.CODENAME
    val incremental: String = Build.VERSION.INCREMENTAL
    val sdkInt: Int = Build.VERSION.SDK_INT
}

class ScreenInfo(application: Application) {
    val heightPixels: Int = application.resources.displayMetrics.heightPixels
    val widthPixels: Int = application.resources.displayMetrics.widthPixels
    val density: Float = application.resources.displayMetrics.density
}

internal class PerfBench {
    private fun benchmark(): String {
        try {
            val uptimeMillis = SystemClock.uptimeMillis()
            var i2 = 1
            var i3 = 0
            var i4 = 0
            while (true) {
                if (i2 >= 1000000) {
                    break
                }
                if (((4508713 % i2) * 11) % i2 == 0) {
                    i3++
                }
                if (i2 % 100 == 0 && SystemClock.uptimeMillis() - uptimeMillis > 2) {
                    break
                }
                i4++
                i2++
            }
            val i5 = i4 / 100

            val uptimeMillis2 = SystemClock.uptimeMillis()
            var f = 33.34f
            var i6 = 0
            var i7 = 0
            for (i8 in 1 until 1000000) {
                f += i8
                if ((19.239f * f) / 3.56f < 10000.0f) {
                    i6++
                }
                if (i8 % 100 == 0 && SystemClock.uptimeMillis() - uptimeMillis2 > 2) {
                    break
                }
                i7++
            }
            val i9 = i7 / 100

            val uptimeMillis3 = SystemClock.uptimeMillis()
            var i10 = 0
            var i11 = 0
            var d = 0.0
            while (d < 1000000.0) {
                if (sqrt(d) > 30.0) {
                    i10++
                }
                if (d.toInt() % 100 == 0 && SystemClock.uptimeMillis() - uptimeMillis3 > 2) {
                    break
                }
                i11++
                d += 1.0
            }
            val i12 = i10
            val i13 = i11 / 100

            val uptimeMillis4 = SystemClock.uptimeMillis()
            var i14 = 1
            var i15 = 0
            var i16 = 0
            val i = 1000000
            while (i14 < i) {
                if (
                    acos(i14.toDouble() / i.toDouble())
                    + asin(i14.toDouble() / i.toDouble())
                    + atan(i14.toDouble() / 1000000.toDouble())
                    > 1.5
                ) {
                    i15++
                }
                if (i14 % 100 == 0 && SystemClock.uptimeMillis() - uptimeMillis4 > 2) {
                    break
                }
                i16++
                i14++
            }
            val i17 = i16 / 100

            val uptimeMillis5 = SystemClock.uptimeMillis()
            var i18 = 0
            var i19 = 1
            while (i19 < 1000000 && SystemClock.uptimeMillis() - uptimeMillis5 <= 2) {
                i18++
                i19++
            }

            return "$i3,$i5,$i6,$i9,$i12,$i13,$i15,$i17,$i18"
        } catch (e: Exception) {
            println("ERROR(perf bench): $e")
            return "-1,-1,-1,-1,-1,-1,-1,-1,-1"
        }
    }

    fun benchmarkMultiple(n: Int): List<String> {
        val l = arrayListOf<String>()
        for (i in 0 until n) {
            l.add(benchmark())
        }
        return l
    }
}
