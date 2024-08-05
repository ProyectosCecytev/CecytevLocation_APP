package com.example.cecytevlocationapp.ui.view

import android.R
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter

import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.cecytevlocationapp.data.model.LocationProvider
import com.example.cecytevlocationapp.data.model.TutorCredencialsLocation
import com.example.cecytevlocationapp.databinding.ActivityShowMapBinding
import com.example.cecytevlocationapp.domain.GetStudentLocationUseCase
import com.example.cecytevlocationapp.ui.viewModel.StudentLocationViewModel
import com.example.cecytevlocationapp.utility.AlertMessage
import com.example.cecytevlocationapp.utility.Codes

class ShowMap : AppCompatActivity() {
    lateinit var binding: ActivityShowMapBinding
    val locationStudentViewModel : StudentLocationViewModel by viewModels()
    val alertMessage = AlertMessage()
    var positionSpinner = 0//guardar la posicion seleccionada en el spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var idStudent = intent.getStringExtra("idStudent") ?: ""// extraer valor mandado desde la actividad que lo instanció
        locationStudentViewModel.idStudent = idStudent//pasando valor a variable en el viewModel
        locationStudentViewModel.getNewStudentLocation()//activando funcion en el viewModel
        locationStudentViewModel.getstudentLocationViewModel.observe(this){//observar cambios de respuesta
            validateCodeResponse()
        }
    }

    private fun validateCodeResponse() {//validar el codigo de respuesta recibido del servidor
        when (locationStudentViewModel.httpCodegetStudentLocation){
            Codes.CODE_SUCCESS -> {
                setSpinner()
                setupWebView()
                loadLeafletMap()
                setListeners()
                binding.loadinLayerShowMap.visibility = View.GONE
            }
            Codes.CODE_FAIL -> {
                alertMessage.showAlertDialog("Error", "ocurrio un error", this)
                returnLogin()
            }
            Codes.CODE_NOT_FOUND ->{
                alertMessage.showAlertDialog("Estudiante no encontrado","No se encontro informacion del estudiante deseado",this)
                returnMenuParent()
            }
        }
    }

    private fun returnMenuParent(){//presiona regresar
        val intent  = Intent (this,ShowMenuParent::class.java)
        startActivity(intent)
        finish()
    }

    private fun returnLogin(){//se activa en caso de que haya un error de conexion 500
        val intent  = Intent (this,MainActivity::class.java)
        startActivity(intent)
        finish()//destruir pila actual
    }

    private fun setSpinner() {
        var dateList:MutableList<String> = mutableListOf()
        for (x in LocationProvider.locationStudent.locations){//extrear las fechas de localizacion del provider
            dateList.add(x.dateLocation)
        }
        //configuracion del spinner para mostrar las fechas de localizacion
        val adapterSP = ArrayAdapter(this, R.layout.simple_spinner_item,dateList)
        adapterSP.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spLocationsHistory.adapter = adapterSP
        binding.spLocationsHistory.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // no se usa pero se debe impelementar aunque quede vacio ya se esta herando de AdapterView
            }

            //se activa al seleccionar una fecha del spinner o combo box(se hereda de AdapterView)
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                positionSpinner = position// guarda la posicion en una variable para acceder a ella y poder actualizar el mapa
                loadLeafletMap()
            }
        }
    }

    private fun setListeners() {//asignacion de eventos a los botones de actualizar y regresar
        binding.btnExitShowMap.setOnClickListener{
            var intent = Intent(this, ShowMenuParent::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnUpdateLocation.setOnClickListener{
            recreate()
        }
    }

    /*
    * configuracion de un webView (componente de la vista en el que se muestra el mapa)
    * se le indica que usara el navegador y que permita el uso de codigo javaScript desde kotlin para actualizar las ubicaciones
    * */
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.wbMap.webChromeClient = WebChromeClient()
        binding.wbMap.webViewClient = WebViewClient()

        val webSettings: WebSettings = binding.wbMap.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
    }


    private fun loadLeafletMap() {
        val longitude = LocationProvider.locationStudent.locations[positionSpinner].latitudeStudent
        val latitude = LocationProvider.locationStudent.locations[positionSpinner].longitudeStudent
        val htmlPath = "file:///android_asset/MapContainer.html"  // Ruta al archivo HTML de Leaflet en carpeta assets
        binding.wbMap.loadUrl(htmlPath)//carga el HTML en en el componente WebView
        //metodo de javaScript declarado en el archivo HTML de leaflet en carpeta assets
        val javascript = "javascript:addOrReplaceMarker($latitude, $longitude);"
        //llamado del la funcion de javaScript en el arhivo HTML de leaflet en carpeta assets
        binding.wbMap.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.evaluateJavascript(javascript, null)
            }
        }
    }

}