package com.example.cecytevlocationapp.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras

import com.example.cecytevlocationapp.data.model.LocationStudentModel
import com.example.cecytevlocationapp.data.model.TutorCredencialsLocation
import com.example.cecytevlocationapp.domain.GetChildrenListUseCase
import com.example.cecytevlocationapp.domain.GetStudentLocationUseCase
import com.example.cecytevlocationapp.domain.registerAttendanceUseCase
import com.example.cecytevlocationapp.domain.registerStudentLocationUseCase
import kotlinx.coroutines.launch

class StudentLocationViewModel :ViewModel(){
    var httpCodeRegisterStudentLocation: Int = 400
    lateinit var newStudentLocation: LocationStudentModel
    lateinit var  registerStudentLocationUseCase: registerStudentLocationUseCase
    val studentLocationViewModel = MutableLiveData<Int>()

    fun registerNewStudentLocation() {
        viewModelScope.launch {
            registerStudentLocationUseCase = registerStudentLocationUseCase()
            val result = registerStudentLocationUseCase(newStudentLocation) // Llama al caso de uso
            studentLocationViewModel.postValue(result)
            httpCodeRegisterStudentLocation = result
        }
    }

    var httpCodegetStudentLocation: Int = 400
    lateinit var idStudent : String //parametro que recibe de la  activity
    lateinit var  getStudentLocationUseCase: GetStudentLocationUseCase
    val getstudentLocationViewModel = MutableLiveData<Int>() //ViewModel para ver cambios en activity
    fun getNewStudentLocation() {
        viewModelScope.launch { //uso de corutina para llamar a funciones suspend(UseCase)
            getStudentLocationUseCase = GetStudentLocationUseCase()//instancia de UseCase
            val result = getStudentLocationUseCase(idStudent) //activar funcion
            getstudentLocationViewModel.postValue(result)//guardando
            httpCodegetStudentLocation = result
        }
    }

    var httpCodegetChildrenList: Int = 400
    lateinit var telephone: String
    lateinit var  getChildrenLocationUseCase: GetChildrenListUseCase
    val getChildrenListViewModel = MutableLiveData<Int>()

    fun getChildrenList() {
        viewModelScope.launch {
            getChildrenLocationUseCase = GetChildrenListUseCase()
            val result = getChildrenLocationUseCase(telephone) // Llama al caso de uso
            getChildrenListViewModel.postValue(result)
            httpCodegetChildrenList = result
        }
    }
}