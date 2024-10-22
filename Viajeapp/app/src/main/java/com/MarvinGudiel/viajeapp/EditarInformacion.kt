package com.MarvinGudiel.viajeapp

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.MarvinGudiel.viajeapp.databinding.ActivityEditarInformacionBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditarInformacion : AppCompatActivity() {

    private lateinit var binding: ActivityEditarInformacionBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private var nombres = ""
    private var descripcionIntereses = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditarInformacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        cargarInformacion()

        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnActualizar.setOnClickListener {
            validarInformacion()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validarInformacion() {
        nombres = binding.txtNombres.text.toString().trim()
        descripcionIntereses = binding.txtDescripcion.text.toString().trim()

        when {
            nombres.isEmpty() -> {
                binding.txtNombres.error = "Ingrese Nombres"
                binding.txtNombres.requestFocus()
            }
            descripcionIntereses.isEmpty() -> {
                binding.txtDescripcion.error = "Ingrese una descripción de sus intereses"
                binding.txtDescripcion.requestFocus()
            }
            else -> {
                actualizarInfo()
            }
        }
    }

    private fun actualizarInfo() {
        progressDialog.setMessage("Actualizando Información...")
        progressDialog.show()

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["nombres"] = nombres
        hashMap["interesesViaje"] = descripcionIntereses // Actualiza la clave correcta

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Se actualizó su información", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cargarInformacion() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = "${snapshot.child("nombres").value}"
                    val descripcionIntereses = "${snapshot.child("interesesViaje").value}" // Actualiza la clave correcta
                    val imagen = "${snapshot.child("imagen").value}"

                    binding.txtNombres.setText(nombres)
                    binding.txtDescripcion.setText(descripcionIntereses)

                    try {
                        Glide.with(applicationContext)
                            .load(imagen)
                            .placeholder(R.drawable.img_perfil)
                            .into(binding.ivPerfil)
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Error al cargar la información: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
