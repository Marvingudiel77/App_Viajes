package com.MarvinGudiel.viajeapp.Fragmentos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.MarvinGudiel.viajeapp.R

class PublicacionesActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publicaciones)

        val spinnerTipoPublicacion = findViewById<Spinner>(R.id.spinnerTipoPublicacion)

        // Manejar la selección del Spinner
        spinnerTipoPublicacion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> replaceFragment(FragmentLugares())  // Cargar el fragmento de Lugares
                    1 -> replaceFragment(FragmentHoteles())  // Cargar el fragmento de Hoteles
                    2 -> replaceFragment(FragmentRestaurantes())  // Cargar el fragmento de Restaurantes
                    3 -> replaceFragment(FragmentAeropuertos())  // Cargar el fragmento de Aeropuertos
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada
            }
        }
    }

    // Función para reemplazar el fragmento
    private fun replaceFragment(fragment: FragmentAeropuertos) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)  // Donde fragment_container es el contenedor
        fragmentTransaction.addToBackStack(null)  // Opcional, si quieres permitir navegación hacia atrás
        fragmentTransaction.commit()
    }
}
