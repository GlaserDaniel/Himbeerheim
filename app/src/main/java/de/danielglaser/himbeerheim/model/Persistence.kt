package de.danielglaser.himbeerheim.model

import android.content.ContentValues
import android.util.Log
import de.danielglaser.himbeerheim.view.MainActivity
import java.io.*

/**
 * Created by Daniel on 18.07.2017.
 */
class Persistence {
    // this is the general method to serialize an object
    fun saveObject(`object`: Any, filename: String) {
        try {
            val file = File(MainActivity.contextOfApplication.filesDir, filename)
            val fos = FileOutputStream(file)
            val os = ObjectOutputStream(fos)
            os.writeObject(`object`)
            os.close()
            fos.close()
        } catch (e: Exception) { //TODO Eigentlich nur IOException, aber kann im Moment auch ConcurrentModificationException kommen
            Log.e(ContentValues.TAG, "Fehler beim Speichern des Objektes", e)
        }
    }

    // this is the general method to serialize an object
    fun readObject(filename: String): Any? {
        var result: Any? = null
        try {
            val file = File(MainActivity.contextOfApplication.filesDir, filename)
            if (file.exists()) {
                val fis = FileInputStream(file)
                val ois = ObjectInputStream(fis)
                result = ois.readObject()
                ois.close()
                fis.close()
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Fehler beim lesen des Objektes", e)
        }

        return result
    }
}