package com.bugDim88.arch_comp_mvi

import com.bugDim88.arch_comp_mvi.view.counter_example.CounterInteractor
import com.bugdim88.arch_comp_mvi_testing.getObservedValue
import com.bugdim88.arch_comp_mvi_testing.rules.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Tests for [CounterInteractor].
 */
class CounterInteractorTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun addition_isCorrect() {
        val interactor = CounterInteractor()
        interactor.onViewIntent(CounterInteractor.ViewIntent.IncrementCount)
        val counter = interactor.viewState.getObservedValue()?.counter
        assertEquals(1, counter)
    }
}
