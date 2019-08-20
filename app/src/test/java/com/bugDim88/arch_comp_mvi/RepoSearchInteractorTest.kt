package com.bugDim88.arch_comp_mvi

import com.bugDim88.arch_comp_mvi.domain.SearchGitRepositoriesUseCase
import com.bugDim88.arch_comp_mvi.domain.data.GitRepositoryUI
import com.bugDim88.arch_comp_mvi.view.repo_search_example.RepoSearchInteractor
import com.bugdim88.arch_comp_mvi_testing.getObservedValue
import com.bugdim88.arch_comp_mvi_testing.rules.InstantTaskExecutorRule
import com.bugdim88.arch_comp_mvi_testing.rules.SyncTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

/**
 * Tests for [RepoSearchInteractor].
 */
class RepoSearchInteractorTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var syncTaskExecutorRule = SyncTaskExecutorRule()

    @Test
    fun searching_repos() {
        val queryString = "android repo"
        val repoList = listOf(
            GitRepositoryUI(1, "first",  null, null, "http://"),
            GitRepositoryUI(2, "second",  null, null, "http://")
        )
        val searchRepouseCaseMock = object: SearchGitRepositoriesUseCase(){
            override fun execute(parameters: String): List<GitRepositoryUI> = repoList
        }
        val interactor = RepoSearchInteractor(searchRepouseCaseMock)
        interactor.onViewIntent(RepoSearchInteractor.ViewIntent.SearchRepo(queryString))
        val searchedValues = interactor.viewState.getObservedValue()?.repositories?.data
        assertEquals(repoList, searchedValues)
    }
}
