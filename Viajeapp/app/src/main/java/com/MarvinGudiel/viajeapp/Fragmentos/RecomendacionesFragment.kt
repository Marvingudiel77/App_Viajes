package com.MarvinGudiel.viajeapp.Fragmentos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.MarvinGudiel.viajeapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RecomendacionesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recomendacionesList: MutableList<Recomendacion>
    private lateinit var adapter: RecomendacionesAdapter

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

        recomendacionesList = mutableListOf()
        adapter = RecomendacionesAdapter(recomendacionesList)
        recyclerView.adapter = adapter

        // Obtener el argumento publicacionId
        val publicacionId = arguments?.getString("publicacionId") ?: return
        Log.i("FirebaseDebug", "PublicacionId recibido: $publicacionId")

        // Llamar a la función suspendida dentro de una corrutina
        viewLifecycleOwner.lifecycleScope.launch() {
            try {
                val recomendaciones = obtenerRecomendaciones(publicacionId)
                recomendacionesList.clear()
                recomendacionesList.addAll(recomendaciones)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("FirebaseDebug", "Error al obtener recomendaciones: ${e.message}")
            }
        }
    }

    // Función suspendida para obtener recomendaciones
    private suspend fun obtenerRecomendaciones(publicacionId: String): List<Recomendacion> = suspendCancellableCoroutine { continuation ->
        val database = FirebaseDatabase.getInstance().getReference("publicaciones/$publicacionId/recomendaciones")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recomendacionesList = mutableListOf<Recomendacion>()
                for (dataSnapshot in snapshot.children) {
                    val recomendacion = dataSnapshot.getValue(Recomendacion::class.java)
                    recomendacion?.let { recomendacionesList.add(it) }
                }
                continuation.resume(recomendacionesList)
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
}
