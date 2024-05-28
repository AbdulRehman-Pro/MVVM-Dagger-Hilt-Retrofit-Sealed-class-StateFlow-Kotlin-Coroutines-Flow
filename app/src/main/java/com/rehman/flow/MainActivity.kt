package com.rehman.flow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rehman.flow.databinding.ActivityMainBinding
import com.rehman.flow.databinding.ItemRowBinding
import com.rehman.flow.models.PostModel
import com.rehman.flow.network.Resource
import com.rehman.flow.utils.AppConstants
import com.rehman.flow.utils.ProjectUtils.stringToObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getPosts()



        lifecycleScope.launch {
            viewModel.apiStateFlow.collect {
                when (it) {
                    is Resource.Loading -> {
                        when (it.apiType) {
                            AppConstants.ApiTypes.GetPosts.name -> {
                                binding.apply {
                                    recyclerview.visibility = View.GONE
                                    progress.visibility = View.VISIBLE
                                }
                            }
                        }
                    }

                    is Resource.Success -> {
                        when (it.apiType) {
                            AppConstants.ApiTypes.GetPosts.name -> {
                                binding.apply {
                                    recyclerview.visibility = View.VISIBLE
                                    progress.visibility = View.GONE
                                }


                                it.data?.let { response ->

                                    initRecycler(response.string().stringToObject<ArrayList<PostModel>>())

                                    Log.d("RESPONSE", "Data ${it.data.string()}")
                                }


                            }

                        }


                    }

                    is Resource.Error -> {
                        when (it.apiType) {
                            AppConstants.ApiTypes.GetPosts.name -> {
                                binding.apply {
                                    recyclerview.visibility = View.GONE
                                    progress.visibility = View.VISIBLE
                                    Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }

                    }

                    else -> {}
                }
            }
        }

    }

    private fun initRecycler(list: ArrayList<PostModel>) {
        binding.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = PostAdapter(list)
        }
    }

    inner class PostAdapter(private val list: ArrayList<PostModel>) :
        RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemRowBinding.inflate(layoutInflater, parent, false)
            return PostViewHolder(itemBinding)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            val item = list[position]
            holder.bind(item)
        }

        inner class PostViewHolder(private val binding: ItemRowBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(item: PostModel) {
                // Bind your data here using the binding instance, e.g.,
                binding.descText.text = item.body
            }
        }

    }


}