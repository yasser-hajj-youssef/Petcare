package com.example.petcare.data.remote

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


data class BreedDto(
    val id: String,
    val name: String,
    val temperament: String?,

    @SerializedName("life_span")
    val lifeSpan: String?,

    val origin: String?,
    val weight: WeightDto?,

    @SerializedName("reference_image_id")
    val referenceImageId: String?
)


data class WeightDto(
    val metric: String?
)


interface BreedApi {

    @GET("breeds")
    suspend fun getBreeds(): List<BreedDto>
}


fun createBreedApi(
    baseUrl: String,
    apiKey: String
): BreedApi {

    val client = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val request = chain.request()
                .newBuilder()
                .header("x-api-key", apiKey)
                .build()

            chain.proceed(request)
        }
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .build()
        .create(BreedApi::class.java)
}