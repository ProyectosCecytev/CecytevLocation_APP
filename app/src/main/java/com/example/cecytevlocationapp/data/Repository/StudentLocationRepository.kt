package com.example.cecytevlocationapp.data.Repository

import android.util.Log
import com.example.cecytevlocationapp.data.model.AttendanceModel
import com.example.cecytevlocationapp.data.model.AttendanceProvider
import com.example.cecytevlocationapp.data.model.LocationProvider
import com.example.cecytevlocationapp.data.model.LocationStudentModel
import com.example.cecytevlocationapp.data.model.TutorCredencialsLocation
import com.example.cecytevlocationapp.data.network.AttendanceService
import com.example.cecytevlocationapp.data.network.LocationStudentService
import com.example.cecytevlocationapp.utility.LocationService

class StudentLocationRepository {
    private val api = LocationStudentService()

    suspend fun registerStudentLocation(newLocation : LocationStudentModel) : Int{
        var response = api.registerStudentLocation(newLocation)
        return response
    }

    suspend fun getStudentLocation(idStudent : String) : Int{
        var response = api.getStudentLocation(idStudent)
        LocationProvider.locationStudent = response.second
        return response.first
    }

    suspend fun getChildrenList(telephone : String) : Int{
        Log.d("testService","en repositorio 1")
        var response = api.getChildrenList(telephone)
        LocationProvider.childrenList = response.second
        Log.d("testService","return del repository" + response.second.childrenListFiltered.toString())
        return response.first
    }
}