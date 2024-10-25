package com.MarvinGudiel.viajeapp.Fragmentos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R

class RecomendacionesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendaciones)

        val recyclerView: RecyclerView = findViewById(R.id.rv_recomendaciones)

        // Ejemplo de lista de recomendaciones
        val recomendacionesList = listOf(
            Recomendacion("Muy buen servicio", 4.5f),
            Recomendacion("Recomendado!", 5.0f),
            Recomendacion("Me gustó la experiencia", 4.0f)
        )

        // Configurar el RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecomendacionesAdapter(recomendacionesList)
    }
}

