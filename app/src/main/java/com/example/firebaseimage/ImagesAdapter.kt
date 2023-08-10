package com.example.firebaseimage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseimage.databinding.ActivityImagesBinding
import com.example.firebaseimage.databinding.EachItemBinding
import com.squareup.picasso.Picasso

class ImagesAdapter(private var mList: List<String>,private val listener:setOnclick) :
    RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {


    inner class ImagesViewHolder(var binding: EachItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding = EachItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImagesViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return mList.size
    }
    interface setOnclick{
        fun onClick(path:String)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        with(holder.binding) {
            with(mList[position]) {
                Picasso.get().load(this).into(imageView)
            }
        }
        holder.binding.btndownload.setOnClickListener {
          listener.onClick(mList[position])
        }
    }
}