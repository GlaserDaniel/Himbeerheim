package de.danielglaser.himbeerheim.view

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.os.Build
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.model.ButtonCommand
import de.danielglaser.himbeerheim.model.SSHConnection
import de.danielglaser.himbeerheim.model.Util
import kotlinx.android.synthetic.main.command_item.view.*


/**
 * Created by Daniel on 23.07.2017.
 */
class CommandsAdapter : ArrayAdapter<ButtonCommand> {

    private val TAG = "CommandsAdapter"

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
            onLongClickStartDrag(view, position)

            true
        }

        view.on_button.setOnLongClickListener {
            onLongClickStartDrag(view, position)

            true
        }

        view.off_button.setOnLongClickListener {
            onLongClickStartDrag(view, position)

            true
        }

        view.editButton.setOnLongClickListener {
            onLongClickStartDrag(view, position)

            true
        }

        view.setOnDragListener { v, event ->

            //Log.i(TAG, "enterOnDrag")

            // Defines a variable to store the action type for the incoming event
            val action = event.action

            //Log.i(TAG, "Action: $action")

            // Handles each of the expected events
            when (action) {

                DragEvent.ACTION_DRAG_STARTED -> {

                    // Determines if this View can accept the dragged data
                    if (event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        // As an example of what your application might do,
                        // applies a blue color tint to the View to indicate that it can accept
                        // data.
                        //v.setColorFilter(Color.BLUE)
                        //var filter = ColorFilter()
                        //v.background.setColorFilter()
                        //v.alpha = 0.5F

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate()

                        // returns true to indicate that the View can accept the dragged data.
                        return@setOnDragListener true
                    }

                    // Returns false. During the current drag and drop operation, this View will
                    // not receive events again until ACTION_DRAG_ENDED is sent.
                    return@setOnDragListener false
                }

                DragEvent.ACTION_DRAG_ENTERED -> {

                    v.alpha = 0.5F

                    /*// Applies a green tint to the View. Return true; the return value is ignored.

                    v.setColorFilter(Color.GREEN)

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate()*/

                    return@setOnDragListener true
                }

                DragEvent.ACTION_DRAG_LOCATION -> {

                    // Ignore the event
                    //return true
                    return@setOnDragListener true
                }

                DragEvent.ACTION_DRAG_EXITED -> {

                    v.alpha = 1F

                    /*// Re-sets the color tint to blue. Returns true; the return value is ignored.
                    v.setColorFilter(Color.BLUE)

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate()*/

                    return@setOnDragListener true
                }

                DragEvent.ACTION_DROP -> {

                    // Gets the item containing the dragged data
                    val item = event.clipData.getItemAt(0)

                    // Gets the text data from the item.
                    val dragData = item.text.toString()

                    // Displays a message containing the dragged data.
                    //Toast.makeText(context, "Dragged data is $dragData, and position is $position", Toast.LENGTH_LONG).show()

                    val tmpItem = items!![Integer.parseInt(dragData)]

                    items!![Integer.parseInt(dragData)] = items!![position]

                    items!![position] = tmpItem

                    // Turns off any color tints
                    //v.clearColorFilter()
                    v.alpha = 1F

                    // Invalidates the view to force a redraw
                    v.invalidate()

                    this.notifyDataSetChanged()

                    // Returns true. DragEvent.getResult() will return true.
                    return@setOnDragListener true
                }

                DragEvent.ACTION_DRAG_ENDED -> {

                    v.alpha = 1F

                    // Turns off any color tinting
                    //v.clearColorFilter()

                    // Invalidates the view to force a redraw
                    v.invalidate()

                    // Does a getResult(), and displays what happened.
                    if (event.result) {
                        //Toast.makeText(context, "The drop was handled.", Toast.LENGTH_LONG).show()

                    } else {
                        //Toast.makeText(context, "The drop didn't work.", Toast.LENGTH_LONG).show()

                    }

                    // returns true; the value is ignored.
                    return@setOnDragListener true
                }

            // An unknown action type was received.
                else -> Log.e("DragDrop Error", "Unknown action type received by OnDragListener.")
            }

            return@setOnDragListener false
        }

        return view
    }

    private fun onLongClickStartDrag(view: View, position: Int) {
        val clipData = ClipData.newPlainText("position", position.toString())
        val myShadowBuilder = View.DragShadowBuilder(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.startDragAndDrop(clipData, myShadowBuilder, null, 0)
        } else {
            view.startDrag(clipData, myShadowBuilder, null, 0)
        }
        view.alpha = 0.5F
    }

    private fun handleError(error: String) {
        when {
            error == Util.usernameNotSet -> Toast.makeText(context, context.getString(R.string.error_usernameNotSet), Toast.LENGTH_SHORT).show()
            error == Util.hostNotSet -> Toast.makeText(context, context.getString(R.string.error_hostNotSet), Toast.LENGTH_SHORT).show()
            error == Util.connectTimeout -> Toast.makeText(context, context.getString(R.string.error_connectTimeout), Toast.LENGTH_SHORT).show()
        }
    }
}