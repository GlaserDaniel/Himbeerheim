package de.danielglaser.himbeerheim.model

import java.io.Serializable

/**
 * Created by Daniel on 18.07.2017.
 */
class ButtonCommand : Serializable {

    companion object {
        private const val serialVersionUID: Long = 3
    }

    var title: String = ""

    private var m_code: ArrayList<Int> = ArrayList()

    private var m_letter: Int = 1

    private var m_path: String = "sudo ./raspberry-remote/send"

    var commandOn: String = ""
        get() {
            var commandString = ""
            commandString += m_path + " "
            for (c in m_code)
                commandString += c.toString()
            commandString += " " + m_letter.toString()
            commandString += " 1"
        return commandString
    }

    var commandOff: String = ""
        get() {
            var commandString = ""
            commandString += m_path + " "
            for (c in m_code)
                commandString += c.toString()
            commandString += " " + m_letter.toString()
            commandString += " 0"
            return commandString
        }

    constructor(title: String, code: ArrayList<Any>, letter: Int, path: String) {
        this.title = title
        setCode(code)
        m_letter = letter
        m_path = path
    }

    fun setPath(newPath: String) {
        m_path = newPath
    }

    fun getCode() : ArrayList<Int> {
        return m_code
    }

    fun getCodeBool() : ArrayList<Boolean> {
        val code: ArrayList<Boolean> = ArrayList()
         m_code.mapTo(code) { intToBool(it) }

        return code
    }

    fun setCode(code: ArrayList<Any>) {
        if (!code.isEmpty() && code[0] is Int) {
            m_code.clear()
            code.mapTo(m_code) {it as Int}
        }
        else if (!code.isEmpty() && code[0] is Boolean) {
            m_code.clear()
            code.mapTo(m_code) { boolToInt(it as Boolean) }
        } else {
            // TODO
        }
    }

    fun getLetter() : Int {
        return m_letter
    }

    fun getLetterString() : String {
        return intToLetter(m_letter)
    }

    fun setLetter(letter: Any) {
        if (letter is String) {
            m_letter = letterToInt(letter)
        } else if (letter is Int) {
            m_letter = letter
        }

    }

    private fun boolToInt(bool: Boolean) : Int {
        if (bool) {
            return 1
        } else {
            return 0
        }
    }

    private fun intToBool(int: Int) : Boolean {
        return int == 1
    }

    private fun letterToInt(letter: String) : Int {
        when (letter) {
            "A" -> return 1
            "B" -> return 2
            "C" -> return 3
            "D" -> return 4
        }
        return 0
    }

    private fun intToLetter(int: Int) : String {
        when (int) {
            1 -> return "A"
            2 -> return "B"
            3 -> return "C"
            4 -> return "D"
        }
        return "A"
    }

}