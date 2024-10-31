package com.MarvinGudiel.viajeapp.Fragmentos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.MarvinGudiel.viajeapp.R
import com.google.firebase.database.FirebaseDatabase

class FragmentoRecomendaciones : Fragment(R.layout.fragment_fragmentos_recomendaciones) {

    private lateinit var database: FirebaseDatabase
    private lateinit var etRecomendacion: EditText
    private lateinit var btnAgregarRecomendacion: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar la base de datos
        database = FirebaseDatabase.getInstance()

        // Referencias a los elementos de la vista
        etRecomendacion = view.findViewById(R.id.etRecomendacion)
        btnAgregarRecomendacion = view.findViewById(R.id.btnAgregarRecomendacion)

        btnAgregarRecomendacion.setOnClickListener {
            val recomendacion = etRecomendacion.text.toString()
            val publicacionId = "ID_de_la_publicacion" // Reemplaza con el ID correcto

            if (recomendacion.isNotBlank()) {
                agregarRecomendacionAFirebase(publicacionId, recomendacion)
                etRecomendacion.text.clear()
                Toast.makeText(requireContext(), "Recomendación agregada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Por favor, escribe una recomendación", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // Método para agregar la recomendación a Firebase
    private fun agregarRecomendacionAFirebase(publicacionId: String, recomendacion: String) {
        val recomendacionesRef = database.reference.child("publicaciones")
            .child(publicacionId)
            .child("recomendaciones").push()
        recomendacionesRef.setValue(recomendacion)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Recomendación guardada en Firebase", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al guardar la recomendación", Toast.LENGTH_SHORT).show()
            }
    }

}
