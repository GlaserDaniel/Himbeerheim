package de.danielglaser.himbeerheim.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        view.command_EditText.setText(buttonCommand.command)

        view.cancel_Button.setOnClickListener {
            mCallback.onCancelSelectedListener()
        }

        view.delete_Button.setOnClickListener {
            mCallback.onDeleteSelectedListener(buttonCommand)
        }

        view.save_Button.setOnClickListener {
            buttonCommand.title = view.title_EditText.text.toString()
            buttonCommand.command = view.command_EditText.text.toString()
            mCallback.onSaveSelectedListener()
        }

        return view
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