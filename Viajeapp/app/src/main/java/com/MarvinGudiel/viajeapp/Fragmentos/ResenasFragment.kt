
package com.MarvinGudiel.viajeapp.Fragmentos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.Adaptadores.ResenaAdapter
import com.MarvinGudiel.viajeapp.Modelos.Resena
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
    private lateinit var resenasList: MutableList<R
    esena>
    private lateinit var adapter: ResenaAdapter

    private lateinit var etNuevaResena: EditText
    private lateinit var btnAgregarRese: ImageButton
    private lateinit var stars: List<ImageView>
    private var selectedRating: Int = 0

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

        val publicacionId = arguments?.getString("publicacionId") ?: return
        Log.i("FirebaseDebug", "PublicacionId recibido: $publicacionId")

        cargarResenas(publicacionId)

        etNuevaResena = view.findViewById(R.id.editTextComentario)
        btnAgregarRese = view.findViewById(R.id.btnAgregarComentario)

        stars = listOf(
            view.findViewById(R.id.star1),
            view.findViewById(R.id.star2),
            view.findViewById(R.id.star3),
            view.findViewById(R.id.star4),
            view.findViewById(R.id.star5)
        )

        for (i in stars.indices) {
            stars[i].setOnClickListener {
                selectedRating = i + 1
                updateStarRating()
            }
        }

        btnAgregarRese.setOnClickListener {
            val textoResena = etNuevaResena.text.toString()

            if (textoResena.isNotBlank() && selectedRating > 0) {
                if (publicacionId != null) {
                    agregarResenaAFirebase(publicacionId, textoResena, selectedRating)
                }
                etNuevaResena.text.clear()
                selectedRating = 0
                updateStarRating()
            } else {
                Toast.makeText(requireContext(), "Por favor, escribe un comentario y selecciona una calificación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateStarRating() {
        for (i in stars.indices) {
 stars[i].setImageResource(if (i < selectedRating) R.drawable.ic_star_filled else R.drawable.ic_star_empty)
        }
    }

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

        continuation.invokeOnCancellation {
            database.removeEventListener(listener)
        }
    }

    private fun agregarResenaAFirebase(publicacionId: String, texto: String, rating: Int) {
        val resenasRef = database.reference.child("publicaciones")
            .child(publicacionId)
            .child("resenas")
            .push()

        val res = Resena(texto, rating)

        resenasRef.setValue(res)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Reseña agregada con éxito", Toast.LENGTH_SHORT).show()
                cargarResenas(publicacionId)
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseDebug", "Error al agregar reseña: ${e.message}")
            }
    }
}
