package com.example.cecytevlocationapp.data.model

data class LocationStudentResponse (
    var name : String = "",
    var matricula : String = "",
    var locations : MutableList<LocationStudentModel> = mutableListOf()
)