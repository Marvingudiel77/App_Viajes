package com.MarvinGudiel.viajeapp.Fragmentos

data class Publicacion(
    val nombre_servicio: String = "",
    val categoria: String = "",
    val es_gratuito: Boolean = false,
    val costo: Double = 0.0,
    val descripcion: String = "",
    val imagen_url: String = "",
    var key:String?= null
)