package de.danielglaser.himbeerheim.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity



/**
 * Created by Daniel on 01.08.2017.
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}