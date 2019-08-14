package com.bugDim88.arch_comp_mvi.view.counter_example

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CounterInteractor: ViewStateInteractor<CounterInteractor.ViewState, CounterInteractor.ViewIntent> {

    private val _viewState = MutableLiveData<ViewState>()


    override val viewState: LiveData<ViewState>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun onViewIntent(intent: ViewIntent) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    sealed class ViewIntent{
        /**
         * Increment counter intent
         */
        object IncrementCount:ViewIntent()
    }

    /**
     * @param counter - the actual state of counter inside view
     */
    data class ViewState(
        val counter: Int = 0
    )
}

/**
 * Interactor interface, more simple - more cool.
 */
interface ViewStateInteractor<S,in I>{
    val viewState: LiveData<S>
    fun onViewIntent(intent: I)
}