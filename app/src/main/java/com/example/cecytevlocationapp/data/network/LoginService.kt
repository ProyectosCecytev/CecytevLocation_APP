package com.example.cecytevlocationapp.data.network

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.cecytevlocationapp.core.RetrofitHelper
import com.example.cecytevlocationapp.data.model.ContextProvider
import com.example.cecytevlocationapp.data.model.LoginModel
import com.example.cecytevlocationapp.data.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginService() {
    private val retrofit = RetrofitHelper.getRetrofit()
    suspend fun login(login : LoginModel): Pair<Int, UserModel> {
        return withContext(Dispatchers.IO) {
            var code = 0
            var body: UserModel
            var  sharedPreferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)
            try {
                val response = retrofit.create(APIClient::class.java).login(login)
                code = response.code()
                body = response.body() ?: UserModel()
                var authToken = response.headers()["auth-token"]
                if(authToken != null){
                    sharedPreferences.edit().putString("auth-token", authToken).apply()
                }
                Pair(code, body)
            } catch (e: Exception) {
                code = 500
                Pair(code, UserModel())
            }

        }
    }
}