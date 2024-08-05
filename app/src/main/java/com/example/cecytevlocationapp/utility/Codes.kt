package com.example.cecytevlocationapp.utility

 class Codes {
  companion object{
      //Todo Bien
      var CODE_SUCCESS: Int = 200
      //Error En Servidor
      var CODE_FAIL: Int = 500
      //Elemento no encontrado
      var CODE_NOT_FOUND: Int = 404
      //No se cuenta cn un token valido
      var NOT_AUTHORIZATION_TOKEN : Int = 403
      //Se envio mal la peticion(uso de caracteres especiales no validos u otros)
      var CODE_BAD_REQUEST : Int = 400
  }

 }