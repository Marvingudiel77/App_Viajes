package com.MarvinGudiel.viajeapp.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R
import com.google.firebase.database.FirebaseDatabase

class PublicacionesAdapter(
    private val publicacionesList: List<Publicacion>,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<PublicacionesAdapter.PublicacionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_publicacion, parent, false)
        return PublicacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PublicacionViewHolder, position: Int) {
        val publicacion = publicacionesList[position]
        holder.bind(publicacion)

        holder.btnLike.setOnClickListener {
            if (!publicacion.hasLiked) {
                publicacion.likes++
                publicacion.hasLiked = true
                if (publicacion.hasDisliked) {
                    publicacion.dislikes--
                    publicacion.hasDisliked = false
                }
                updateLikesInDatabase(publicacion)
                updateDislikesInDatabase(publicacion)
            } else {
                publicacion.likes--
                publicacion.hasLiked = false
                updateLikesInDatabase(publicacion)
            }
            holder.updateLikesCount(publicacion.likes)
            holder.updateLikeButton(publicacion)
            holder.updateDislikeButton(publicacion)
        }

        holder.btnDislike.setOnClickListener {
            if (!publicacion.hasDisliked) {
                publicacion.dislikes++
                publicacion.hasDisliked = true
                if (publicacion.hasLiked) {
                    publicacion.likes--
                    publicacion.hasLiked = false
                }
                updateLikesInDatabase(publicacion)
                updateDislikesInDatabase(publicacion)
            } else {
                publicacion.dislikes--
                publicacion.hasDisliked = false
                updateDislikesInDatabase(publicacion)
            }
            holder.updateDislikesCount(publicacion.dislikes)
            holder.updateLikeButton(publicacion)
            holder.updateDislikeButton(publicacion)
        }

        val fragment = RecomendacionesFragment()
        val fragmentResena = ResenasFragment()
        val args = Bundle().apply { putString("publicacionId", publicacion.key) }
        fragment.arguments = args
        fragmentResena.arguments = args

        holder.btnPubReco.setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentoFL, fragment)
                .addToBackStack(null)
                .commit()
        }
        holder.btnPubResena.setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentoFL, fragmentResena)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount() = publicacionesList.size

    private fun updateLikesInDatabase(publicacion: Publicacion) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("publicaciones/${publicacion.key}")
        databaseRef.child("likes").setValue(publicacion.likes)
        databaseRef.child("hasLiked").setValue(publicacion.hasLiked)
    }

    private fun updateDislikesInDatabase(publicacion: Publicacion) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("publicaciones/${publicacion.key}")
        databaseRef.child("dislikes").setValue(publicacion.dislikes)
        databaseRef.child("hasDisliked").setValue(publicacion.hasDisliked)
    }

    class PublicacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreServicioTextView: TextView = itemView.findViewById(R.id.nombreServicioTextView)
        private val categoriaTextView: TextView = itemView.findViewById(R.id.categoriaTextView)
        private val costoTextView: TextView = itemView.findViewById(R.id.costoTextView)
        private val descripcionTextView: TextView = itemView.findViewById(R.id.descripcionTextView)
        val btnPubReco: ImageButton = itemView.findViewById(R.id.btn_pub_reco)
        val btnPubResena: ImageButton = itemView.findViewById(R.id.btn_pub_resena)
        val btnLike: ImageButton = itemView.findViewById(R.id.btn_like)
        val btnDislike: ImageButton = itemView.findViewById(R.id.btn_dislike)
        private val cantLike: TextView = itemView.findViewById(R.id.cantlike)
        private val cantDislike: TextView = itemView.findViewById(R.id.cantdislike)

        fun bind(publicacion: Publicacion) {
            nombreServicioTextView.text = publicacion.nombre_servicio
            categoriaTextView.text = publicacion.categoria
            costoTextView.text = if (publicacion.es_gratuito) "Gratuito" else "$${publicacion.costo}"
            descripcionTextView.text = publicacion.descripcion
            updateLikesCount(publicacion.likes)
            updateDislikesCount(publicacion.dislikes)
            updateLikeButton(publicacion)
            updateDislikeButton(publicacion)
        }

        fun updateLikesCount(likes: Int) {
            cantLike.text = "$likes"
        }

        fun updateDislikesCount(dislikes: Int) {
            cantDislike.text = "$dislikes"
        }

        fun updateLikeButton(publicacion: Publicacion) {
            btnLike.alpha = if (publicacion.hasLiked) 1.0f else 0.5f
        }

        fun updateDislikeButton(publicacion: Publicacion) {
            btnDislike.alpha = if (publicacion.hasDisliked) 1.0f else 0.5f
        }
    }
}