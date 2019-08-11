package com.bugDim88.arch_comp_mvi.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import com.bugDim88.arch_comp_mvi.domain.SearchGitRepositoriesUseCase
import com.bugDim88.arch_comp_mvi.domain.data.GitRepositoryUI
import com.bugdim88.arch_comp_mvi_lib.HandledData
import com.bugdim88.arch_comp_mvi_lib.Reducer
import com.bugdim88.arch_comp_mvi_lib.Result
import com.bugdim88.arch_comp_mvi_lib.ViewStateReducerImpl

class RepoSearchReducer(private val searchGitRepositoriesUseCase: SearchGitRepositoriesUseCase):
    ViewStateReducerImpl<RepoSearchReducer.ViewState, RepoSearchReducer.ViewIntent>(initialState = ViewState()) {

    private val _repoQuery = MutableLiveData<String>()
    private val _repoQueryResult = switchMap(_repoQuery){searchGitRepositoriesUseCase(it)}
    private val _queryResultReducer: Reducer<ViewState, Result<List<GitRepositoryUI>>> = {state, result->
        when(result){
            is Result.Success->state.copy(repositories = HandledData(result.data))
            is Result.Error->state.copy(exception = HandledData(result.exception))
            else -> state
        }
    }

    override val intentReducer: Reducer<ViewState, ViewIntent> = {state, intent->
        when(intent) {
            is ViewIntent.SearchRepo->{
                _repoQuery.value = intent.query
                state
            }
            is ViewIntent.ConfigurationChange -> state.copy(
                repositories = HandledData(state.repositories.data)
            )
            ViewIntent.InitIntent -> TODO()
            ViewIntent.ReloadIntent -> TODO()
        }
    }
    override val resultStateReducer: Reducer<ViewState, Unit> = {state, _->state}

    init{
        searchGitRepositoriesUseCase.checkSingleTask = false
        attachUseCaseReducer(_repoQueryResult, _queryResultReducer)
    }

    sealed class ViewIntent{
        data class SearchRepo(val query: String): ViewIntent()
        object ConfigurationChange: ViewIntent()
        object InitIntent: ViewIntent()
        object ReloadIntent: ViewIntent()
    }

    data class ViewState(
        val exception: HandledData<Exception> = HandledData(null),
        val repositories: HandledData<List<GitRepositoryUI>> = HandledData(null)
    )
}