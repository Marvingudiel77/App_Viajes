package com.MarvinGudiel.viajeapp.Fragmentos


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.MarvinGudiel.viajeapp.R

class FragmentPublicaciones : Fragment() {

    private val imagenesList = mutableListOf<Uri>()
    private lateinit var imagenAdapter: ImagenAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fragment_publicaciones, container, false)

        // Configuración del RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.rvImagenes)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        imagenAdapter = ImagenAdapter(imagenesList)
        recyclerView.adapter = imagenAdapter

        // Botón para subir imagen
        val btnSubirImagen: Button = view.findViewById(R.id.btnSubirImagen)
        btnSubirImagen.setOnClickListener {
            seleccionarImagen()
        }

        // Configurar el Spinner
        val spinner: Spinner = view.findViewById(R.id.spinnerTipoPublicacion)
        configurarSpinner(spinner)

        // Configurar botones de interacción
        configurarBotonesInteraccion(view)

        return view
    }

    private fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECCIONAR_IMAGEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECCIONAR_IMAGEN && resultCode == -1 && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                imagenesList.add(selectedImageUri)
                imagenAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun configurarSpinner(spinner: Spinner) {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tipo_publicacion_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val tipoSeleccionado = parent.getItemAtPosition(position).toString()
                Toast.makeText(requireContext(), "Seleccionado: $tipoSeleccionado", Toast.LENGTH_SHORT).show()
                cargarPublicacionesPorTipo(tipoSeleccionado)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun cargarPublicacionesPorTipo(tipo: String) {
        // Implementar la lógica para cargar publicaciones basadas en el tipo
    }

    private fun configurarBotonesInteraccion(view: View) {
        val btnMeGusta: Button = view.findViewById(R.id.btnMeGusta)
        val btnNoMeGusta: Button = view.findViewById(R.id.btnNoMeGusta)
        val btnVerMas: Button = view.findViewById(R.id.btnVerMas)
        val btnBorrar: Button = view.findViewById(R.id.btnBorrar)
        val btnVerReseñas: Button = view.findViewById(R.id.btnVerReseñas)

        btnMeGusta.setOnClickListener { Toast.makeText(requireContext(), "Me gusta", Toast.LENGTH_SHORT).show() }
        btnNoMeGusta.setOnClickListener { Toast.makeText(requireContext(), "No me gusta", Toast.LENGTH_SHORT).show() }
        btnVerMas.setOnClickListener { Toast.makeText(requireContext(), "Ver más detalles", Toast.LENGTH_SHORT).show() }
        btnBorrar.setOnClickListener { Toast.makeText(requireContext(), "Publicación eliminada", Toast.LENGTH_SHORT).show() }
        btnVerReseñas.setOnClickListener { Toast.makeText(requireContext(), "Ver reseñas", Toast.LENGTH_SHORT).show() }
    }

    companion object {
        const val SELECCIONAR_IMAGEN = 1001
    }
}
