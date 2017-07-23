package de.danielglaser.himbeerheim.view

import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.Button
import android.widget.Toast
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.model.SSHConnection
import de.danielglaser.himbeerheim.model.ButtonCommand
import de.danielglaser.himbeerheim.model.Data

import kotlinx.android.synthetic.main.fragment_main.view.*

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment() : Fragment() {

    constructor(data: Data) : this() {
        retainInstance = true
        this.data = data
    }

    lateinit var data: Data

    lateinit var sshConnection: SSHConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_powerOffRaspberryPi -> {
                Toast.makeText(context, "Schalte Raspberry Pi aus", Toast.LENGTH_SHORT).show();
                val result = sshConnection.sendSSHCommand("sudo shutdown -h 0");
                Toast.makeText(context, "Result: " + result, Toast.LENGTH_SHORT).show();
                return true
            }
        //R.id.search -> consume { MenuItemCompat.expandActionView(item) }
        //R.id.settings -> consume { navigateToSettings() }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_main, container, false)

        sshConnection = data.sshConnection

        var commandsAdapter = CommandsAdapter(context, data.buttonCommands, sshConnection, mCallback)

        view.buttons_gridView.adapter = commandsAdapter

        view.fab.setOnClickListener {
            val newButtonCommand = ButtonCommand("Licht", 0, 1, 1, 1, 1, 4)

            data.buttonCommands.add(newButtonCommand)

            mCallback.onButtonCommandSelected(newButtonCommand)

            if (data.editNoticeCounter > 0) {
                Toast.makeText(context, getString(R.string.editNotice), Toast.LENGTH_SHORT).show()
                data.editNoticeCounter--
            }
            data.save()

            commandsAdapter.notifyDataSetChanged()
        }

        return view
    }

    private fun makeButton(buttonCommand: ButtonCommand) : Button {
        val button = Button(context)
        button.text = buttonCommand.title
        button.layoutParams = Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        button.setOnClickListener {
            sshConnection.sendSSHCommand(command = buttonCommand.command)
        }
        button.setOnLongClickListener{
            mCallback.onButtonCommandSelected(buttonCommand)

            true
        }
        return button
    }

    lateinit var mCallback: OnButtonCommandSelectedListener

    // Container Activity must implement this interface
    interface OnButtonCommandSelectedListener {
        fun onButtonCommandSelected(buttonCommand: ButtonCommand)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = context as OnButtonCommandSelectedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener")
        }
    }
}
