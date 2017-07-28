package de.danielglaser.himbeerheim.view

import android.app.Fragment
import android.content.Context
import android.os.Bundle
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

        val code = buttonCommand.getCodeBool()

        view.switch1.isChecked = code[0]
        view.switch2.isChecked = code[1]
        view.switch3.isChecked = code[2]
        view.switch4.isChecked = code[3]
        view.switch5.isChecked = code[4]

        view.spinner.setSelection(getIndex(view.spinner, buttonCommand.getLetterString()))

        view.cancel_Button.setOnClickListener {
            mCallback.onCancelSelectedListener()
        }

        view.delete_Button.setOnClickListener {
            mCallback.onDeleteSelectedListener(buttonCommand)
        }

        view.save_Button.setOnClickListener {
            buttonCommand.title = view.title_EditText.text.toString()
            //buttonCommand.command = view.command_EditText.text.toString()

            var code = ArrayList<Any>()
            code.add(view.switch1.isChecked)
            code.add(view.switch2.isChecked)
            code.add(view.switch3.isChecked)
            code.add(view.switch4.isChecked)
            code.add(view.switch5.isChecked)

            buttonCommand.setCode(code)

            buttonCommand.setLetter(view.spinner.selectedItem)

            mCallback.onSaveSelectedListener()
        }

        return view
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

    lateinit var mCallback: EditCommandListener

    // Container Activity must implement this interface
    interface EditCommandListener {
        fun onSaveSelectedListener()
        fun onCancelSelectedListener()
        fun onDeleteSelectedListener(buttonCommand: ButtonCommand)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = context as EditCommandListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener")
        }
    }
}