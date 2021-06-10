package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListViewItemBinding
import com.udacity.asteroidradar.domain.ModelAsteroid

class AsteroidRecyclerAdapter :
        ListAdapter<ModelAsteroid, AsteroidRecyclerAdapter.AsteroidViewHolder>(AsteroidDiffCallBack) {

    var asteroids: List<ModelAsteroid> = emptyList()

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): AsteroidViewHolder {
        return  AsteroidViewHolder(ListViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount() = asteroids.size


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
        fun bind(asteroid: ModelAsteroid) {
            viewDataBinding.asteroid = asteroid
            viewDataBinding.executePendingBindings()
        }
    }

}