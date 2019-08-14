package com.bugdim88.arch_comp_mvi_lib

import androidx.lifecycle.*

/**
 * The basic interface for binding View and ViewLogic stuff, where View it some
 * android UI container(Activity or Fragment) and ViewLogic is [ViewStateInteractor] implementation.
 * The client View observes and handles [viewState] and pass
 * all user interactions through view intents [I] to [ViewStateInteractor] by
 * [onViewIntent] method. By processing [onViewIntent] interactor push some updated ViewState to View
 * with [viewState] member.
 *
 * Example usage:
 *
 * ```
 * data class ViewState(
 *  val titles: List<String>
 * )
 *
 * sealed class ViewIntent{
 *  data class SelectTitle(val title: String): ViewIntent()
 * }
 *
 * class TitleActivity: AppCompatActivity{
 *  lateinit var viewStateInteractor: ViewStateInteractor<ViewState, ViewIntent>
 *  ...
 *  override fun onCreate(...){
 *      ...
 *      viewStateInteractor.viewState.observe(this, Observer{
 *          //handleStateFun()
 *      })
 *  }
 *  ...
 *  private fun onTitleClick(title: String){
 *      viewStateInteractor.onViewIntent(ViewIntent.SelectTitle(title))
 *  }
 * }
 * ```
 *
 * @param S  view state type.
 * @param I  view intent type.
 * @property viewState [LiveData] container of view state
 */
interface ViewStateInteractor<S, in I> {
    val viewState: LiveData<S>
    /**
     * Pass view [intent] to interactor
     */
    fun onViewIntent(intent: I)
}

/**
 * Function that reduce ViewState. Get source ViewState [S] and optional Params [P] and
 * return updated ViewState.
 * @param S - view state
 * @param P - some additional params that may need to be taken into account
 */
typealias Reducer <S, P> = (S, P) -> S

operator fun <S> Reducer<S, Unit>.invoke(state: S) = this(state, Unit)


/**
 * Basic [ViewStateInteractor] implementation.
 * Typical usage of implementation of this class will be through [ViewModel]
 * to handle all configuration changes and lifecycle stuff out of the box.
 * You can use [ViewStateInteractorVM] wrap up to make a [ViewModel] from
 * [ViewStateInteractor]. If you got some extreme use case with many fragments on the screen
 * and one 'orchestra' [ViewModel] use composition to bind all fragments interactors behaviour.
 * @param initialState - initial ViewState.
 */
abstract class ViewStateInteractorImpl<S, in I>(initialState: S) : ViewStateInteractor<S, I> {

    private val _viewStateMediator = MediatorLiveData<S>()
    private var _currentState: S? = initialState
        set(value) {
            val newValue = value?.let { resultStateReducer(value, Unit) } ?: return
            field = newValue
            _viewStateMediator.value = newValue
        }

    /**
     * Attach [Reducer] to long operation [result]. Assuming that long time consuming async operations will pass
     * their results through [LiveData] in thread save manner ([MutableLiveData.postValue]). So to set appropriate
     * viewState updates, [ViewStateInteractorImpl] observes [result] and invoke [reducer] on new [result] income.
     * @param result - LiveData with some long operation result
     * @param reducer - reducer for result to get ViewState updated
     */
    protected fun <R> attachUseCaseReducer(result: LiveData<R>, reducer: Reducer<S, R>) {
        _viewStateMediator.addSource(result) {
            val state = _currentState ?: return@addSource
            _currentState = reducer(state, it)
        }
    }

    /**
     * [Reducer] for viewIntent handling. Reduces [ViewStateInteractor.onViewIntent] argument intent.
     * @param I viewIntent
     */
    protected abstract val intentReducer: Reducer<S, I>
    /**
     * [Reducer] for post processing ViewState updating based on the current state. It is calling before actual
     * push updated ViewState to [viewState] LiveData, in this moment you can do some `old` and `new` view state
     * comparisons for example and make appropriate updates to ViewState.
     */
    protected abstract val resultStateReducer: Reducer<S, Unit>

    override val viewState: LiveData<S> = _viewStateMediator
    override fun onViewIntent(intent: I) {
        val state = _currentState ?: return
        _currentState = intentReducer(state, intent)
    }

}

/**
 * Simple delegation `wrap up` to [ViewStateInteractor] with [ViewModel] class.
 */
open class ViewStateInteractorVM<S, I>(reducerDelegate: ViewStateInteractor<S, I>) : ViewModel(),
    ViewStateInteractor<S, I> by reducerDelegate
