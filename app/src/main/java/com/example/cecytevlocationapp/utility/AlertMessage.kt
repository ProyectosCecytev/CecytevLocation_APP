package com.example.cecytevlocationapp.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.example.cecytevlocationapp.ui.view.MainActivity

class AlertMessage {

    //mensaje general usado en culquier parte del codigo
     fun showAlertDialog(title: String, message: String,context : Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("ACEPTAR") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //muestra mensaje  cierra la actividad actual al presionar aceptar
    fun showAlertDialogTokenAthorization(title: String, message: String,context: Context,contextActivity: Activity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("ACEPTAR") { dialog, _ ->
            dialog.dismiss()
            //mandando a actividad de login
            var intent = Intent (context, MainActivity::class.java)
            context.startActivity(intent)
            //cerando actividad actual
            contextActivity.finish()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}