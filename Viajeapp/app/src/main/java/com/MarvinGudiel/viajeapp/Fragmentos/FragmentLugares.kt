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

class FragmentLugares : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_lugares, container, false)

        // Aquí puedes configurar el RecyclerView para mostrar las publicaciones
        val recyclerView: RecyclerView = view.findViewById(R.id.rvPublicacionesLugares)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = PublicacionAdapter(getLugaresPublicaciones())

        return view
    }

    // Función para obtener las publicaciones de lugares
    private fun getLugaresPublicaciones(): List<Publicacion> {
        // Aquí puedes añadir lógica para obtener publicaciones desde Firebase o una fuente de datos
        return listOf(
            Publicacion("Playa del Carmen", "Juan Perez", "Lugar turístico con playas espectaculares"),
            Publicacion("Volcán Tajumulco", "Ana Lopez", "El punto más alto de Guatemala, ideal para hacer senderismo")
        )
    }
}
