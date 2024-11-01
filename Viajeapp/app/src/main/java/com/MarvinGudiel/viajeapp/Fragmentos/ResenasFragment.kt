
package com.MarvinGudiel.viajeapp.Fragmentos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.MarvinGudiel.viajeapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ResenasFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var resenasList: MutableList<Resena>
    private lateinit var adapter: ResenaAdapter

    private lateinit var etNuevaResena: EditText
    private lateinit var btnAgregarRese: ImageButton

    private lateinit var contenedorResena: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_resena, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance()

        recyclerView = view.findViewById(R.id.recyclerViewResenas)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        resenasList = mutableListOf()
        adapter = ResenaAdapter(resenasList)
        recyclerView.adapter = adapter

        // Obtener el argumento publicacionId
        val publicacionId = arguments?.getString("publicacionId") ?: return
        Log.i("FirebaseDebug", "PublicacionId recibido: $publicacionId")

        // Cargar reseñas iniciales
        cargarResenas(publicacionId)

        etNuevaResena = view.findViewById(R.id.editTextComentario)
        btnAgregarRese = view.findViewById(R.id.btnAgregarComentario)

        btnAgregarRese.setOnClickListener {
            val textoResena = etNuevaResena.text.toString()

            if (textoResena.isNotBlank()) {
                if (publicacionId != null) {
                    agregarResenaAFirebase(publicacionId, textoResena)
                }
                etNuevaResena.text.clear() // Limpiar el campo de entrada después de agregar
            } else {
                Toast.makeText(requireContext(), "Por favor, escribe un comentario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para cargar reseñas
    private fun cargarResenas(publicacionId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resenas = obtenerResenas(publicacionId)
                resenasList.clear()
                resenasList.addAll(resenas)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("FirebaseDebug", "Error al cargar comentarios: ${e.message}")
            }
        }
    }

    // Función suspendida para obtener reseñas
    private suspend fun obtenerResenas(publicacionId: String): List<Resena> = suspendCancellableCoroutine { continuation ->
        val database = FirebaseDatabase.getInstance().getReference("publicaciones/$publicacionId/resenas")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val resenasList = mutableListOf<Resena>()
                for (dataSnapshot in snapshot.children) {
                    val resena = dataSnapshot.getValue(Resena::class.java)
                    resena?.let { resenasList.add(it) }
                }
                continuation.resume(resenasList)
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWithException(error.toException())
            }
        }

        database.addListenerForSingleValueEvent(listener)

        // Cancelar la consulta si la corrutina es cancelada
        continuation.invokeOnCancellation {
            database.removeEventListener(listener)
        }
    }

    // Método para guardar cada reseña en Firebase
    private fun agregarResenaAFirebase(publicacionId: String, texto: String) {
        val resenasRef = database.reference.child("publicaciones")
            .child(publicacionId)
            .child("resenas")
            .push() // Genera un nuevo ID para cada reseña

        val res = Resena(texto)
        resenasRef.setValue(res)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Comentario enviado", Toast.LENGTH_SHORT).show()

                // Recargar reseñas después de agregar una nueva
                cargarResenas(publicacionId)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al enviar comentario", Toast.LENGTH_SHORT).show()
            }
    }
}



