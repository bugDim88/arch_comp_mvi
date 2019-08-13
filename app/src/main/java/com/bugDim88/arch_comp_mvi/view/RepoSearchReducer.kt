package com.bugDim88.arch_comp_mvi.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import com.bugDim88.arch_comp_mvi.domain.SearchGitRepositoriesUseCase
import com.bugDim88.arch_comp_mvi.domain.data.GitRepositoryUI
import com.bugdim88.arch_comp_mvi_lib.HandledData
import com.bugdim88.arch_comp_mvi_lib.Reducer
import com.bugdim88.arch_comp_mvi_lib.Result
import com.bugdim88.arch_comp_mvi_lib.ViewStateReducerImpl

class RepoSearchReducer(private val searchGitRepositoriesUseCase: SearchGitRepositoriesUseCase, private val initState: ViewState = ViewState()) :
    ViewStateReducerImpl<RepoSearchReducer.ViewState, RepoSearchReducer.ViewIntent>(initialState = initState) {

    private val _repoQuery = MutableLiveData<String>()
    private val _repoQueryResult = switchMap(_repoQuery) { searchGitRepositoriesUseCase(it) }
    private val _queryResultReducer: Reducer<ViewState, Result<List<GitRepositoryUI>>> = { state, result ->
        when (result) {
            is Result.Success -> state.copy(repositories = HandledData(result.data), loadEvent = HandledData(false))
            is Result.Error -> {
                result.exception.printStackTrace()
                state.copy(exception = HandledData(result.exception), loadEvent = HandledData(false))
            }
            else -> state.copy(loadEvent = HandledData(true))
        }
    }

    private val _refreshResult = MutableLiveData<Result<List<GitRepositoryUI>>>()
    private val _refreshResultReducer: Reducer<ViewState, Result<List<GitRepositoryUI>>> = { state, result ->
        when (result) {
            is Result.Success -> state.copy(repositories = HandledData(result.data), loadEvent = HandledData(false))
            is Result.Error -> {
                result.exception.printStackTrace()
                state.copy(exception = HandledData(result.exception), loadEvent = HandledData(false))
            }
            is Result.Loading -> state.copy(loadEvent = HandledData(true))
            else -> state
        }
    }

    override val intentReducer: Reducer<ViewState, ViewIntent> = { state, intent ->
        when (intent) {
            is ViewIntent.SearchRepo -> {
                _repoQuery.value = intent.query
                state
            }
            is ViewIntent.ConfigurationChange -> state.copy(
                repositories = HandledData(state.repositories.data),
                loadEvent = HandledData(state.loadEvent.data)
            )
            ViewIntent.InitIntent -> state
            ViewIntent.RefreshIntent -> onRefreshIntent(state)
        }
    }

    private fun onRefreshIntent(state: ViewState): ViewState {
        val query = _repoQuery.value
        if (query?.isNotEmpty() == true) {
            searchGitRepositoriesUseCase(query, _refreshResult)
        }
        return state
    }

    override val resultStateReducer: Reducer<ViewState, Unit> = { state, _ ->
        state
    }

    init {
        searchGitRepositoriesUseCase.checkSingleTask = false
        attachUseCaseReducer(_repoQueryResult, _queryResultReducer)
        attachUseCaseReducer(_refreshResult, _refreshResultReducer)
    }

    sealed class ViewIntent {
        data class SearchRepo(val query: String) : ViewIntent()
        object ConfigurationChange : ViewIntent()
        object InitIntent : ViewIntent()
        object RefreshIntent : ViewIntent()
    }

    data class ViewState(
        val exception: HandledData<Exception> = HandledData(null),
        val repositories: HandledData<List<GitRepositoryUI>> = HandledData(null),
        val loadEvent: HandledData<Boolean> = HandledData(null)
    )
}