package com.MarvinGudiel.viajeapp.Modelos

class Usuario {
    var uid : String =""
    var email: String = ""
    var nombres: String = ""
    var imagen : String = ""


    constructor()

    constructor(uid: String, email: String, nombres: String, imagen: String) {
        this.uid = uid
        this.email = email
        this.nombres = nombres
        this.imagen = imagen
    }


}