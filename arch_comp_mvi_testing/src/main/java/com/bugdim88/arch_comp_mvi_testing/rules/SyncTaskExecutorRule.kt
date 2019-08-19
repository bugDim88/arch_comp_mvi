package com.bugdim88.arch_comp_mvi_testing.rules

import com.bugdim88.arch_comp_mvi_lib.DefaultScheduler
import com.bugdim88.arch_comp_mvi_lib.SyncScheduler
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Force [DefaultScheduler] clients to perform on [SyncScheduler]
 */
class SyncTaskExecutorRule: TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        DefaultScheduler.setDelegate(SyncScheduler)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        SyncScheduler.clearScheduledPostdelayedTasks()
        DefaultScheduler.setDelegate(null)
    }
}