package com.MarvinGudiel.viajeapp.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R

class RecomendacionesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recomendaciones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewRecomendaciones)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Lista de recomendaciones de ejemplo
        val recomendacionesList = listOf(
            Recomendacion("Visita el Museo", "Es un lugar lleno de historia y cultura."),
            Recomendacion("Explora el Parque Nacional", "Perfecto para una caminata y disfrutar de la naturaleza."),
            // Agrega más recomendaciones según necesites
        )

        val adapter = RecomendacionesAdapter(recomendacionesList)
        recyclerView.adapter = adapter
    }
}
