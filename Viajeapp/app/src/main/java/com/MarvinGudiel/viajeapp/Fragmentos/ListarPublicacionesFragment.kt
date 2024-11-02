package com.MarvinGudiel.viajeapp.Fragmentos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ListarPublicacionesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var publicacionesList: MutableList<Publicacion>
    private lateinit var adapter: PublicacionesAdapter
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño para este fragmento
        return inflater.inflate(R.layout.fragment_listar_publicaciones, container, false)
    }

    @SuppressLint("MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewPublicaciones)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        publicacionesList = mutableListOf()
        adapter = PublicacionesAdapter(publicacionesList,parentFragmentManager)
        recyclerView.adapter = adapter

        // Referencia a Realtime Database
        database = FirebaseDatabase.getInstance().getReference("publicaciones")

        // Obtener datos de Realtime Database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                publicacionesList.clear()
                for (dataSnapshot in snapshot.children) {
                    val publicacion = dataSnapshot.getValue(Publicacion::class.java)
                    publicacion?.let {
                      it.key= dataSnapshot.key
                        publicacionesList.add(it) }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo del error
            }
        })

        // Configuración del FAB para abrir la actividad de creación de publicaciones
        val fabAddPublication: Button = view.findViewById(R.id.btn_add)
        fabAddPublication.setOnClickListener {
            // Navega a la actividad de creación de publicaciones
            // Navegar al fragmento de creación de publicaciones
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentoFL, FragmentosPublicaciones())
                .addToBackStack(null)  // Permite regresar al fragmento anterior con el botón de retroceso
                .commit()
        }
    }


}
