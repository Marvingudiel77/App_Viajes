package com.MarvinGudiel.viajeapp.Fragmentos

data class Publicacion(
    val nombre_servicio: String = "",
    val categoria: String = "",
    val es_gratuito: Boolean = false,
    val costo: Double = 0.0,
    val descripcion: String = "",
    val imagen_url: String = "",
    var key:String?= null,
    var likes: Int = 0,
    var dislikes: Int = 0,
    var hasLiked: Boolean = false,
    var hasDisliked: Boolean = false
)