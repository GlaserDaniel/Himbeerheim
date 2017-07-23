package de.danielglaser.himbeerheim.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import de.danielglaser.himbeerheim.model.ButtonCommand
import android.view.LayoutInflater
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.model.SSHConnection

import kotlinx.android.synthetic.main.command_item.view.*

/**
 * Created by Daniel on 23.07.2017.
 */
class CommandsAdapter : ArrayAdapter<ButtonCommand> {

    constructor(context: Context, items: ArrayList<ButtonCommand>, sshConnection: SSHConnection, mCallback: MainActivityFragment.OnButtonCommandSelectedListener) : super(context, 0, items) {
        this.items = items
        this.sshConnection = sshConnection
        this.mCallback = mCallback
        vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    private var items: ArrayList<ButtonCommand>? = ArrayList()
    private var vi: LayoutInflater? = null
    private var sshConnection: SSHConnection
    private var mCallback: MainActivityFragment.OnButtonCommandSelectedListener

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        var buttonCommand = items!!.get(position)

        view = vi!!.inflate(R.layout.command_item, null)

        view.commandTitle_textView.text = buttonCommand.title

        view.on_button.setOnClickListener {
            sshConnection.sendSSHCommand(command = "sudo ./send " + buttonCommand.command + " 1")
        }

        view.off_button.setOnClickListener {
            sshConnection.sendSSHCommand(command = "sudo ./send " + buttonCommand.command + " 0")
        }

        view.on_button.setOnLongClickListener {
            mCallback.onButtonCommandSelected(buttonCommand)

            true
        }

        view.off_button.setOnLongClickListener {
            mCallback.onButtonCommandSelected(buttonCommand)

            true
        }

        view.setOnLongClickListener {
            mCallback.onButtonCommandSelected(buttonCommand)

            true
        }

        return view
    }
}