package com.kanna.sensorcollector

import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class WebSocketClient(serverUri: URI) : WebSocketClient(serverUri) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        Log.d("SocketCollector", "Connected to server")
    }

    override fun onMessage(message: String?) {
        Log.d("SocketCollector", "Received message: $message")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Log.d("SocketCollector", "Disconnected from server with exit code $code and reason: $reason")
    }

    override fun onError(ex: Exception?) {
        Log.e("SocketCollector", "Error occurred: ${ex?.message}")
    }

    fun sendMessage(message: String) {
        if (this.isOpen) {
            this.send(message)
        } else {
            println("WebSocket is not open. Unable to send message.")
        }
    }
}
