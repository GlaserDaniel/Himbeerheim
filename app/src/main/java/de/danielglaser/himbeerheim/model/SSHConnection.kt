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

    fun connect() {
        val task = object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                try {
                    internalConnect()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return null
            }
        }
        task.execute()
    }

    fun sendSSHCommand(command: String): String? {
        val task = object : AsyncTask<String, Void, String>() {
            override fun doInBackground(vararg params: String): String? {
                try {
                    internalSendSSHCommand(command)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return null
            }
        }
        task.execute(command)
        return task.get()
    }

    private fun internalConnect() {
        val jsch = JSch()

        try {
            session = jsch.getSession(username, host, port)
        } catch (e: JSchException) {
            e.printStackTrace()
            return
        }

        session!!.setPassword(password)

        // Avoid asking for key confirmation
        val prop = Properties()
        prop.put("StrictHostKeyChecking", "no")
        session!!.setConfig(prop)

        try {
            session!!.connect()
        } catch (e: JSchException) {
            e.printStackTrace()
            return
        }
    }

    private fun internalSendSSHCommand(command: String): String? {
        var counter = 5

        while ((session == null || !session!!.isConnected) && counter > 0) {
            internalConnect()
            counter--
        }

        if (session == null || !session!!.isConnected) {
            return null
        }

        // SSH Channel
        val channelssh: ChannelExec?
        try {
            channelssh = session!!.openChannel("exec") as ChannelExec
        } catch (e: JSchException) {
            e.printStackTrace()
            return null
        }

        val baos = ByteArrayOutputStream()
        channelssh.outputStream = baos

        // Execute commandOn
        channelssh.setCommand(command)
        try {
            channelssh.connect()
        } catch (e: JSchException) {
            e.printStackTrace()
        }

        channelssh.disconnect()

        Log.d(TAG, "Result from SSH: " + baos.toString())
        return baos.toString()
    }
}
