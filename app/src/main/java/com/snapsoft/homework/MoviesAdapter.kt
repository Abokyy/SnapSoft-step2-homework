package com.snapsoft.homework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snapsoft.homework.model.DTOs.MovieDTO
import com.snapsoft.homework.utils.formatBudget

class MoviesAdapter(val movies: List<MovieDTO>, val itemClickListener: OnViewItemSelectedListener): RecyclerView.Adapter<MoviesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MoviesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        return holder.bind(movies[position], itemClickListener)
    }

    interface OnViewItemSelectedListener {
        fun onViewItemSelected(movie: MovieDTO)
    }
}

class MoviesViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
    private val photo:ImageView = itemView.findViewById(R.id.movie_photo)
    private val title:TextView = itemView.findViewById(R.id.movie_title)
    private val budget:TextView = itemView.findViewById(R.id.movie_budget)

    fun bind(movie: MovieDTO, clickListener: MoviesAdapter.OnViewItemSelectedListener?) {
        Glide.with(itemView.context).load("https://image.tmdb.org/t/p/w500${movie.poster_path}").into(photo)
        title.text = movie.title
        budget.text = formatBudget(movie.budget)

        itemView.setOnClickListener {
            clickListener?.onViewItemSelected(movie)
        }

    }

}