package com.MarvinGudiel.viajeapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class EditarInformacion : AppCompatActivity() {

    private lateinit var binding: ActivityEditarInformacionBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var nombres = ""
    private var descripcion = ""
    private var imageUri: Uri? = null

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditarInformacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data
                binding.ivPerfil.setImageURI(imageUri)
            } else {
                Toast.makeText(this, "Imagen no seleccionada", Toast.LENGTH_SHORT).show()
            }
        }

        cargarInformacion()

        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnActualizar.setOnClickListener {
            validarInformacion()
        }

        binding.ivPerfil.setOnClickListener {
            seleccionarImagen()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validarInformacion() {
        nombres = binding.txtNombres.text.toString().trim()
        descripcion = binding.txtDescripcion.text.toString().trim()

        if (nombres.isEmpty()) {
            binding.txtNombres.error = "Ingrese Nombres"
            binding.txtNombres.requestFocus()
        } else if (descripcion.isEmpty()) {
            binding.txtDescripcion.error = "Ingrese una descripción"
            binding.txtDescripcion.requestFocus()
        } else if (imageUri != null) {
            actualizarImagenYInfo()
        } else {
            actualizarInfo()
        }
    }

    private fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun actualizarImagenYInfo() {
        progressDialog.setMessage("Actualizando Imagen...")
        progressDialog.show()

        val filePathAndName = "Perfil_Imagenes/${firebaseAuth.uid}"
        val storageRef = FirebaseStorage.getInstance().getReference(filePathAndName)

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    val downloadImageUrl = uri.toString()
                    actualizarInfoConImagen(downloadImageUrl)
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun actualizarInfoConImagen(imageUrl: String) {
        progressDialog.setMessage("Actualizando Información...")
        progressDialog.show()

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["nombres"] = nombres
        hashMap["interesesViaje"] = descripcion
        hashMap["imagen"] = imageUrl

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Se actualizó su información", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun actualizarInfo() {
        progressDialog.setMessage("Actualizando Información...")
        progressDialog.show()

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["nombres"] = nombres
        hashMap["interesesViaje"] = descripcion

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Se actualizó su información", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cargarInformacion() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = "${snapshot.child("nombres").value}"
                    val descripcion = "${snapshot.child("interesesViaje").value}"
                    val imagen = "${snapshot.child("imagen").value}"

                    binding.txtNombres.setText(nombres)
                    binding.txtDescripcion.setText(descripcion)  // Mostrar interesesViaje

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
                    // Manejo del error
                }
            })
    }
}

