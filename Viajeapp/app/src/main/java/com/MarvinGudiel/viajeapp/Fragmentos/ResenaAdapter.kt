package com.MarvinGudiel.viajeapp.Adaptadores

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.Modelos.Resena
import com.MarvinGudiel.viajeapp.R

class ResenaAdapter(private val resenas: List<Resena>) : RecyclerView.Adapter<ResenaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreUsuario: TextView = itemView.findViewById(R.id.nombreUsuarioTextView)
        val fotoPerfil: ImageView = itemView.findViewById(R.id.IvPerfil)
        val textoResena: TextView = itemView.findViewById(R.id.textoResenaTextView)
        val star1: ImageView = itemView.findViewById(R.id.star1)
        val star2: ImageView = itemView.findViewById(R.id.star2)
        val star3: ImageView = itemView.findViewById(R.id.star3)
        val star4: ImageView = itemView.findViewById(R.id.star4)
        val star5: ImageView = itemView.findViewById(R.id.star5)
    }
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_resena, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resena = resenas[position]
        holder.nombreUsuario.text = resena.nombreUsuario
        holder.textoResena.text = resena.texto

        holder.fotoPerfil.setImageResource(R.drawable.ic_imagen_perfil)

        val rating = resena.rating

        val stars = listOf(holder.star1, holder.star2, holder.star3, holder.star4, holder.star5)

        for (i in stars.indices) {
            if (i < rating) {
                stars[i].setImageResource(R.drawable.ic_star_filled)
            } else {
                stars[i].setImageResource(R.drawable.ic_star_empty)
            }
        }
    }

    override fun getItemCount(): Int {
        return resenas.size
    }
}
