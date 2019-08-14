package com.bugDim88.arch_comp_mvi.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bugDim88.arch_comp_mvi.R
import com.bugDim88.arch_comp_mvi.view.counter_example.CounterActivity
import com.bugDim88.arch_comp_mvi.view.repo_search_example.RepoSearchActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_counter_example.setOnClickListener{
            startActivity(Intent(this, CounterActivity::class.java))
        }
        tv_search_repo_example.setOnClickListener{
            startActivity(Intent(this, RepoSearchActivity::class.java))
        }
    }
}
