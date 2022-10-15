package com.judylee.kurlytest.presentation.root.view.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.judylee.kurlytest.R
import com.judylee.kurlytest.data.network.dto.SearchGithubResponse


class RepositoryAdapter(var context : Context, var searchText : String, var data : SearchGithubResponse) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

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
            .load(data.items[position].owner.avatarUrl)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .fitCenter()
            .circleCrop()
            .into(holder.ivUser)


        //searchText 와 동일한 부분 Bold 처리
        if(containText(data.items[position].fullName,searchText) == Pair(0,0)){
            holder.tvTitle.text = data.items[position].fullName
        }
        else{
            var firstIndex = containText(data.items[position].fullName,searchText).first
            var lastIndex = containText(data.items[position].fullName,searchText).second
            val substr = data.items[position].fullName.substring(firstIndex, lastIndex+1)
            val sourceString = data.items[position].fullName.split(substr)[0]+"<b>" + substr + "</b> " + data.items[position].fullName.split(substr)[1]
            holder.tvTitle.text = Html.fromHtml(sourceString)
        }


        if(data.items[position].description == null || data.items[position].description == ""){
            holder.tvDescription.visibility = View.GONE
        }
        else{
            holder.tvDescription.visibility = View.VISIBLE
            if(containText(data.items[position].description,searchText) == Pair(0,0)){
                holder.tvDescription.text = data.items[position].description
            }
            else{
                var firstIndex = containText(data.items[position].description,searchText).first
                var lastIndex = containText(data.items[position].description,searchText).second
                val substr = data.items[position].description.substring(firstIndex, lastIndex+1)
                val sourceString = data.items[position].description.split(substr)[0]+"<b>" + substr + "</b> " + data.items[position].description.split(substr)[1]
                holder.tvDescription.text = Html.fromHtml(sourceString)
            }
        }

        holder.tvStarCount.text = data.items[position].stargazersCount.toString()

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it,position)
        }

    }

    override fun getItemCount(): Int {
       return data.totalCount
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