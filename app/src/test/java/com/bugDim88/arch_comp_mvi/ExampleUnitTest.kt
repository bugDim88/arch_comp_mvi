package com.bugDim88.arch_comp_mvi

import com.bugDim88.arch_comp_mvi.domain.SearchGitRepositoriesUseCaseMock
import com.bugDim88.arch_comp_mvi.view.repo_search_example.RepoSearchInteractor
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val repoSearchReducer =
            RepoSearchInteractor(SearchGitRepositoriesUseCaseMock())
        assertEquals(4, 2 + 2)
    }
}
