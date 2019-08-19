package com.bugdim88.arch_comp_mvi_testing.rules

import android.annotation.SuppressLint
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Force tasks in the Architecture Components to performed in the same thread
 */
class InstantTaskExecutorRule: TestWatcher() {
    @SuppressLint("RestrictedApi")
    override fun starting(description: Description?) {
        super.starting(description)
        ArchTaskExecutor.getInstance().setDelegate(object: TaskExecutor(){
            override fun executeOnDiskIO(runnable: Runnable) {runnable.run()}
            override fun isMainThread(): Boolean {return true}
            override fun postToMainThread(runnable: Runnable) {runnable.run()}
        })
    }

    @SuppressLint("RestrictedApi")
    override fun finished(description: Description?) {
        super.finished(description)
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}