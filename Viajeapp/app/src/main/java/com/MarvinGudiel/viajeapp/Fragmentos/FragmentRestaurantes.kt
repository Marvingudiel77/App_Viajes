package com.MarvinGudiel.viajeapp.Fragmentos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R

class FragmentRestaurantes : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_restaurantes, container, false)

        // Aquí puedes configurar el RecyclerView para mostrar las publicaciones de restaurantes
        val recyclerView: RecyclerView = view.findViewById(R.id.rvPublicacionesRestaurantes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = PublicacionAdapter(getRestaurantesPublicaciones())

        return view
    }

    // Función para obtener las publicaciones de restaurantes
    private fun getRestaurantesPublicaciones(): List<Publicacion> {
        // Aquí puedes añadir lógica para obtener publicaciones desde Firebase o una fuente de datos
        return listOf(
            Publicacion("Restaurante El Pescador", "Luis Martínez", "Deliciosos mariscos frescos con vista al mar."),
            Publicacion("Café Central", "Ana Gómez", "Cafetería con ambiente acogedor y postres artesanales.")
        )
    }
}
