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
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ResenasFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var resenasList: MutableList<Resena>
    private lateinit var adapter: ResenaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_resena, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewResenas)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        resenasList = mutableListOf()
        adapter = ResenaAdapter(resenasList)
        recyclerView.adapter = adapter

        // Obtener el argumento publicacionId
        val publicacionId = arguments?.getString("publicacionId") ?: return
        Log.i("FirebaseDebug", "PublicacionId recibido: $publicacionId")

        // Llamar a la función suspendida dentro de una corrutina
        viewLifecycleOwner.lifecycleScope.launch() {
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

    // Función suspendida para obtener recomendaciones
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
}

