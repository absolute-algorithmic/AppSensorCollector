package com.kanna.sensorcollector

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import org.json.JSONObject
import java.net.URI
import java.security.MessageDigest


class MainActivity : AppCompatActivity() {

    private lateinit var webSocketClient: WebSocketClient
    private lateinit var sensorEventListener: SensorEventListener
    private lateinit var systemInfo: SystemInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val serverUri = URI("ws://192.168.100.22:8080") // IPV4 URI
        webSocketClient = WebSocketClient(serverUri)

        webSocketClient.connect()

        systemInfo = SystemInfo(application)

        sensorEventListener = SensorEventListener(application, webSocketClient, listOf(
            Sensor.TYPE_ACCELEROMETER,
            Sensor.TYPE_GRAVITY,
            Sensor.TYPE_MAGNETIC_FIELD,
            Sensor.TYPE_GYROSCOPE
        ))

        sendSystemInfo()
        sensorEventListener.startListening()
    }

    private fun sendSystemInfo() {
        val json = JSONObject().apply {
            put("id", systemInfo.id)
            put("messageType", systemInfo.messageType)
            put("build", JSONObject(mapOf(
                "manufacturer" to systemInfo.build.manufacturer,
                "hardware" to systemInfo.build.hardware,
                "model" to systemInfo.build.model,
                "bootloader" to systemInfo.build.bootloader,
                "product" to systemInfo.build.product,
                "tags" to systemInfo.build.tags,
                "type" to systemInfo.build.type,
                "user" to systemInfo.build.user,
                "display" to systemInfo.build.display,
                "board" to systemInfo.build.board,
                "brand" to systemInfo.build.brand,
                "device" to systemInfo.build.device,
                "fingerprint" to systemInfo.build.fingerprint,
                "host" to systemInfo.build.host,
                "id" to systemInfo.build.id
            )))
            put("version", JSONObject(mapOf(
                "release" to systemInfo.build.version.release,
                "codename" to systemInfo.build.version.codename,
                "incremental" to systemInfo.build.version.incremental,
                "sdkInt" to systemInfo.build.version.sdkInt
            )))
            put("screen", JSONObject(mapOf(
                "heightPixels" to systemInfo.screen.heightPixels,
                "widthPixels" to systemInfo.screen.widthPixels,
                "density" to systemInfo.screen.density
            )))
            put("sensorList", systemInfo.sensorList)
            put("perfBench", systemInfo.perfBench)
        }

        webSocketClient.sendMessage(json.toString())
    }

    private inner class SensorEventListener(
        application: Application,
        private val webSocketClient: WebSocketClient,
        private val sensorsToListen: List<Int>
    ) : android.hardware.SensorEventListener {
        private val sensorManager =
            application.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        private var lastSensors: HashMap<Int, SendableSensorEvent> = HashMap()
        private var eventsSent: HashMap<Int, Int> = HashMap()

        fun startListening() {
            val handlerThread = HandlerThread("sensorEventListener")
            handlerThread.start()
            val handler = Handler(handlerThread.looper)

            for (type in sensorsToListen) {
                val defaultSensor = sensorManager.getDefaultSensor(type)
                sensorManager.registerListener(
                    this,
                    defaultSensor,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    handler
                )
            }
        }

        private fun send(event: SendableSensorEvent) {
            Log.v("SensorCollector", "Sending sensor event $event")
            webSocketClient.sendMessage(Gson().toJson(event))
        }

        private fun stop() {
            sensorManager.unregisterListener(this)
            webSocketClient.close()
        }

        override fun onSensorChanged(event: SensorEvent) {
            val sendableEvent = SendableSensorEvent(systemInfo.id, event)
            val lastEventTimestamp = this.lastSensors[sendableEvent.type]?.timestamp ?: 0

            if (sendableEvent.timestamp - lastEventTimestamp >= 100e6) {
                send(sendableEvent)
                this.lastSensors[sendableEvent.type] = sendableEvent

                val newCount: Int = (eventsSent[sendableEvent.type] ?: 0) + 1
                eventsSent[sendableEvent.type] = newCount
            }

            if (eventsSent.values.all { it >= 50 }) {
                this.stop()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    internal data class SendableSensorEvent(
        val id: String,
        val messageType: String,
        val timestamp: Long,
        val accuracy: Int,
        val type: Int,
        val x: Float,
        val y: Float,
        val z: Float
    ) {
        constructor(id: String, sensorEvent: SensorEvent) : this(
            id,
            "sensor",
            sensorEvent.timestamp,
            sensorEvent.accuracy,
            sensorEvent.sensor.type,
            sensorEvent.values[0],
            sensorEvent.values[1],
            sensorEvent.values[2]
        )
    }
}
