package de.danielglaser.himbeerheim.view

import android.app.Fragment
import android.content.Context
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
                Toast.makeText(activity, "Settings", Toast.LENGTH_SHORT).show()
                mCallback.onSettingsSelected()
                return true
            }
            R.id.action_powerOffRaspberryPi -> {
                Toast.makeText(activity, "Schalte Raspberry Pi aus", Toast.LENGTH_SHORT).show()
                val result = sshConnection.sendSSHCommand("sudo shutdown -h 0")
                Toast.makeText(activity, "Result: " + result, Toast.LENGTH_SHORT).show()
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

        val commandsAdapter = CommandsAdapter(activity, data.buttonCommands, sshConnection, mCallback)

        view.buttons_gridView.adapter = commandsAdapter

        view.fab.setOnClickListener {
            val code = ArrayList<Any>()
            code.add(0)
            code.add(1)
            code.add(1)
            code.add(1)
            code.add(1)
            val newButtonCommand = ButtonCommand("Licht", code, 4)

            data.buttonCommands.add(newButtonCommand)

            mCallback.onButtonCommandSelected(newButtonCommand)

            if (data.editNoticeCounter > 0) {
                Toast.makeText(activity, getString(R.string.editNotice), Toast.LENGTH_SHORT).show()
                data.editNoticeCounter--
            }
            data.save()

            commandsAdapter.notifyDataSetChanged()
        }

        return view
    }

    private fun makeButton(buttonCommand: ButtonCommand) : Button {
        val button = Button(activity)
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

    lateinit var mCallback: MainActivityFragmentListener

    // Container Activity must implement this interface
    interface MainActivityFragmentListener {
        fun onButtonCommandSelected(buttonCommand: ButtonCommand)
        fun onSettingsSelected()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = context as MainActivityFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement OnHeadlineSelectedListener")
        }
    }
}
