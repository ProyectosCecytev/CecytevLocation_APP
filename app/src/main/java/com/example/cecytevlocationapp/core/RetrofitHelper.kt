package com.example.cecytevlocationapp.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://8584-2806-10a6-9-2816-a88c-1ad-ea8f-8798.ngrok-free.app")//URL DE KNOR
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}