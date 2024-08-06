package com.example.cecytevlocationapp.core

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://672b-2806-10a6-9-2816-1461-dc40-faf2-49a.ngrok-free.app")//URL DE KNOR
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}