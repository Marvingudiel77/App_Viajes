package com.MarvinGudiel.viajeapp.Fragmentos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R

class RecomendacionesAdapter(
    private val recomendacionesList: List<Recomendacion>
) : RecyclerView.Adapter<RecomendacionesAdapter.RecomendacionViewHolder>() {

    // ViewHolder para cada elemento de recomendación
    class RecomendacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloTextView: TextView = itemView.findViewById(R.id.tituloTextView)
        val descripcionTextView: TextView = itemView.findViewById(R.id.descripcionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecomendacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recomendacion, parent, false)
        return RecomendacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecomendacionViewHolder, position: Int) {
        val recomendacion = recomendacionesList[position]
        holder.tituloTextView.text = recomendacion.titulo
        holder.descripcionTextView.text = recomendacion.descripcion
    }

    override fun getItemCount(): Int {
        return recomendacionesList.size
    }
}
