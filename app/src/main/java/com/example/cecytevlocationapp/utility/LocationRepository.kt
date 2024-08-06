package com.example.cecytevlocationapp.utility

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.example.cecytevlocationapp.core.RetrofitHelper
import com.example.cecytevlocationapp.data.model.ContextProvider
import com.example.cecytevlocationapp.data.model.LocationStudentModel
import com.example.cecytevlocationapp.data.network.APIClient

class LocationRepository {
    suspend fun sendLocation(newLocationStudent : LocationStudentModel){
        try {
            val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
            val retrofit = RetrofitHelper.getRetrofit()
            val apiService = retrofit.create(APIClient::class.java)
            var authToken = sharedPreferences.getString("auth-token","") ?: ""
            val response = apiService.registerLocationStudent("Bearer $authToken ",newLocationStudent)
            Log.d("BackgroundLocation","Entrando en Repository service")
            if (response.isSuccessful) {
                Log.d("LocationRepository", "Ubicación enviada correctamente")
            } else {
                Log.e("LocationRepository", "Error al enviar la ubicación: ${response.code()}")
            }
        }catch (e : Exception ){
            Log.d("LocationRepository", "Excepcoin")
        }

    }
}