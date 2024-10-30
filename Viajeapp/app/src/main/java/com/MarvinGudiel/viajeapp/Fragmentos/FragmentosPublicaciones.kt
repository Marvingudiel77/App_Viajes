package com.MarvinGudiel.viajeapp.Fragmentos

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.MarvinGudiel.viajeapp.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FragmentosPublicaciones : Fragment(R.layout.fragment_fragmentos_publicaciones) {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        val nombreServicioEditText: EditText = view.findViewById(R.id.etNombreServicio)
        val categoriaEditText: EditText = view.findViewById(R.id.etCategoria)
        val esGratuitoSwitch: Switch = view.findViewById(R.id.switchEsGratuito)
        val costoEditText: EditText = view.findViewById(R.id.etCosto)
        val descripcionEditText: EditText = view.findViewById(R.id.etDescripcion)
        val subirImagenButton: Button = view.findViewById(R.id.btnSubirImagen)
        val agregarServicioButton: Button = view.findViewById(R.id.btnAgregarServicio)

        // Mostrar el campo de costo solo si el servicio no es gratuito
        esGratuitoSwitch.setOnCheckedChangeListener { _, isChecked ->
            costoEditText.visibility = if (isChecked) View.GONE else View.VISIBLE
        }

        // Lógica para subir una imagen
        subirImagenButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1000)
        }

        // Lógica para agregar el servicio
        agregarServicioButton.setOnClickListener {
            val nombreServicio = nombreServicioEditText.text.toString()
            val categoria = categoriaEditText.text.toString()
            val esGratuito = esGratuitoSwitch.isChecked
            val costo = if (esGratuito) 0.0 else costoEditText.text.toString().toDoubleOrNull() ?: 0.0
            val descripcion = descripcionEditText.text.toString()

            if (nombreServicio.isNotEmpty() && categoria.isNotEmpty() && descripcion.isNotEmpty()) {
                val publicacion = hashMapOf(
                    "nombreServicio" to nombreServicio,
                    "categoria" to categoria,
                    "esGratuito" to esGratuito,
                    "costo" to costo,
                    "descripcion" to descripcion
                )

                firestore.collection("publicaciones")
                    .add(publicacion)
                    .addOnSuccessListener { documentReference ->
                        if (imageUri != null) {
                            subirImagenAFirebase(documentReference.id)
                        } else {
                            Toast.makeText(requireContext(), "Servicio agregado sin imagen", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Error al agregar el servicio", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para manejar el resultado de la selección de la imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            Toast.makeText(requireContext(), "Imagen seleccionada", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para subir la imagen a Firebase Storage
    private fun subirImagenAFirebase(documentId: String) {
        val imagenRef = storage.reference.child("publicaciones/$documentId.jpg")
        imageUri?.let {
            imagenRef.putFile(it)
                .addOnSuccessListener {
                    imagenRef.downloadUrl.addOnSuccessListener { uri ->
                        firestore.collection("publicaciones").document(documentId)
                            .update("imagenUrl", uri.toString())
                        Toast.makeText(requireContext(), "Servicio agregado con imagen", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

