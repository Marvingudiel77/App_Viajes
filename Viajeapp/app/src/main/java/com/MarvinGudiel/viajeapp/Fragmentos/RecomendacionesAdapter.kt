package com.MarvinGudiel.viajeapp.Fragmentos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R


class RecomendacionesAdapter(private val recomendacionesList: List<Recomendacion>) :
    RecyclerView.Adapter<RecomendacionesAdapter.RecomendacionViewHolder>() {

    class RecomendacionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewComentario: TextView = view.findViewById(R.id.tv_recomendacion)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecomendacionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recomendacion, parent, false)
        return RecomendacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecomendacionViewHolder, position: Int) {
        val recomendacion = recomendacionesList[position]
        holder.textViewComentario.text = recomendacion.texto
        holder.ratingBar.rating = recomendacion.rating
    }

    override fun getItemCount(): Int {
        return recomendacionesList.size
    }
}
