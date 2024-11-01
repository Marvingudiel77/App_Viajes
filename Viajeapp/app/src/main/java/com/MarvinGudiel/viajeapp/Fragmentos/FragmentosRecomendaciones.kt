package com.MarvinGudiel.viajeapp.Fragmentos

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.MarvinGudiel.viajeapp.R
import com.google.firebase.database.FirebaseDatabase

class FragmentoRecomendaciones : Fragment(R.layout.fragment_fragmentos_recomendaciones) {

    private lateinit var database: FirebaseDatabase
    private lateinit var contenedorRecomendaciones: LinearLayout
    private lateinit var etNuevaRecomendacion: EditText
    private lateinit var btnAgregarRecomendacion: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar la base de datos y los elementos de la vista
        database = FirebaseDatabase.getInstance()
        contenedorRecomendaciones = view.findViewById(R.id.contenedorRecomendaciones)
        etNuevaRecomendacion = view.findViewById(R.id.etNuevaRecomendacion)
        btnAgregarRecomendacion = view.findViewById(R.id.btnAgregarRecomendacion)

        btnAgregarRecomendacion.setOnClickListener {
            val recomendacion = etNuevaRecomendacion.text.toString()

            val publicacionId =  arguments?.getString("publicacionId") // Reemplaza con el ID correcto

            if (recomendacion.isNotBlank()) {
                // Guardar la recomendación en Firebase
                if (publicacionId != null) {
                    agregarRecomendacionAFirebase(publicacionId, recomendacion)
                }
                // Agregar un nuevo campo de recomendación en la pantalla
                agregarCampoDeRecomendacion(recomendacion)
                etNuevaRecomendacion.text.clear() // Limpiar el campo de entrada después de agregar
            } else {
                Toast.makeText(requireContext(), "Por favor, escribe una recomendación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para agregar dinámicamente un nuevo EditText para cada recomendación
    private fun agregarCampoDeRecomendacion(texto: String) {
        val nuevoEditText = EditText(requireContext()).apply {
            setText(texto)
            isEnabled = false // Deshabilitar para que sea solo de lectura
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8) // Margen entre recomendaciones
            }
        }
        contenedorRecomendaciones.addView(nuevoEditText)
    }

    // Método para guardar cada recomendación en Firebase
    private fun agregarRecomendacionAFirebase(publicacionId: String, recomendacion: String) {
        val recomendacionesRef = database.reference.child("publicaciones")
            .child(publicacionId)
            .child("recomendaciones")
            .push() // Genera un nuevo ID para cada recomendación
        val reco=  Recomendacion("titulooo", recomendacion)
        recomendacionesRef.setValue(reco)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Recomendación guardada en Firebase", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al guardar la recomendación", Toast.LENGTH_SHORT).show()
            }
    }
}
