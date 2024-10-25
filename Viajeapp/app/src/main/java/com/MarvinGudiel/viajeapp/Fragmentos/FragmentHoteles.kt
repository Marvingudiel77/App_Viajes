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

class FragmentHoteles : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_hoteles, container, false)

        // Aquí puedes configurar el RecyclerView para mostrar las publicaciones de hoteles
        val recyclerView: RecyclerView = view.findViewById(R.id.rvPublicacionesHoteles)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = PublicacionAdapter(getHotelesPublicaciones())

        return view
    }

    // Función para obtener las publicaciones de hoteles
    private fun getHotelesPublicaciones(): List<Publicacion> {
        // Aquí puedes añadir lógica para obtener publicaciones desde Firebase o una fuente de datos
        return listOf(
            Publicacion("Hotel Las Brisas", "Carlos Fernandez", "Un hotel con vista al mar y excelente servicio."),
            Publicacion("Resort Montaña Azul", "Maria Rodriguez", "Resort de lujo en la cima de las montañas con spa incluido.")
        )
    }
}
