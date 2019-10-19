package com.bugDim88.arch_comp_mvi.view.repo_search_example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bugDim88.arch_comp_mvi.R
import com.bugDim88.arch_comp_mvi.dataSource.GitHubServiceImpl
import com.bugDim88.arch_comp_mvi.domain.SearchGitRepositoriesUseCaseImpl
import com.bugDim88.arch_comp_mvi.domain.data.GitRepositoryUI
import com.bugDim88.arch_comp_mvi.view.repo_search_example.RepoSearchInteractor.*
import kotlinx.android.synthetic.main.repo_search_activity.*

class RepoSearchActivity : AppCompatActivity() {

    private lateinit var _refreshLayout: SwipeRefreshLayout
    private lateinit var _searchView: SearchView
    private lateinit var _recView: RecyclerView
    private lateinit var _gitRepAdapter: GitRepsAdapter
    private lateinit var _repoSearchReducer: RepoSearchAcvtivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.repo_search_activity)
        setToolbar()
        gitRepsInit()
        searchViewInit()
        initVM()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun searchViewInit() {
        _searchView = findViewById(R.id.rep_search)
        _searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query ?: return false
                _repoSearchReducer.onViewIntent(ViewIntent.SearchRepo(query))
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText ?: return false
                _repoSearchReducer.onViewIntent(ViewIntent.SearchRepo(newText))
                return true
            }

        })
    }

    @Suppress("UNCHECKED_CAST")
    private fun initVM() {
        _repoSearchReducer = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return RepoSearchAcvtivityVM(
                    SearchGitRepositoriesUseCaseImpl(GitHubServiceImpl)
                ) as T
            }
        })[RepoSearchAcvtivityVM::class.java]
        _repoSearchReducer.viewState.observe(this, Observer { handleState(it) })
    }

    private fun handleState(state: ViewState?) {
        state ?: return
        state.repositories.handle { _gitRepAdapter.submitList(it) }
        state.loadEvent.handle { _refreshLayout.isRefreshing = it?:false}
        state.exception.handle(::handleError)
    }

    private fun handleError(e: Exception?){
        e?:return
        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
    }
    
    private fun gitRepsInit() {
        _recView = findViewById(R.id.rep_list)
        _gitRepAdapter = GitRepsAdapter()
        _recView.adapter = _gitRepAdapter

        _refreshLayout = findViewById(R.id.swipe_layout)
        _refreshLayout.apply{
            setOnRefreshListener { _repoSearchReducer.onViewIntent(ViewIntent.RefreshIntent) }
        }
    }

}

class GitRepsAdapter : ListAdapter<GitRepositoryUI, GitRepViewHolder>(
    DiffCallBack
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitRepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_git_repo, parent, false)
        return GitRepViewHolder(view)
    }

    override fun onBindViewHolder(holder: GitRepViewHolder, position: Int) = holder.bind(getItem(position))

    object DiffCallBack : DiffUtil.ItemCallback<GitRepositoryUI>() {
        override fun areItemsTheSame(oldItem: GitRepositoryUI, newItem: GitRepositoryUI): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GitRepositoryUI, newItem: GitRepositoryUI): Boolean =
            oldItem == newItem

    }
}

class GitRepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val _tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
    private val _tvDescription = itemView.findViewById<TextView>(R.id.tv_description)
    private val _language = itemView.findViewById<TextView>(R.id.tv_lang)
    fun bind(item: GitRepositoryUI) {
        _tvTitle.text = item.name
        _tvDescription.text = item.description
        _language.text = item.language
    }
}
