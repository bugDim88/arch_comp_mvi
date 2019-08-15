package com.bugDim88.arch_comp_mvi.view.counter_example

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CounterInteractor: ViewStateInteractor<CounterInteractor.ViewState, CounterInteractor.ViewIntent> {

    private val _viewState = MutableLiveData<ViewState>()

    private var _currentState: ViewState? = ViewState()
        set(value){
            value?:return
            field = value
            _viewState.value = value
        }

    override val viewState: LiveData<ViewState>
        get() = _viewState

    override fun onViewIntent(intent: ViewIntent) {
        _currentState = intentReducer(_currentState, intent)
    }

    private fun intentReducer(state: ViewState?, intent: ViewIntent):ViewState?{
       return when(intent){
            is ViewIntent.IncrementCount ->state?.copy(counter = state.counter + 1)
        }
    }

    init{
        _viewState.value = ViewState()
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

class CounterVM: ViewModel(), ViewStateInteractor<CounterInteractor.ViewState, CounterInteractor.ViewIntent> by
        CounterInteractor()

/**
 * Interactor interface, more simple - more cool.
 */
interface ViewStateInteractor<S,in I>{
    val viewState: LiveData<S>
    fun onViewIntent(intent: I)
}