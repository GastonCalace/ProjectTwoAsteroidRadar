package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListViewItemBinding
import com.udacity.asteroidradar.domain.ModelAsteroid

class AsteroidRecyclerAdapter(private val onClickListener: OnClickListener) :
        ListAdapter<ModelAsteroid, AsteroidRecyclerAdapter.AsteroidViewHolder>(AsteroidDiffCallBack) {

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val modelAsteroid = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(modelAsteroid)
        }
        holder.bind(modelAsteroid)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AsteroidViewHolder {
        return  AsteroidViewHolder(ListViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }


    companion object AsteroidDiffCallBack : DiffUtil.ItemCallback<ModelAsteroid>() {
        override fun areItemsTheSame(oldItem: ModelAsteroid, newItem: ModelAsteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ModelAsteroid, newItem: ModelAsteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class AsteroidViewHolder constructor(private var viewDataBinding: ListViewItemBinding):
            RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(modelAsteroid: ModelAsteroid) {
            viewDataBinding.apply{
                asteroid = modelAsteroid
                executePendingBindings()
            }
        }
    }

    class OnClickListener(val clickListener: (modelAsteroid: ModelAsteroid) -> Unit) {
        fun onClick(modelAsteroid:ModelAsteroid) = clickListener(modelAsteroid)
    }

}