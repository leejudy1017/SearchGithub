package com.judylee.kurlytest.presentation.root.view.adapter

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.judylee.kurlytest.R
import com.judylee.kurlytest.data.network.dto.GithubRepositoryResponse
import com.judylee.kurlytest.data.network.dto.SearchGithubResponse


class RepositoryAdapter(var context : Context, var searchText : String, var data : List<GithubRepositoryResponse>, var totalCount : Int, var page : Int) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }
    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.github_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context)
            .load(data[position].owner.avatarUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .fitCenter()
            .circleCrop()
            .into(holder.ivUser)

        holder.tvTitle.text = data[position].fullName
        holder.tvDescription.text = data[position].description
        holder.tvStarCount.text = data[position].stargazersCount.toString()

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it,position)
        }

    }

    override fun getItemCount(): Int {

        return data.size
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view : View = v
        var ivUser = v.findViewById<ImageView>(R.id.ivUser)
        var tvTitle = v.findViewById<TextView>(R.id.tvTitle)
        var tvDescription = v.findViewById<TextView>(R.id.tvDescription)
        var tvStarCount = v.findViewById<TextView>(R.id.tvStarCount)
    }


    fun containText(fullText: String, searchText: String) : Pair<Int,Int> {
        var firstIndex = 0
        var lastIndex = 0

        if(fullText.toLowerCase().contains(searchText.toLowerCase())){
            firstIndex = fullText.toLowerCase().split(searchText.toLowerCase())[0].length
            lastIndex = firstIndex + searchText.length - 1
        }

        return Pair(firstIndex,lastIndex)
    }

}