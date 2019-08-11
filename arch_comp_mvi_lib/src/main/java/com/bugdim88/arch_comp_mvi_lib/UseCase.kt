package com.bugdim88.arch_comp_mvi_lib

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Executes business logic synchronously or asynchronously using a [Scheduler].
 * [checkSingleTask] - boolean flag, true by default,  that explicitly prevent or allow multiply invoking.
 * If [checkSingleTask] is true and [UseCase] are already running, than [UseCase.invoke]
 * do nothing, or if [checkSingleTask] is false in this situation [UseCase.invoke] start to
 * process new task.
 *
 */
abstract class UseCase<in P, R> {

    private val taskScheduler = DefaultScheduler
    //check if executeInProcess flag needs to by applied
    var checkSingleTask = true

    /** Executes the use case asynchronously and places the [Result] in a MutableLiveData
     *
     * @param parameters the input parameters to run the use case with
     * @param result the MutableLiveData where the result is posted to
     *
     */
    operator fun invoke(parameters: P, result: MutableLiveData<Result<R>>) {
        try {
            if(checkSingleTask) return
            result.postValue(Result.Loading)
            taskScheduler.execute {
                try {
                    execute(parameters).let { useCaseResult ->
                        result.postValue(Result.Success(useCaseResult))
                    }
                } catch (e: Exception) {
                    result.postValue(Result.Error(e))
                }
            }
        } catch (e: Exception) {
            result.postValue(Result.Error(e))
        }
    }

    /** Executes the use case asynchronously and returns a [Result] in a new LiveData object.
     *
     * @return an observable [LiveData] with a [Result].
     *
     * @param parameters the input parameters to run the use case with
     */
    operator fun invoke(parameters: P): LiveData<Result<R>> {
        val liveCallback: MutableLiveData<Result<R>> = MutableLiveData()
        this(parameters, liveCallback)
        return liveCallback
    }

    /** Executes the use case synchronously  */
    fun executeNow(parameters: P): Result<R> {
        return try {
            Result.Success(execute(parameters))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /** Executes the use case synchronously with exception out*/
    fun executeNowWithException(parameters: P):Result<R>{
        return Result.Success(execute(parameters))
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract fun execute(parameters: P): R

}

operator fun <R> UseCase<Unit, R>.invoke(): LiveData<Result<R>> = this(Unit)
operator fun <R> UseCase<Unit, R>.invoke(result: MutableLiveData<Result<R>>) = this(Unit, result)