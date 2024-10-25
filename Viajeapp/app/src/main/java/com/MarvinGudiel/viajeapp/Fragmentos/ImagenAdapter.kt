package com.MarvinGudiel.viajeapp.Fragmentos


import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R

class ImagenAdapter(private val imagenesList: List<Uri>) : RecyclerView.Adapter<ImagenAdapter.ImagenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_imagen, parent, false)
        return ImagenViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagenViewHolder, position: Int) {
        val imageUri = imagenesList[position]
        holder.bind(imageUri)
    }

    override fun getItemCount(): Int {
        return imagenesList.size
    }

    inner class ImagenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagen: ImageView = itemView.findViewById(R.id.ivImagenItem)

        fun bind(uri: Uri) {
            imagen.setImageURI(uri)  // Cargar la imagen desde el URI
        }
    }
}
