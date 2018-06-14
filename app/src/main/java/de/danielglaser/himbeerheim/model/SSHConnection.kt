package de.danielglaser.himbeerheim.model

import android.annotation.SuppressLint
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

    private var mTAG = "SSHConnection"

    private var session: Session? = null

    fun connect(): String {
        val task = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg p0: Void?): String {
                try {
                    return internalConnect()
                } catch (e: Exception) {
                    Log.e(mTAG, "internalConnect", e)
                }
                return ""
            }
        }
        task.execute()
        return task.get()
    }

    fun sendSSHCommand(command: String): String {
        val task = @SuppressLint("StaticFieldLeak")
        object : AsyncTask<String, Void, String>() {
            override fun doInBackground(vararg params: String): String {
                try {
                    return internalSendSSHCommand(command)
                } catch (e: Exception) {
                    Log.e(mTAG, "internalSendSSHCommand", e)
                }

                return ""
            }
        }
        task.execute(command)
        return task.get()
    }

    private fun internalConnect(): String {
        val jsch = JSch()

        if (username.isBlank()) {
            return Util.usernameNotSet
        } else if (host.isBlank()) {
            return Util.hostNotSet
        }

        try {
            session = jsch.getSession(username, host, port)
        } catch (e: JSchException) {
            Log.e(mTAG, "GetSession", e)
            return ""
        }

        session!!.setPassword(password)

        // Avoid asking for key confirmation
        val prop = Properties()
        prop["StrictHostKeyChecking"] = "no"
        session!!.setConfig(prop)

        try {
            session!!.connect(1000)
        } catch (e: JSchException) {
            //Log.e(mTAG, "Session Connect", e)
            return Util.connectTimeout
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
            Log.e(mTAG, "session null or not connected")
            return ""
        }

        // SSH Channel
        val channelssh: ChannelExec?
        try {
            channelssh = session!!.openChannel("exec") as ChannelExec
        } catch (e: JSchException) {
            Log.e(mTAG, "openChannel", e)
            return ""
        }

        val baos = ByteArrayOutputStream()
        channelssh.outputStream = baos

        // Execute commandOn
        channelssh.setCommand(command)
        try {
            channelssh.connect()
        } catch (e: JSchException) {
            Log.e(mTAG, "Channelssh Connect", e)
        }

        channelssh.disconnect()

        Log.d(mTAG, "Result from SSH: " + baos.toString())
        return baos.toString()
    }
}
