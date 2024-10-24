package com.MarvinGudiel.viajeapp.Chat

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.MarvinGudiel.viajeapp.Constantes
import com.MarvinGudiel.viajeapp.R
import com.MarvinGudiel.viajeapp.databinding.ActivityChatBinding
import com.MarvinGudiel.viajeapp.databinding.FragmentUsuariosBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private var uid = ""

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var miUid = ""

    private var chatRuta = ""
    private var imagenUri: Uri?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("espere porfavor")
        progressDialog.setCanceledOnTouchOutside(false)

        uid = intent.getStringExtra("uid")!!

        miUid = firebaseAuth.uid!!

        chatRuta = Constantes.rutaChat(uid, miUid)

        cargarInfo()
    }

    private fun cargarInfo(){
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = "${snapshot.child("nombres").value}"
                    val imagen = "${snapshot.child("imagen").value}"

                    binding.txtNombreUsuario.text = nombres

                    try {
                        Glide.with(this@ChatActivity)
                            .load(imagen)
                            .placeholder(R.drawable.perfil_usuario)
                            .into(binding.toolbarIv)
                    }catch (e : Exception){

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }
}