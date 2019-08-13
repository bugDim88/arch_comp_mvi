package com.bugDim88.arch_comp_mvi.domain.data

import com.google.gson.annotations.SerializedName


@Suppress("SpellCheckingInspection")
data class GitRepositoryAPI(
    val id: Int,
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    val description: String?,
    val language: String?,
    val url: String,
    val score: Float,
    val size: Int,
    val stars: Int
)


data class GitRepositoryUI (
        val id: Int,
        val name: String,
        val description: String?,
        val language: String?,
        val url: String
){
    companion object{
        fun fromGitRepApi(item: GitRepositoryAPI): GitRepositoryUI = GitRepositoryUI(
            id = item.id,
            name = item.fullName,
            description = item.description,
            language = item.language,
            url = item.url
        )
    }
}