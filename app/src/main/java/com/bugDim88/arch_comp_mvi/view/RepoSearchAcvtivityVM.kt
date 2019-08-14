package com.bugDim88.arch_comp_mvi.view

import com.bugDim88.arch_comp_mvi.domain.SearchGitRepositoriesUseCase
import com.bugdim88.arch_comp_mvi_lib.ViewStateInteractorVM

class RepoSearchAcvtivityVM(private val searchGitRepositoriesUseCase: SearchGitRepositoriesUseCase):
    ViewStateInteractorVM<RepoSearchReducer.ViewState, RepoSearchReducer.ViewIntent>(RepoSearchReducer(searchGitRepositoriesUseCase))
