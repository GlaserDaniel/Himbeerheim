package de.danielglaser.himbeerheim.model

import android.os.AsyncTask
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session

import java.io.ByteArrayOutputStream
import java.util.Properties

/**
 * Created by Daniel on 16.07.2017.
 */

class SSHConnection(internal var hostname: String, internal var port: Int, internal var username: String, internal var password: String) {

    fun sendSSHCommand(command: String): String? {
        var task = object : AsyncTask<String, Void, String>() {
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

    private fun internalSendSSHCommand(command: String): String? {
        val jsch = JSch()
        var session: Session? = null
        try {
            session = jsch.getSession(username, hostname, port)
        } catch (e: JSchException) {
            e.printStackTrace()
        }

        session!!.setPassword(password)

        // Avoid asking for key confirmation
        val prop = Properties()
        prop.put("StrictHostKeyChecking", "no")
        session.setConfig(prop)

        try {
            session.connect()
        } catch (e: JSchException) {
            e.printStackTrace()
            return null
        }

        // SSH Channel
        var channelssh: ChannelExec? = null
        try {
            channelssh = session.openChannel("exec") as ChannelExec
        } catch (e: JSchException) {
            e.printStackTrace()
            return null
        }

        val baos = ByteArrayOutputStream()
        channelssh!!.outputStream = baos

        // Execute command
        channelssh.setCommand(command)
        try {
            channelssh.connect()
        } catch (e: JSchException) {
            e.printStackTrace()
        }

        channelssh.disconnect()

        return baos.toString()
    }
}
