package com.bugDim88.arch_comp_mvi.view.counter_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bugDim88.arch_comp_mvi.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_counter.*

class CounterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

}
