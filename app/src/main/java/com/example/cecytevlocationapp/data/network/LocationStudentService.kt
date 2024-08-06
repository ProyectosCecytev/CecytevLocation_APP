package com.example.cecytevlocationapp.data.network

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.example.cecytevlocationapp.core.RetrofitHelper
import com.example.cecytevlocationapp.data.model.ChildInfo
import com.example.cecytevlocationapp.data.model.ChildrenListFiltered
import com.example.cecytevlocationapp.data.model.ContextProvider
import com.example.cecytevlocationapp.data.model.LocationStudentModel
import com.example.cecytevlocationapp.data.model.LocationStudentResponse
import com.example.cecytevlocationapp.data.model.LoginModel
import com.example.cecytevlocationapp.data.model.TelephoneParent
import com.example.cecytevlocationapp.data.model.TutorCredencialsLocation
import com.example.cecytevlocationapp.data.model.UserModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationStudentService {
    //instancia para la conexion al api
    private val retrofit = RetrofitHelper.getRetrofit()
    //instancia para recuperar el token almacenado en sahred preferences
    private val sharedPreferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(ContextProvider.context)

    suspend fun registerStudentLocation(studentLocation: LocationStudentModel): Int {
        return withContext(Dispatchers.IO) {
            var code = 0
            var authToken = sharedPreferences.getString("auth-token","") ?: ""
            try {
                val response =
                    retrofit.create(APIClient::class.java).registerLocationStudent("Bearer $authToken",studentLocation)
                code = response.code()
                code
            } catch (e: Exception) {
                code = 500
                code
            }
        }
    }

    suspend fun getStudentLocation(matricula: String): Pair<Int, LocationStudentResponse> {
        return withContext(Dispatchers.IO) {
            var code = 0
            var locationStudent: LocationStudentResponse = LocationStudentResponse()
            var authToken = sharedPreferences.getString("auth-token","") ?: ""
            try {
                val response =
                    retrofit.create(APIClient::class.java).getLocationStudent("Bearer $authToken",matricula)
                code = response.code()
                locationStudent = response.body() ?: LocationStudentResponse()
                Pair(code, locationStudent)
            } catch (e: Exception) {
                code = 500
                Pair(code, locationStudent)
            }
        }
    }

    suspend fun getChildrenList(telephone: String): Pair<Int, ChildrenListFiltered> {
        return withContext(Dispatchers.IO) {
            var code = 0
            var childrenListFiltered: ChildrenListFiltered = ChildrenListFiltered()
            var telephoneParent : TelephoneParent = TelephoneParent(telephone)
            var authToken = sharedPreferences.getString("auth-token","") ?: ""
            try {
                val response =
                    retrofit.create(APIClient::class.java).getChildrenList("Bearer $authToken",telephoneParent)
                code = response.code()
                childrenListFiltered = response.body() ?: ChildrenListFiltered()
                Pair(code, childrenListFiltered)
            } catch (e: Exception) {
                code = 500
                Pair(code, childrenListFiltered)
            }
        }
    }
}