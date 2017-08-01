package de.danielglaser.himbeerheim.model

import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.view.BaseActivity
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
    private var m_port = 22
    private var m_username = "pi"
    private var m_password = ""

    private var m_command = "sudo ./raspberry-remote/send"

    var editNoticeCounter = 0

    @Transient
    lateinit var m_sshConnection: SSHConnection

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

    init {
        load()
        initSSHConnection()
        m_sshConnection.connect()
    }

    fun updateCommand(newCommand: String) {
        m_command = newCommand
        for (buttonCommand in m_buttonCommands) {
            buttonCommand.setCommand(m_command)
        }
    }

    fun getCommand(): String {
        return m_command
    }

    private fun initSSHConnection() {
        m_sshConnection = SSHConnection(host = m_host, port = m_port, username = m_username, password = m_password)
    }

    fun save() {
        Persistence().saveObject(`object` = this, filename = BaseActivity.appContext.getString(R.string.DATA_FILENAME))
    }

    fun load() {
        var loadObject = Persistence().readObject(filename = BaseActivity.appContext.getString(R.string.DATA_FILENAME))
        if (loadObject is Data) {
            this.m_buttonCommands = loadObject.m_buttonCommands
            this.m_host = loadObject.m_host
            this.m_port = loadObject.m_port
            this.m_username = loadObject.m_username
            this.m_password = loadObject.m_password
            this.editNoticeCounter = loadObject.editNoticeCounter
        }
    }
}

