package com.bugDim88.arch_comp_mvi.view.repo_search_example

import com.bugDim88.arch_comp_mvi.domain.SearchGitRepositoriesUseCase
import com.bugdim88.arch_comp_mvi_lib.ViewStateInteractorVM

class RepoSearchAcvtivityVM(private val searchGitRepositoriesUseCase: SearchGitRepositoriesUseCase):
    ViewStateInteractorVM<RepoSearchInteractor.ViewState, RepoSearchInteractor.ViewIntent>(
        RepoSearchInteractor(searchGitRepositoriesUseCase)
    )
