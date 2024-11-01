package com.MarvinGudiel.viajeapp.Fragmentos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R

class ResenaAdapter(
    private val resenasList: List<Resena>
) : RecyclerView.Adapter<ResenaAdapter.ResenasViewHolder>() {

    // ViewHolder para cada elemento de recomendación
    class ResenasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textoTextView: TextView = itemView.findViewById(R.id.textoTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResenasViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_resena, parent, false)
        return ResenasViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResenasViewHolder, position: Int) {
        val resena = resenasList[position]
        holder.textoTextView.text = resena.texto

    }

    override fun getItemCount(): Int {
        return resenasList.size
    }
}

