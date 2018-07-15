package de.danielglaser.himbeerheim.view

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
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

    @SuppressLint("ValidFragment")
    constructor(data: Data) : this() {
        retainInstance = true
        this.mData = data
    }

    private lateinit var mData: Data

    private lateinit var sshConnection: SSHConnection

    private lateinit var commandsAdapter: CommandsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true

        //Maybe bugfix for long in background bug
        if (mData == null) {
            mData = Data()
        }

        sshConnection = mData.m_sshConnection

        commandsAdapter = CommandsAdapter(activity, mData.m_buttonCommands, sshConnection, mCallback)
    }

    override fun onResume() {
        super.onResume()

        handleGridViewSize(resources.configuration)
    }

    private fun handleGridViewSize(conf: Configuration) {
        if (conf.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_XLARGE)) {
            //großes Tablet
            if (conf.orientation == Configuration.ORIENTATION_PORTRAIT) {
                view.buttons_gridView.numColumns = 3
            } else if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE){
                view.buttons_gridView.numColumns = 8
            }
        } else if (conf.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) {
            //kleines Tablet
            if (conf.orientation == Configuration.ORIENTATION_PORTRAIT) {
                view.buttons_gridView.numColumns = 2
            } else if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE){
                view.buttons_gridView.numColumns = 4
            }
        } else if (conf.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_NORMAL)) {
            //normales Smartphone
            if (conf.orientation == Configuration.ORIENTATION_PORTRAIT) {
                view.buttons_gridView.numColumns = 2
            } else if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE){
                view.buttons_gridView.numColumns = 4
            }
        } else if (conf.isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_SMALL)) {
            //kleines Smartphone
            if (conf.orientation == Configuration.ORIENTATION_PORTRAIT) {
                view.buttons_gridView.numColumns = 1
            } else if (conf.orientation == Configuration.ORIENTATION_LANDSCAPE){
                view.buttons_gridView.numColumns = 2
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                mCallback.onSettingsSelected()
                true
            }
            R.id.action_powerOffRaspberryPi -> {
                Toast.makeText(activity, getString(R.string.turnPiOff), Toast.LENGTH_SHORT).show()
                val result = sshConnection.sendSSHCommand("sudo shutdown -h 0")
                Toast.makeText(activity, "Result: $result", Toast.LENGTH_SHORT).show()
                true
            }
        //R.id.search -> consume { MenuItemCompat.expandActionView(item) }
        //R.id.settings -> consume { navigateToSettings() }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null
        }

        val view = inflater!!.inflate(R.layout.fragment_main, container, false)

        view.buttons_gridView.adapter = commandsAdapter

        view.fab.setOnClickListener {
            val code = ArrayList<Any>()
            code.add(0)
            code.add(1)
            code.add(1)
            code.add(1)
            code.add(1)
            val newButtonCommand = ButtonCommand("Licht", code, 4, mData.getPath())

            mData.m_buttonCommands.add(newButtonCommand)

            mCallback.onButtonCommandSelected(newButtonCommand)

            if (mData.editNoticeCounter > 0) {
                Toast.makeText(activity, getString(R.string.editNotice), Toast.LENGTH_SHORT).show()
                mData.editNoticeCounter--
            }
            mData.save()

            commandsAdapter.notifyDataSetChanged()
        }

        return view
    }

    /*private fun makeButton(buttonCommand: ButtonCommand) : Button {
        val button = Button(activity)
        button.text = buttonCommand.title
        button.layoutParams = Toolbar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        button.setOnClickListener {
            m_sshConnection.sendSSHCommand(command = buttonCommand.commandOn)
        }
        button.setOnLongClickListener{
            mCallback.onButtonCommandSelected(buttonCommand)

            true
        }
        return button
    }*/

    private lateinit var mCallback: MainActivityFragmentListener

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
