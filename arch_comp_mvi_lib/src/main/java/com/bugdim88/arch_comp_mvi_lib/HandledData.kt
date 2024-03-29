package com.bugdim88.arch_comp_mvi_lib

/**
 * Container for the data that must to be handled only once.
 *
 * Example usage:
 *
 * ```
 * ...
 * val simpleFlag = HandleData<Boolean>(false)
 * ...
 * simleFlag.handle{flag-> ... }// this call do some stuff with flag
 * simleFlag.handle{flag-> ... }// this call do nothing
 * ..
 */
class HandledData<out T>( val data: T?){
    var hasBeenHandled = false
        private set

    fun handle(action: (T?) -> Unit){
        if(!hasBeenHandled){
            hasBeenHandled = true
            action(data)
        }
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HandledData<*>

        return (data == other.data)
    }
}