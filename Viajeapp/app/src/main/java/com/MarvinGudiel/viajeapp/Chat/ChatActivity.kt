package com.MarvinGudiel.viajeapp.Chat

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.MarvinGudiel.viajeapp.Adaptadores.AdaptadorChat
import com.MarvinGudiel.viajeapp.Constantes
import com.MarvinGudiel.viajeapp.Modelos.Chat
import com.MarvinGudiel.viajeapp.R
import com.MarvinGudiel.viajeapp.databinding.ActivityChatBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private var uid = ""

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var miUid = ""

    private var chatRuta = ""
    private var imagenUri: Uri? = null

    private lateinit var adaptadorChat: AdaptadorChat
    private val mensajesArrayList = ArrayList<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        uid = intent.getStringExtra("uid") ?: ""
        miUid = firebaseAuth.uid ?: ""


        chatRuta = Constantes.rutaChat(uid, miUid)
        Log.d("ChatActivity", "Ruta del chat: $chatRuta")

        adaptadorChat = AdaptadorChat(this, mensajesArrayList)
        binding.chatsRV.adapter = adaptadorChat

        binding.adjuntarFAB.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                imagenGaleria()
            } else {
                solicitarPermisoAlmacenamiento.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        binding.IbRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.enviarFAB.setOnClickListener {
            validarMensaje()
        }

        cargarInfo()
        cargarMensajes()
    }

    private fun cargarMensajes() {
        val ref = FirebaseDatabase.getInstance().getReference("chats")
        ref.child(chatRuta)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mensajesArrayList.clear()

                    for (ds: DataSnapshot in snapshot.children) {
                        val chat = ds.getValue(Chat::class.java)
                        if (chat != null) {
                            mensajesArrayList.add(chat)
                        }
                    }

                    adaptadorChat.notifyDataSetChanged()

                    binding.chatsRV.setHasFixedSize(true)
                    var linearLayoutManager = LinearLayoutManager(this@ChatActivity)
                    linearLayoutManager.stackFromEnd = true
                    binding.chatsRV.layoutManager= linearLayoutManager
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, "Error al cargar mensajes", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun validarMensaje() {
        val mensaje = binding.etMensajeChat.text.toString().trim()
        val tiempo = Constantes.obtenerTiempoD()

        if (mensaje.isEmpty()) {
            Toast.makeText(this, "Ingrese un mensaje", Toast.LENGTH_SHORT).show()
        } else {
            enviarMensaje(Constantes.MENSAJE_TIPO_TEXTO, mensaje, tiempo)
        }
    }

    private fun cargarInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = "${snapshot.child("nombres").value}"
                    val imagen = "${snapshot.child("imagen").value}"

                    binding.txtNombreUsuario.text = nombres

                    try {
                        Glide.with(this@ChatActivity)
                            .load(imagen)
                            .placeholder(R.drawable.perfil_usuario)
                            .into(binding.toolbarIv)
                    } catch (e: Exception) {
                        Log.e("ChatActivity", "Error al cargar imagen: ${e.message}")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, "Error al cargar información de usuario", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun imagenGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultadoGaleriaARL.launch(intent)
    }

    private val resultadoGaleriaARL =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) {
                val data = resultado.data
                imagenUri = data?.data
                if (imagenUri != null) {
                    subirImagenstore()
                } else {
                    Toast.makeText(this, "Error al seleccionar imagen", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            }
        }

    private val solicitarPermisoAlmacenamiento =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { esConcedido ->
            if (esConcedido) {
                imagenGaleria()
            } else {
                Toast.makeText(this, "El permiso de almacenamiento no ha sido concedido", Toast.LENGTH_SHORT).show()
            }
        }

    private fun subirImagenstore() {
        progressDialog.setMessage("Subiendo Imagen")
        progressDialog.show()

        val tiempo = Constantes.obtenerTiempoD()
        val nombreRutaImg = "ImagenesChats/$tiempo"
        val storageRef = FirebaseStorage.getInstance().getReference(nombreRutaImg)
        imagenUri?.let {
            storageRef.putFile(it)
                .addOnSuccessListener { taskSnapshot ->
                    val uriTask = taskSnapshot.storage.downloadUrl
                    while (!uriTask.isSuccessful);
                    val urlImagen = uriTask.result.toString()
                    if (uriTask.isSuccessful) {
                        enviarMensaje(Constantes.MENSAJE_TIPO_IMAGEN, urlImagen, tiempo)
                    }
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(this, "No se pudo enviar la imagen: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun enviarMensaje(tipoMensaje: String, mensaje: String, tiempo: Long) {
        progressDialog.setMessage("Enviando Mensaje")
        progressDialog.show()

        val refChat = FirebaseDatabase.getInstance().getReference("chats")
        val keyId = "${refChat.push().key}"
        val hashMap = hashMapOf(
            "idMensaje" to keyId,
            "tipoMensaje" to tipoMensaje,
            "mensaje" to mensaje,
            "emisorUid" to miUid,
            "receptorUid" to uid,
            "tiempo" to tiempo
        )

        refChat.child(chatRuta)
            .child(keyId)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                binding.etMensajeChat.setText("")
            }.addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "No se pudo enviar el mensaje: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
