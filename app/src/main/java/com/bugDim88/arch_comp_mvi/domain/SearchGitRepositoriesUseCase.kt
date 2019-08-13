package com.bugDim88.arch_comp_mvi.domain

import com.bugDim88.arch_comp_mvi.dataSource.GitHubService
import com.bugDim88.arch_comp_mvi.domain.data.GitRepositoryUI
import com.bugdim88.arch_comp_mvi_lib.UseCase
import java.lang.Exception

abstract class SearchGitRepositoriesUseCase: UseCase<String, List<GitRepositoryUI>>()

class SearchGitRepositoriesUseCaseImpl(private val gitHubService: GitHubService): SearchGitRepositoriesUseCase(){
    override fun execute(parameters: String): List<GitRepositoryUI> {
        val response = gitHubService.searchRepos(parameters).execute()
        if(!response.isSuccessful)
            throw Exception(response.errorBody().toString())
        val result = response.body()?.items?.map { GitRepositoryUI.fromGitRepApi(it) }?: emptyList()
        return result
    }
}

class SearchGitRepositoriesUseCaseMock(val resultList: List<GitRepositoryUI> = emptyList()): SearchGitRepositoriesUseCase(){
    override fun execute(parameters: String): List<GitRepositoryUI> = resultList
}