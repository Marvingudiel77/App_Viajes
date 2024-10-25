package com.MarvinGudiel.viajeapp.Fragmentos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R

class PublicacionAdapter(private val listaPublicaciones: List<Publicacion>) :
    RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder>() {

    class PublicacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo = itemView.findViewById<TextView>(R.id.tvTitulo)
        val descripcion = itemView.findViewById<TextView>(R.id.tvDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_publicacion, parent, false)
        return PublicacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PublicacionViewHolder, position: Int) {
        val publicacion = listaPublicaciones[position]
        holder.titulo.text = publicacion.titulo
        holder.descripcion.text = publicacion.descripcion
    }

    override fun getItemCount(): Int {
        return listaPublicaciones.size
    }
}
