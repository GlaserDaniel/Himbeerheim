package de.danielglaser.himbeerheim.model

import android.widget.Button
import java.io.Serializable

/**
 * Created by Daniel on 18.07.2017.
 */
class ButtonCommand(var title: String, var command: String) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1
    }
}