package com.snapsoft.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.snapsoft.homework.model.DTOs.MovieDTO
import com.snapsoft.homework.utils.formatBudget
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.content_scrolling.*

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var movie: MovieDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        movie = intent.getSerializableExtra("movie") as MovieDTO

        Glide.with(this).load("https://image.tmdb.org/t/p/w500${movie.poster_path}").into(movie_detail_photo)

        val titleText = "Title: ${movie.title}"
        movie_detail_title.text = titleText


        movie_detail_budget.text = formatBudget(movie.budget)

        val ratingText = "Rating: ${movie.vote_average}"
        movie_detail_rating.text = ratingText
    }
}
