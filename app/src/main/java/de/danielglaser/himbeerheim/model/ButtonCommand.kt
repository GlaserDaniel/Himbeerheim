package de.danielglaser.himbeerheim.model

import android.widget.Button
import java.io.Serializable

/**
 * Created by Daniel on 18.07.2017.
 */
class ButtonCommand : Serializable {

    companion object {
        private const val serialVersionUID: Long = 1
    }

    var title: String = ""

    var code1: Int = 0
    var code2: Int = 0
    var code4: Int = 0
    var code5: Int = 0
    var code3: Int = 0

    var letter: Int = 1

    var command: String = ""
        get() {
        return code1.toString() + "" + code2.toString() + "" + code3.toString() + "" + code4.toString() + "" + code5.toString() + " " + letter.toString()
    }

    constructor(title: String, code1: Int, code2: Int, code3: Int, code4: Int, code5: Int, letter: Int) {
        this.title = title
        this.code1 = code1
        this.code2 = code2
        this.code3 = code3
        this.code4 = code4
        this.code5 = code5
        this.letter = letter
    }

}