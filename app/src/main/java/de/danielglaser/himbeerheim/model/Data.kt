package de.danielglaser.himbeerheim.model

import android.preference.PreferenceManager
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.view.BaseActivity
import de.danielglaser.himbeerheim.view.MainActivity
import java.io.Serializable

import java.util.ArrayList


/**
 * Created by Daniel on 18.07.2017.
 */

class Data : Serializable {

    companion object {
        private const val serialVersionUID: Long = 4
    }

    var m_buttonCommands: ArrayList<ButtonCommand> = ArrayList()
    private var m_host = ""
    private var m_port = Util.defaultPort
    private var m_username = Util.defaultUsername
    private var m_password = ""

    private var m_path = Util.defaultPath

    var editNoticeCounter = 0

    @Transient
    private lateinit var m_sshConnection: SSHConnection

    var host: String
        get() {
            return m_host
        }
        set(value) {
            m_host = value
            initSSHConnection()
        }

    var port: Int
        get() {
            return m_port
        }
        set(value) {
            m_port = value
            initSSHConnection()
        }

    var username: String
        get() {
            return m_username
        }
        set(value) {
            m_username = value
            initSSHConnection()
        }

    var password: String
        get() {
            return m_password
        }
        set(value) {
            m_password = value
            initSSHConnection()
        }

    var sshConnection: SSHConnection
        get() {
            if (m_sshConnection == null) {
                initSSHConnection()
            }
            return m_sshConnection
        }
        set(value) {
            m_sshConnection = value
            initSSHConnection()
        }

    init {
        load()
        initSSHConnection()
        m_sshConnection.connect()
    }

    fun updatePath(newPath: String) {
        m_path = newPath
        for (buttonCommand in m_buttonCommands) {
            buttonCommand.setPath(m_path)
        }
    }

    fun getPath(): String {
        return m_path
    }

    fun initSSHConnection() {
        m_sshConnection = SSHConnection(host = m_host, port = m_port, username = m_username, password = m_password)
    }

    fun save() {
        Persistence().saveObject(`object` = this, filename = BaseActivity.appContext.getString(R.string.DATA_FILENAME))
    }

    private fun load() {
        val loadObject = Persistence().readObject(filename = BaseActivity.appContext.getString(R.string.DATA_FILENAME))
        if (loadObject is Data) {
            this.m_buttonCommands = loadObject.m_buttonCommands
            this.m_host = loadObject.m_host
            this.m_port = loadObject.m_port
            this.m_username = loadObject.m_username
            this.m_password = loadObject.m_password
            this.editNoticeCounter = loadObject.editNoticeCounter
        }

        val prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext())

        val prefIP = prefs.getString(MainActivity.getContext().getString(R.string.ip_key), "")

        if (this.host.isNullOrBlank() && !prefIP.isNullOrBlank()) {
            this.host = prefIP
        }

        val prefPort = prefs.getString(MainActivity.getContext().getString(R.string.port_key), "")

        if (this.m_port == Util.defaultPort && !prefPort.isNullOrBlank()) {
            this.m_port = prefPort.toInt()
        }

        val prefUsername = prefs.getString(MainActivity.getContext().getString(R.string.username_key), "")

        if (this.m_username.equals(Util.defaultUsername) && !prefUsername.isNullOrBlank()) {
            this.m_username = prefUsername
        }

        val prefPassword = prefs.getString(MainActivity.getContext().getString(R.string.password_key), "")

        if (this.m_password.isNullOrBlank() && !prefPassword.isNullOrBlank()) {
            this.m_password = prefPassword
        }

        val prefPath = prefs.getString(MainActivity.getContext().getString(R.string.path_key), "")

        if (this.m_path.equals(Util.defaultPath) && !prefPath.isNullOrBlank()) {
            this.m_path = prefPath
        }
    }
}

