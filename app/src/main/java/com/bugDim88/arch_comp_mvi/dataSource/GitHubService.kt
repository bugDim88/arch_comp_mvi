package com.bugDim88.arch_comp_mvi.dataSource

import com.bugDim88.arch_comp_mvi.domain.data.GitRepositoryAPI
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {

    @GET("search/repositories?per_page=50")
    fun searchRepos(@Query("q") query: String): Call<ServiceResponse<GitRepositoryAPI>>

    data class ServiceResponse<R>(
        @SerializedName("total_count")
        val totalCount: Int,
        @SerializedName("incomplete_results")
        val incompleteResult: Boolean,
        val items: List<R>
    )
}

private val _client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.HEADERS })
    .build()

private val _retrofit = Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .client(_client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object GitHubServiceImpl: GitHubService by _retrofit.create(GitHubService::class.java)
