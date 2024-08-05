package com.example.cecytevlocationapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cecytevlocationapp.R
import com.example.cecytevlocationapp.data.model.ContextProvider
import com.example.cecytevlocationapp.data.model.LocationProvider
import com.example.cecytevlocationapp.data.model.LoginProvider
import com.example.cecytevlocationapp.databinding.ActivityShowMenuParentBinding
import com.example.cecytevlocationapp.ui.viewModel.StudentLocationViewModel
import com.example.cecytevlocationapp.utility.AlertMessage
import com.example.cecytevlocationapp.utility.ChildAdapter
import com.example.cecytevlocationapp.utility.Codes

class ShowMenuParent : AppCompatActivity() {
    lateinit var binding : ActivityShowMenuParentBinding
    val childrenListViewModel : StudentLocationViewModel by viewModels()
    val alertMessage  = AlertMessage()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityShowMenuParentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ContextProvider.context = this
        childrenListViewModel.telephone = LoginProvider.userCredentials.idUser
        childrenListViewModel.getChildrenList()//lamada a funcion recupera hijos del viewModel
        childrenListViewModel.getChildrenListViewModel.observe(this){//observer del viewModel
            validateCode()
        }

    }

    private fun validateCode() {//valida codigos de respuesta del servidor
      when(childrenListViewModel.httpCodegetChildrenList){
          Codes.CODE_SUCCESS -> {
              LoadRecyclerView()
              binding.loadinLayerShowMenuParent.visibility = View.GONE
          }
          Codes.NOT_AUTHORIZATION_TOKEN -> {
              alertMessage.showAlertDialog("Sesión terminada", "Inicie sesión nuevamente",this)
              showLoginView()
          }
          Codes.CODE_FAIL -> {
              alertMessage.showAlertDialog("Error de conexion","compuebe su conexion e intente mas tarde",this)
              showLoginView()
          }
      }
    }

    private fun LoadRecyclerView() {//confira un adpater para mosntrar la lista en e recyclerView de la vista
        if(!LocationProvider.childrenList.childrenListFiltered.isNullOrEmpty()){//verificando que la lista contenga algo
            val adapter = ChildAdapter()
            adapter.contextShowMenuParent = this
            binding.rvChildListShowMenuParent.layoutManager = LinearLayoutManager(this)
            binding.rvChildListShowMenuParent.adapter = adapter//asignando adapter al recyclerView
            for(item in LocationProvider.childrenList.childrenListFiltered){//pasa cada elmento de la lista al adapter
                adapter.setItem(item)
            }
        }

    }

    private fun showLoginView(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}