package com.example.cecytevlocationapp.ui.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cecytevlocationapp.data.model.ContextProvider
import com.example.cecytevlocationapp.data.model.LoginModel
import com.example.cecytevlocationapp.data.model.LoginProvider
import com.example.cecytevlocationapp.databinding.ActivityMainBinding
import com.example.cecytevlocationapp.ui.viewModel.LoginViewModel
import com.example.cecytevlocationapp.utility.AlertMessage
import com.example.cecytevlocationapp.utility.BackgroundLocationService
import com.example.cecytevlocationapp.utility.Codes
import com.example.cecytevlocationapp.utility.JobPosition
import com.example.cecytevlocationapp.utility.LocationService

class MainActivity : AppCompatActivity() {
    // Esta propiedad se utiliza para acceder a los elementos de la interfaz de usuario.
    lateinit var binding: ActivityMainBinding
    // Este es el ViewModel para manejar la lógica de inicio de sesión.
    val loginViewModel: LoginViewModel by viewModels()
    // Esta propiedad se utiliza para almacenar datos persistentes.
    private lateinit var sharedPreferences: SharedPreferences
    // Servicio para manejar la localización en segundo plano.
    private val location = BackgroundLocationService()
    // Utilidad para mostrar mensajes de alerta.
    private val alertDialog = AlertMessage()

    // Método que se ejecuta cuando la actividad se crea.
    override fun onCreate(savedInstanceState: Bundle?) {
        // Configura la vista con el binding.
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Establece los listeners para los botones.
        setListener()
        // Observa los cambios en el ViewModel.
        observeViewModel()
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        // Valida los permisos de ubicación.
        validatePermissionLocation()
        //se usa para poder usar los shared preference en los archivos service(LoginService)
        ContextProvider.context = this
    }

    // Método para validar los campos de entrada del usuario.
    private fun validateInputs(): Boolean {
        if(binding.etUser.text.isBlank() || binding.etPassword.text.isBlank()){
            return false
        }
        return true
    }

    // Método para establecer los listeners de los botones.
    private fun setListener() {
        // Listener para el botón de inicio de sesión.
        binding.btnLogin.setOnClickListener {
            if (validateInputs()) {
                login()
            }else{
                alertDialog.showAlertDialog("Error","ALguno de los campos se encuentra vacio. Favor de verificar",this)
            }
        }

        // Listener para el botón de salida de la aplicacion.
        binding.btnExitLogin.setOnClickListener{
            finish()
        }
    }

    // Método para manejar el inicio de sesión.
    private fun login() {
        // Crea un modelo de login con los datos ingresados por el usuario.
        val login = LoginModel(
            user = binding.etUser.text.toString(),
            passwordUser= binding.etPassword.text.toString()
        )
        // Asigna el modelo de login al ViewModel.
        loginViewModel.loginModel = login
        // Llama al método de login del ViewModel.
        loginViewModel.login()
        // Muestra una capa de carga y oculta el botón de login.
        binding.loadinLayer.visibility = View.VISIBLE
        binding.btnLogin.visibility = View.GONE
    }

    // Método para observar los cambios en el ViewModel.
    private fun observeViewModel() {
        loginViewModel.loginViewModel.observe(this) { result ->
            // Oculta la capa de carga y muestra el botón de login.
            binding.loadinLayer.visibility = View.GONE
            binding.btnLogin.visibility = View.VISIBLE
            // Maneja los diferentes códigos de resultado del login.
            when (result) {
                Codes.CODE_SUCCESS -> handleLoginSuccess()
                Codes.CODE_NOT_FOUND -> showAlertDialog("Usuario no encontrado", "Matrícula o contraseña incorrecta. Favor de intentar de nuevo.")
                Codes.CODE_FAIL -> showAlertDialog("Error", "Error de conexión. Favor de revisar su conexión o intente más tarde.")
                Codes.CODE_BAD_REQUEST -> showAlertDialog("Error", "Caracteres invalidos")
                else -> showAlertDialog("Error", "Ha ocurrido un error inesperado.")
            }
        }
    }

    // Método para manejar un login exitoso.
    private fun handleLoginSuccess() {
        // Dependiendo del tipo de usuario, muestra el menú correspondiente y empieza el servicio de localización.
        when(LoginProvider.userCredentials.type){
            JobPosition.STUDENT -> {
                showStudentMenu()
                startBackgroundLocationService()
            }
            JobPosition.TEACHER -> showTeacherMenu()
            JobPosition.PARENT -> ShowParentMenu()
        }
    }

    // Constante para el código de solicitud de permiso de ubicación.
       private val PERMISSION_REQUEST_LOCATION = 1

    // Método para validar el permiso de ubicación.
       private fun validatePermissionLocation() {
           if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               ActivityCompat.requestPermissions(
                   this,
                   arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                   PERMISSION_REQUEST_LOCATION
               )
           } else {
               Toast.makeText(this, "Permiso de ubicación recuperado" , Toast.LENGTH_SHORT).show()
           }
       }

    // Método para iniciar el servicio de localización en segundo plano.
       private fun startBackgroundLocationService() {
           val serviceIntent = Intent(this, BackgroundLocationService()::class.java)
          startService(serviceIntent)
       }

    // Método que se llama cuando se aceptan los permisos.
       override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
           super.onRequestPermissionsResult(requestCode, permissions, grantResults)
           when (requestCode) {
               PERMISSION_REQUEST_LOCATION -> {
                   if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                       Toast.makeText(this, "EL PERMISO FUE ACEPTADO",Toast.LENGTH_SHORT).show()
                       // Permiso concedido, puedes acceder a la ubicación
                        val serviceIntent = Intent(this, BackgroundLocationService::class.java)
                        startService(serviceIntent)
                   } else {
                       // Permiso denegado, no puedes acceder a la ubicación
                       finish()
                   }
                   return
               }
           }
       }

    // Método para mostrar el menú del estudiante.
    private fun showStudentMenu() {
        saveToSharedPreferences()
        val intent = Intent(this, MenuStudent::class.java)
        startActivity(intent)
        finish()
    }

    // Método para mostrar el menú del padre.
    fun ShowParentMenu() {
        val intent = Intent(this,ShowMenuParent::class.java)
        startActivity(intent)
        finish()
    }

    // Método para mostrar el menú del profesor.
    private fun showTeacherMenu() {
        val intent = Intent(this, MenuTeacher::class.java)
        startActivity(intent)
        finish()
    }
    // Método para iniciar el servicio de localización.
    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationService::class.java)
        //LocationService.enqueueWork(this, serviceIntent)
        Toast.makeText(this, "Servicio de ubicación iniciado", Toast.LENGTH_SHORT).show()
    }

    // Método para guardar datos en SharedPreferences(el id de usuario para guardar la lozalizacion en segundo plano).
    private fun saveToSharedPreferences() {
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("idStudent", LoginProvider.userCredentials.idUser)
        editor.apply()

        var idSudentSP = sharedPreferences.getString("idStudent","")
        Toast.makeText(this,"shared: " + idSudentSP, Toast.LENGTH_SHORT).show()
    }

    //Metodo para mostrar un mensaje en pantalla(las demas clases usan la clase AlerMessage en Utility)
    private fun showAlertDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("ACEPTAR") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}


