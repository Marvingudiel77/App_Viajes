package com.MarvinGudiel.viajeapp.Fragmentos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.MarvinGudiel.viajeapp.R

class RecomendacionesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recomendacionesList: MutableList<Recomendacion>
    private lateinit var adapter: RecomendacionesAdapter
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recomendaciones, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewRecomendaciones)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Obtener el argumento publicacionId
        val publicacionId = arguments?.getString("publicacionId") ?: return
        Log.i("keily",publicacionId)

        recomendacionesList = mutableListOf()
        adapter = RecomendacionesAdapter(recomendacionesList)
        recyclerView.adapter = adapter

        // Referencia a la subcolección "recomendaciones" dentro de la publicación especificada
        database = FirebaseDatabase.getInstance().getReference("publicaciones/$publicacionId/recomendaciones")

        // Consultar las recomendaciones dentro de la publicación
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recomendacionesList.clear()
                for (dataSnapshot in snapshot.children) {
                    val recomendacion = dataSnapshot.getValue(Recomendacion::class.java)
                    recomendacion?.let { recomendacionesList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo del error
            }
        })
    }
}


