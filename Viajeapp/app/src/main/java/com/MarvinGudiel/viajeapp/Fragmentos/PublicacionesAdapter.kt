package com.MarvinGudiel.viajeapp.Fragmentos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R

class PublicacionesAdapter(private val publicacionesList: List<Publicacion>) :
    RecyclerView.Adapter<PublicacionesAdapter.PublicacionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_publicacion, parent, false)
        return PublicacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PublicacionViewHolder, position: Int) {
        val publicacion = publicacionesList[position]
        holder.bind(publicacion)
    }

    override fun getItemCount() = publicacionesList.size

    class PublicacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreServicioTextView: TextView = itemView.findViewById(R.id.nombreServicioTextView)
        private val categoriaTextView: TextView = itemView.findViewById(R.id.categoriaTextView)
        private val costoTextView: TextView = itemView.findViewById(R.id.costoTextView)
        private val descripcionTextView: TextView = itemView.findViewById(R.id.descripcionTextView)

        fun bind(publicacion: Publicacion) {
            nombreServicioTextView.text = publicacion.nombre_servicio
            categoriaTextView.text = publicacion.categoria
            costoTextView.text = if (publicacion.es_gratuito) "Gratuito" else "$${publicacion.costo}"
            descripcionTextView.text = publicacion.descripcion
        }
    }
}