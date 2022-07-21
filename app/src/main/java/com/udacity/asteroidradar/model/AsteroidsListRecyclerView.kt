package com.udacity.asteroidradar.model

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R

class AsteroidsListRecyclerView : RecyclerView.Adapter<AsteroidsListRecyclerView.ViewHolder>() {
    var asteroidList: List<Asteroid> = emptyList()
    var onClickListener: ((Asteroid) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setList(userList: List<Asteroid>) {
        this.asteroidList = userList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tv_asteroidName: TextView = itemView.findViewById(R.id.tv_asteroid_name)
        private var tv_asteroidCloseApproachDate: TextView =
            itemView.findViewById(R.id.tv_asteroid_close_approach_date)
        private var iv_hazardousStatus: ImageView = itemView.findViewById(R.id.iv_hazardous_status)
        fun bind(asteroid: Asteroid) {
            tv_asteroidName.text = asteroid.codename
            tv_asteroidCloseApproachDate.text = asteroid.closeApproachDate
            iv_hazardousStatus.setImageResource(if (asteroid.isPotentiallyHazardous) R.drawable.ic_status_potentially_hazardous else R.drawable.ic_status_normal)
            itemView.setOnClickListener {
                onClickListener?.invoke(asteroid)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.asteroids_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val asteroid: Asteroid = asteroidList[position]
        holder.bind(asteroid)
    }

    override fun getItemCount(): Int {
        return asteroidList.size
    }

}