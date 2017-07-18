package de.danielglaser.himbeerheim.model

import de.danielglaser.himbeerheim.view.MainActivity
import de.danielglaser.himbeerheim.R
import java.io.Serializable

import java.util.ArrayList


/**
 * Created by Daniel on 18.07.2017.
 */

class Data : Serializable {

    companion object {
        private const val serialVersionUID: Long = 2
    }

    var buttonCommands: ArrayList<ButtonCommand> = ArrayList()
    var hostname = "192.168.178.43"
    var port = 22
    var username = "pi"
    var password = "Roter!Weg!3"

    var editNoticeCounter = 3

    @Transient
    lateinit var sshConnection: SSHConnection

    init {
        load()
        initSSHConnection()
        sshConnection.connect()
    }

    private fun initSSHConnection() {
        sshConnection = SSHConnection(hostname = hostname, port = port, username = username, password = password)
    }

    fun save() {
        Persistence().saveObject(`object` = this, filename = MainActivity.contextOfApplication.getString(R.string.DATA_FILENAME))
    }

    fun load() {
        var loadObject = Persistence().readObject(filename = MainActivity.contextOfApplication.getString(R.string.DATA_FILENAME))
        if (loadObject is Data) {
            this.buttonCommands = loadObject.buttonCommands
            this.hostname = loadObject.hostname
            this.port = loadObject.port
            this.username = loadObject.username
            this.password = loadObject.password
            this.editNoticeCounter = loadObject.editNoticeCounter
        }
    }
}

