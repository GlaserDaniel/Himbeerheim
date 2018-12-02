package de.danielglaser.himbeerheim.view

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.os.Build
import de.danielglaser.himbeerheim.R
import de.danielglaser.himbeerheim.model.Util


/**
 * Created by Daniel on 29.07.2017.
 */
open class BaseActivity: AppCompatActivity() {

    companion object {
        lateinit var appContext: Context
    }

    private val THEME_LIGHT = 1
    private val THEME_BLACK = 2

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(null)
        appContext = this
        updateTheme()
    }

    private fun updateTheme() {
        if (Util.getTheme() <= THEME_LIGHT) {
            setTheme(R.style.AppTheme)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.statusBarColor = resources.getColor(R.color.darkBlue, theme)
                } else {
                    window.statusBarColor = resources.getColor(R.color.darkBlue)
                }
            }
        } else if (Util.getTheme() == THEME_BLACK) {
            setTheme(R.style.DarkAppTheme)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.statusBarColor = resources.getColor(R.color.black, theme)
                } else {
                    window.statusBarColor = resources.getColor(R.color.black)
                }
            }
        }
    }
}