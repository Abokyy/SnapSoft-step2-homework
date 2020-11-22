package com.snapsoft.homework.api

import com.snapsoft.homework.model.*
import com.snapsoft.homework.model.DTOs.GuestSessionDTO
import com.snapsoft.homework.model.DTOs.MovieDTO
import com.snapsoft.homework.model.DTOs.RequestTokenDTO
import com.snapsoft.homework.model.DTOs.SessionDTO
import retrofit2.Call
import retrofit2.http.*

interface TmdbEndpoints {

    @GET("/3/movie/popular")
    fun getPopularMovies(@Query("api_key") key: String): Call<Movies>

    @GET("/3/movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId : Int, @Query("api_key") key: String): Call<MovieDTO>

    @GET("/3/search/movie")
    fun searchMovies(@Query("api_key") key: String, @Query("query") query: String): Call<Movies>

    @GET("/3/authentication/token/new")
    fun generateRequestToken(@Query("api_key") key: String): Call<RequestTokenDTO>

    @GET("/3/authentication/guest_session/new")
    fun generateGuestSession(@Query("api_key") key: String) : Call<GuestSessionDTO>

    @POST("/3/authentication/session/new")
    fun generateValidatedSession(@Body requestToken: SessionRequestBody, @Query("api_key") key: String): Call<SessionDTO>

    @POST("/3/authentication/token/validate_with_login")
    fun validateWithLogin(@Body userData: UserValidator,
                          @Query("api_key") key: String) : Call<RequestTokenDTO>
}