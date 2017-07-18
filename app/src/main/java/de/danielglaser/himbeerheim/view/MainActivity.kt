package de.danielglaser.himbeerheim.view

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import de.danielglaser.himbeerheim.R

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var contextOfApplication: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contextOfApplication = applicationContext
        setContentView(R.layout.activity_main)
        //val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val manager = supportFragmentManager
        val newFragment = MainActivityFragment()

        val trans = manager.beginTransaction()
        trans.addToBackStack(EditCommandFragment::class.java!!.name)
        trans.replace(R.id.fragment_container, newFragment)
        trans.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    /*fun changeFragment(id: Int) {
        when (id) {
            R.id.fragment_main -> {
                val manager = supportFragmentManager
                val newFragment = MainActivityFragment()

                val trans = manager.beginTransaction()
                trans.addToBackStack(EditCommandFragment::class.java!!.name)
                trans.replace(R.id.fragment_container, newFragment)
                trans.commit()
            }
            R.id.fragnent_edit_command -> {
                val manager = supportFragmentManager
                val newFragment = EditCommandFragment()
                *//*
                val args = Bundle()
                args.putInt(ArticleFragment.ARG_POSITION, position)
                newFragment.setArguments(args)
                *//*

                val trans = manager.beginTransaction()
                trans.addToBackStack(EditCommandFragment::class.java!!.name)
                trans.replace(R.id.fragment_container, newFragment)
                trans.commit()
            }
        }
    }*/
}
