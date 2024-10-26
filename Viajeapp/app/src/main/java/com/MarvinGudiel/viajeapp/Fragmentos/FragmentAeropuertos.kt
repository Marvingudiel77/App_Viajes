package com.MarvinGudiel.viajeapp.Fragmentos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R

class FragmentAeropuertos : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_aeropuertos, container, false)

        // Configuración del RecyclerView para mostrar las publicaciones de aeropuertos
        val recyclerView: RecyclerView = view.findViewById(R.id.rvFragment_Aeropuertos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = PublicacionAdapter(getAeropuertosPublicaciones())

        return view
    }

    // Función para obtener las publicaciones de aeropuertos
    private fun getAeropuertosPublicaciones(): List<Publicacion> {
        // Aquí puedes añadir lógica para obtener publicaciones desde Firebase o una fuente de datos
        return listOf(
            Publicacion("Aeropuerto Internacional La Aurora", "Carlos Pérez", "El aeropuerto más grande de Guatemala con vuelos internacionales."),
            Publicacion("Aeropuerto de Tocumen", "María González", "Conocido por ser uno de los principales hubs de Latinoamérica.")
        )
    }
}
