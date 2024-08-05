package com.example.cecytevlocationapp.data.network

import com.example.cecytevlocationapp.data.model.AttendanceModel
import com.example.cecytevlocationapp.data.model.AttendanceStudentResponse
import com.example.cecytevlocationapp.data.model.ChildInfo
import com.example.cecytevlocationapp.data.model.ChildrenListFiltered
import com.example.cecytevlocationapp.data.model.LocationStudentModel
import com.example.cecytevlocationapp.data.model.LocationStudentResponse
import com.example.cecytevlocationapp.data.model.LoginModel
import com.example.cecytevlocationapp.data.model.TelephoneParent
import com.example.cecytevlocationapp.data.model.TutorCredencialsLocation
import com.example.cecytevlocationapp.data.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface APIClient {
    @POST("api/v1/login")
    suspend fun login(@Body credentials : LoginModel): Response<UserModel>

    @POST("api/v1/registerattendance")
    suspend fun registerAttendance(@Body attendance : AttendanceModel): Response<AttendanceStudentResponse>

    @POST("api/v1/location/registerLocation")
    suspend fun registerLocationStudent(@Body locationStudent : LocationStudentModel): Response<Void>

    @GET("api/v1/location/getLocationStudent/{matricula}")
    suspend fun getLocationStudent(@Path("matricula") matricula: String): Response<LocationStudentResponse>

    @POST("api/v1/location/getChildrenList")
    suspend fun getChildrenList(
        @Header ("Authorization") authToken : String,
        @Body telephoneParent : TelephoneParent
    ): Response<ChildrenListFiltered>
}