package de.danielglaser.himbeerheim.view

import android.content.ClipData
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import de.danielglaser.himbeerheim.model.ButtonCommand
import android.view.LayoutInflater
import android.widget.Toast
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.model.SSHConnection
import de.danielglaser.himbeerheim.model.Util

import kotlinx.android.synthetic.main.command_item.view.*

/**
 * Created by Daniel on 23.07.2017.
 */
class CommandsAdapter : ArrayAdapter<ButtonCommand> {

    constructor(context: Context, items: ArrayList<ButtonCommand>, sshConnection: SSHConnection, mCallback: MainActivityFragment.MainActivityFragmentListener) : super(context, 0, items) {
        this.items = items
        this.sshConnection = sshConnection
        this.mCallback = mCallback
        vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    private var items: ArrayList<ButtonCommand>? = ArrayList()
    private var vi: LayoutInflater? = null
    private var sshConnection: SSHConnection
    private var mCallback: MainActivityFragment.MainActivityFragmentListener

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val buttonCommand = items!!.get(position)

        val view = vi!!.inflate(R.layout.command_item, null)

        view.commandTitle_textView.text = buttonCommand.title

        view.on_button.setOnClickListener {
            val result = sshConnection.sendSSHCommand(command = buttonCommand.commandOn)
            handleError(result)
        }

        view.off_button.setOnClickListener {
            val result = sshConnection.sendSSHCommand(command = buttonCommand.commandOff)
            handleError(result)
        }

        view.editButton.setOnClickListener {
            mCallback.onButtonCommandSelected(buttonCommand)
        }

        view.setOnLongClickListener {
            onLongClickStartDrag(view)

            true
        }

        view.on_button.setOnLongClickListener {
            onLongClickStartDrag(view)

            true
        }

        view.off_button.setOnLongClickListener {
            onLongClickStartDrag(view)

            true
        }

        view.editButton.setOnLongClickListener {
            onLongClickStartDrag(view)

            true
        }

        return view
    }

    private fun onLongClickStartDrag(view: View) {
        val clipData = ClipData.newPlainText("","")
        val myShadowBuilder = View.DragShadowBuilder(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.startDragAndDrop(clipData, myShadowBuilder, null, 0)
        } else {
            view.startDrag(clipData, myShadowBuilder, null, 0)
        }
    }

    private fun handleError(error: String) {
        when {
            error == Util.usernameNotSet -> Toast.makeText(context, context.getString(R.string.error_usernameNotSet), Toast.LENGTH_SHORT).show()
            error == Util.hostNotSet -> Toast.makeText(context, context.getString(R.string.error_hostNotSet), Toast.LENGTH_SHORT).show()
            error == Util.connectTimeout -> Toast.makeText(context, context.getString(R.string.error_connectTimeout), Toast.LENGTH_SHORT).show()
        }
    }
}