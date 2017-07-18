package de.danielglaser.himbeerheim.model

import de.danielglaser.himbeerheim.MainActivity
import de.danielglaser.himbeerheim.R
import java.io.Serializable

import java.util.ArrayList


/**
 * Created by Daniel on 18.07.2017.
 */

class Data : Serializable {

    companion object {
        private const val serialVersionUID: Long = 1
    }

    var buttonCommands: ArrayList<ButtonCommand> = ArrayList()

    init {
        load()
    }

    fun save() {
        Persistence().saveObject(`object` = this, filename = MainActivity.contextOfApplication.getString(R.string.DATA_FILENAME))
    }

    fun load() {
        var loadObject = Persistence().readObject(filename = MainActivity.contextOfApplication.getString(R.string.DATA_FILENAME))
        if (loadObject is Data) {
            this.buttonCommands = loadObject.buttonCommands
        }
    }
}

