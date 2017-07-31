package de.danielglaser.himbeerheim.model

import android.os.AsyncTask
import android.util.Log
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session

import java.io.ByteArrayOutputStream
import java.util.Properties

/**
 * Created by Daniel on 16.07.2017.
 */

class SSHConnection(internal var host: String, internal var port: Int, internal var username: String, internal var password: String) {

    private var TAG = "SSHConnection"

    var session: Session? = null

    fun connect(): String {
        val task = object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                try {
                    return internalConnect()
                } catch (e: Exception) {
                    Log.e(TAG, "internalConnect", e)
                }
                return ""
            }
        }
        task.execute()
        return task.get()
    }

    fun sendSSHCommand(command: String): String {
        val task = object : AsyncTask<String, Void, String>() {
            override fun doInBackground(vararg params: String): String {
                try {
                    return internalSendSSHCommand(command)
                } catch (e: Exception) {
                    Log.e(TAG, "internalSendSSHCommand", e)
                }

                return ""
            }
        }
        task.execute(command)
        return task.get()
    }

    private fun internalConnect(): String {
        val jsch = JSch()

        if (username.isNullOrBlank()) {
            return Util.usernameNotSet
        } else if (host.isNullOrBlank()) {
            return Util.hostNotSet
        }

        try {
            session = jsch.getSession(username, host, port)
        } catch (e: JSchException) {
            Log.e(TAG, "GetSession", e)
            return ""
        }

        session!!.setPassword(password)

        // Avoid asking for key confirmation
        val prop = Properties()
        prop.put("StrictHostKeyChecking", "no")
        session!!.setConfig(prop)

        try {
            session!!.connect()
        } catch (e: JSchException) {
            Log.e(TAG, "Session Connect", e)
            return ""
        }

        return ""
    }

    private fun internalSendSSHCommand(command: String): String {
        var counter = 5

        while ((session == null || !session!!.isConnected) && counter > 0) {
            val result = internalConnect()
            if (result.isNotEmpty()) {
                return result
            }
            counter--
        }

        if (session == null || !session!!.isConnected) {
            Log.e(TAG, "session null or not connected")
            return ""
        }

        // SSH Channel
        val channelssh: ChannelExec?
        try {
            channelssh = session!!.openChannel("exec") as ChannelExec
        } catch (e: JSchException) {
            Log.e(TAG, "openChannel", e)
            return ""
        }

        val baos = ByteArrayOutputStream()
        channelssh.outputStream = baos

        // Execute commandOn
        channelssh.setCommand(command)
        try {
            channelssh.connect()
        } catch (e: JSchException) {
            Log.e(TAG, "Channelssh Connect", e)
        }

        channelssh.disconnect()

        Log.d(TAG, "Result from SSH: " + baos.toString())
        return baos.toString()
    }
}
