package com.bugDim88.arch_comp_mvi.view.counter_example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bugDim88.arch_comp_mvi.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_counter.*

class CounterActivity : AppCompatActivity() {

    private lateinit var counterVM: CounterVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        counterVM = ViewModelProviders.of(this).get(CounterVM::class.java)
        counterVM.viewState.observe(this, Observer{viewState->
            handleViewState(viewState)
        })

        fab.setOnClickListener {
            counterVM.onViewIntent(CounterInteractor.ViewIntent.IncrementCount)
        }
    }

    private fun handleViewState(viewState: CounterInteractor.ViewState?) {
        viewState?:return
        tv_counter.text = viewState.counter.toString()
    }

}
