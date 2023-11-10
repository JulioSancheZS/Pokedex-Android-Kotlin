package com.example.pokedex

import com.google.gson.annotations.SerializedName

data class PokemonResponse (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("sprites") val sprites: SpriteResponse,
    @SerializedName("types") val types: List<TypesResponse>,
    @SerializedName("stats") val stats: List<StatsResponse>
)

data class SpriteResponse(
    @SerializedName("front_default") val front_default: String
)

data class TypeResponse(
    @SerializedName("name") val name: String
)

data class TypesResponse(
    @SerializedName("type") val type: TypeResponse
)

data class StatResponse(
    @SerializedName("name") val name: String
)

data class StatsResponse(
    @SerializedName("base_stat") val base_stat: Int,
    @SerializedName("stat") val stat: StatResponse
)