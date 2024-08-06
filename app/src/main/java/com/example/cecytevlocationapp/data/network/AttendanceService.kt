package com.example.cecytevlocationapp.data.network

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.cecytevlocationapp.core.RetrofitHelper
import com.example.cecytevlocationapp.data.model.AttendanceModel
import com.example.cecytevlocationapp.data.model.AttendanceStudentResponse
import com.example.cecytevlocationapp.data.model.ContextProvider
import com.example.cecytevlocationapp.data.model.LoginModel
import com.example.cecytevlocationapp.data.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AttendanceService {
    private val retrofit = RetrofitHelper.getRetrofit()
    private val sharedPreferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(
        ContextProvider.context)

    suspend fun registerattendance(attendance : AttendanceModel): Pair<Int,AttendanceStudentResponse> {
        return withContext(Dispatchers.IO) {
            var code = 0
            var body = AttendanceStudentResponse()
            var authToken = sharedPreferences.getString("auth-token","") ?: ""
            try {
                val response = retrofit.create(APIClient::class.java).registerAttendance("Bearer $authToken",attendance)
                code = response.code()
                body = response.body() ?: AttendanceStudentResponse()
                Pair(code,body)
            } catch (e: Exception) {
                code = 500
                Pair(code,body)
            }

        }
    }
}