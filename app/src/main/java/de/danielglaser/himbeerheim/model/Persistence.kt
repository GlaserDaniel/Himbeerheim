package de.danielglaser.himbeerheim.model

import android.content.ContentValues
import android.util.Log
import de.danielglaser.himbeerheim.view.BaseActivity
import java.io.*

/**
 * Created by Daniel on 18.07.2017.
 */
class Persistence {
    // this is the general method to serialize an object
    fun saveObject(`object`: Any, filename: String) {
        try {
            val file = File(BaseActivity.appContext.filesDir, filename)
            val fos = FileOutputStream(file)
            val os = ObjectOutputStream(fos)
            os.writeObject(`object`)
            os.close()
            fos.close()
        } catch (e: Exception) { //TODO Usually only IOException, but in the moment it can also be ConcurrentModificationException
            Log.e(ContentValues.TAG, "Error while saving the object: ", e)
        }
    }

    // this is the general method to serialize an object
    fun readObject(filename: String): Any? {
        var result: Any? = null
        try {
            val file = File(BaseActivity.appContext.filesDir, filename)
            if (file.exists()) {
                val fis = FileInputStream(file)
                val ois = ObjectInputStream(fis)
                result = ois.readObject()
                ois.close()
                fis.close()
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error while reading the object: ", e)
        }

        return result
    }
}