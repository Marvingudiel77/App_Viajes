package com.MarvinGudiel.viajeapp.Fragmentos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R
import com.google.firebase.database.*



class ListarPublicacionesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var publicacionesList: MutableList<Publicacion>
    private lateinit var adapter: PublicacionesAdapter
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_listar_publicaciones)

        recyclerView = findViewById(R.id.recyclerViewPublicaciones)
        recyclerView.layoutManager = LinearLayoutManager(this)
        publicacionesList = mutableListOf()
        adapter = PublicacionesAdapter(publicacionesList)
        recyclerView.adapter = adapter

        // Referencia a Realtime Database
        database = FirebaseDatabase.getInstance().getReference("publicaciones")

        // Obtener datos de Realtime Database
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                publicacionesList.clear()
                for (dataSnapshot in snapshot.children) {
                    val publicacion = dataSnapshot.getValue(Publicacion::class.java)
                    publicacion?.let { publicacionesList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo del error
            }
        })
//        val btnRecomendaciones = findViewById<Button>(R.id.btn_recomenda)
//        btnRecomendaciones.setOnClickListener {
//            mostrarRecomendacionesFragment()
//        }



    }

//    private fun mostrarRecomendacionesFragment() {
//        // Reemplaza el contenido actual por el fragmento de recomendaciones
//        val fragment = RecomendacionesFragment()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentoFL, fragment)
//            .addToBackStack(null)
//            .commit()
//    }
}