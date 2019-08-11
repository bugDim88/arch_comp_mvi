package com.bugdim88.arch_comp_mvi_lib

import androidx.lifecycle.*

/**
 * [S] - view state type.
 * [I] - view intent type.
 */
interface ViewStateReducer<S, in I> {
    val viewState: LiveData<S>
    fun onViewIntent(intent: I)
}

typealias Reducer <S, P> = (S, P) -> S

operator fun <S> Reducer<S, Unit>.invoke(state: S) = this(state, Unit)

abstract class ViewStateReducerImpl<S, in I>(private val initialState: S) : ViewStateReducer<S, I> {

    private val _viewStateMediator = MediatorLiveData<S>()

    private var _currentState: S?
        get() = _viewStateMediator.value
        set(value) {
            value
                ?.let { resultStateReducer(it) }
                ?.let { _viewStateMediator.value = it }
        }

    private val _viewIntent = MutableLiveData<I>()

    protected fun <R> attachUseCaseReducer(result: LiveData<R>, reducer: Reducer<S, R>) {
        _viewStateMediator.addSource(result) {
            val state = _currentState ?: return@addSource
            _currentState = reducer(state, it)
        }
    }

    protected abstract val intentReducer: Reducer<S, I>
    protected abstract val resultStateReducer: Reducer<S, Unit>

    override val viewState: LiveData<S> = _viewStateMediator
    override fun onViewIntent(intent: I) = _viewIntent.postValue(intent)

    init {
        _currentState = initialState
        _viewStateMediator.apply {
            addSource(_viewIntent) { viewIntent ->
                val state = _currentState ?: return@addSource
                _currentState = intentReducer(state, viewIntent)
            }
        }
    }
}

open class ViewStateReducerVM<S, I>(reducerDelegate: ViewStateReducer<S, I>) : ViewModel(), ViewStateReducer<S, I> by reducerDelegate
