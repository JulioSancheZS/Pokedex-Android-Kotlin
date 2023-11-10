package com.example.pokedex

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/api/v2/pokemon/{id}")
    suspend fun getPokemon(@Path("id") pokemonId:Int): Response<PokemonResponse>
}