package de.danielglaser.himbeerheim.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.model.ButtonCommand
import kotlinx.android.synthetic.main.fragment_edit_command.view.*

/**
 * Created by Daniel on 18.07.2017.
 */
class EditCommandFragment() : Fragment() {

    constructor(buttonCommand: ButtonCommand) : this() {
        retainInstance = true
        this.buttonCommand = buttonCommand
    }

    lateinit var buttonCommand: ButtonCommand

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_edit_command, container, false)
        view.title_EditText.setText(buttonCommand.title)
        //view.command_EditText.setText(buttonCommand.command)

        view.switch1.isChecked = intToBool(buttonCommand.code1)
        view.switch2.isChecked = intToBool(buttonCommand.code2)
        view.switch3.isChecked = intToBool(buttonCommand.code3)
        view.switch4.isChecked = intToBool(buttonCommand.code4)
        view.switch5.isChecked = intToBool(buttonCommand.code5)

        view.spinner.setSelection(getIndex(view.spinner, intToLetter(buttonCommand.letter)))

        view.cancel_Button.setOnClickListener {
            mCallback.onCancelSelectedListener()
        }

        view.delete_Button.setOnClickListener {
            mCallback.onDeleteSelectedListener(buttonCommand)
        }

        view.save_Button.setOnClickListener {
            buttonCommand.title = view.title_EditText.text.toString()
            //buttonCommand.command = view.command_EditText.text.toString()

            buttonCommand.code1 = boolToInt(view.switch1.isChecked)
            buttonCommand.code2 = boolToInt(view.switch2.isChecked)
            buttonCommand.code3 = boolToInt(view.switch3.isChecked)
            buttonCommand.code4 = boolToInt(view.switch4.isChecked)
            buttonCommand.code5 = boolToInt(view.switch5.isChecked)

            buttonCommand.letter = letterToInt(view.spinner.selectedItem as String)

            mCallback.onSaveSelectedListener()
        }

        return view
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

    private fun getIndex(spinner: Spinner, myString: String): Int {

        var index = 0

        for (i in 0..spinner.count - 1) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i
            }
        }
        return index
    }

    lateinit var mCallback: OnBackToMainSelectedListener

    // Container Activity must implement this interface
    interface OnBackToMainSelectedListener {
        fun onSaveSelectedListener()
        fun onCancelSelectedListener()
        fun onDeleteSelectedListener(buttonCommand: ButtonCommand)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = context as OnBackToMainSelectedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener")
        }
    }
}