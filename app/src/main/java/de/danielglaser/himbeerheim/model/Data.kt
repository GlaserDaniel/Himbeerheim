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
        private const val serialVersionUID: Long = 3
    }

    var buttonCommands: ArrayList<ButtonCommand> = ArrayList()
    var host = ""
    var port = 22
    var username = "pi"
    var password = ""

    private var command = "sudo ./raspberry-remote/send"

    var editNoticeCounter = 0

    @Transient
    lateinit var sshConnection: SSHConnection

    init {
        load()
        initSSHConnection()
        sshConnection.connect()
    }

    fun updateCommand(newCommand: String) {
        command = newCommand
        for (buttonCommand in buttonCommands) {
            buttonCommand.setCommand(command)
        }
    }

    fun getCommand() : String {
        return command
    }

    private fun initSSHConnection() {
        sshConnection = SSHConnection(host = host, port = port, username = username, password = password)
    }

    fun save() {
        Persistence().saveObject(`object` = this, filename = BaseActivity.appContext.getString(R.string.DATA_FILENAME))
    }

    fun load() {
        var loadObject = Persistence().readObject(filename = BaseActivity.appContext.getString(R.string.DATA_FILENAME))
        if (loadObject is Data) {
            this.buttonCommands = loadObject.buttonCommands
            this.host = loadObject.host
            this.port = loadObject.port
            this.username = loadObject.username
            this.password = loadObject.password
            this.editNoticeCounter = loadObject.editNoticeCounter
        }
    }
}

