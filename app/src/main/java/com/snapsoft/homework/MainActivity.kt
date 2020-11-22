package com.snapsoft.homework

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapsoft.homework.api.ServiceBuilder
import com.snapsoft.homework.api.TmdbEndpoints
import com.snapsoft.homework.model.*
import com.snapsoft.homework.model.DTOs.GuestSessionDTO
import com.snapsoft.homework.model.DTOs.MovieDTO
import com.snapsoft.homework.model.DTOs.RequestTokenDTO
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), MoviesAdapter.OnViewItemSelectedListener {

    val moviesToList = mutableListOf<MovieDTO>()
    private lateinit var requestToken: String
    val request = ServiceBuilder.buildService(TmdbEndpoints::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val popularMoviesCall = request.getPopularMovies(getString(R.string.api_key))

        initRecyclerView(popularMoviesCall)
        requestToken(request)
        generateGuestSession()

        loginFAB.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("request_token", requestToken)
            startActivity(intent)
        }

        movieSearchView.setOnEditorActionListener { _, actionId, _ ->
            searchMovies(actionId)
            true
        }

    }

    private fun initRecyclerView(call: Call<Movies>) {

        call.enqueue(object : Callback<Movies>{
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {

                if (response.isSuccessful){
                    queryBudgets(response.body()!!.results, request)
                }
            }
            override fun onFailure(call: Call<Movies>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

            mainRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = MoviesAdapter(moviesToList, this@MainActivity)
            }

    }

    private fun requestToken(request: TmdbEndpoints) {
        val requestTokenCall = request.generateRequestToken(getString(R.string.api_key))

        requestTokenCall.enqueue(object : Callback<RequestTokenDTO> {
            override fun onResponse(
                call: Call<RequestTokenDTO>,
                response: Response<RequestTokenDTO>
            ) {
                if (response.body()!!.success) {
                    requestToken = response.body()!!.request_token
                    Toast.makeText(this@MainActivity, "Request token: $requestToken", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<RequestTokenDTO>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun generateGuestSession() {
        val sessionCall = request.generateGuestSession(getString(R.string.api_key))

        sessionCall.enqueue(object : Callback<GuestSessionDTO> {
            override fun onResponse(call: Call<GuestSessionDTO>, response: Response<GuestSessionDTO>) {
                val guestSessionId = response.body()!!.guest_session_id
                Toast.makeText(this@MainActivity, "SessionID: $guestSessionId", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<GuestSessionDTO>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }



    private fun updateRecyclerView(movies: List<MovieDTO>) {
        progressBar.visibility = View.GONE
        moviesToList.clear()

        moviesToList.addAll(movies)

        mainRecyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun queryBudgets(movies: List<MovieDTO>, request: TmdbEndpoints) {
        val moviesWithBudgets = mutableListOf<MovieDTO>()


        movies.forEach { movie ->
            val movieCall = request.getMovie(movie.id, getString(R.string.api_key))


            movieCall.enqueue(object: Callback<MovieDTO> {
                override fun onResponse(call: Call<MovieDTO>, response: Response<MovieDTO>) {
                    moviesWithBudgets.add(response.body()!!)

                    if (moviesWithBudgets.size == movies.size) {
                        updateRecyclerView(moviesWithBudgets)
                    }
                }

                override fun onFailure(call: Call<MovieDTO>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }


    }

    private fun searchMovies(actionId: Int) {
        progressBar.visibility = View.VISIBLE
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val searchMovieCall = request.searchMovies(getString(R.string.api_key), movieSearchView.text.toString())

            searchMovieCall.enqueue(object : Callback<Movies> {
                override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                    if(response.body()!!.results.isNotEmpty()){
                        queryBudgets(response.body()!!.results, request)
                    }
                    else {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "No movies found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Movies>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }

            })

            movieSearchView.clearFocus()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(movieSearchView.windowToken, 0)
        }
    }

    override fun onViewItemSelected(movie: MovieDTO) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("movie", movie)
        startActivity(intent)
    }
}
