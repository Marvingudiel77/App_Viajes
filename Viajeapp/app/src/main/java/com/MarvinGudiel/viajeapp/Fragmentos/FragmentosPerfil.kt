package com.MarvinGudiel.viajeapp.Fragmentos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.MarvinGudiel.viajeapp.Constantes
import com.MarvinGudiel.viajeapp.EditarInformacion
import com.MarvinGudiel.viajeapp.OpcionesLoginActivity
import com.MarvinGudiel.viajeapp.R
import com.MarvinGudiel.viajeapp.databinding.FragmentFragmentosPerfilBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentosPerfil : Fragment() {

    private lateinit var binding : FragmentFragmentosPerfilBinding
    private lateinit var mContext : Context
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentFragmentosPerfilBinding.inflate(layoutInflater, container, false )
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        cargarInformacion()

        binding.btnActualizarInfo.setOnClickListener {
            startActivity(Intent(mContext, EditarInformacion::class.java))
        }

        binding.btnCerrarSesion.setOnClickListener { firebaseAuth.signOut()
        startActivity(Intent(mContext, OpcionesLoginActivity::class.java))
            activity?.finishAffinity()
        }
    }

    private fun cargarInformacion() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = "${snapshot.child("nombres").value}"
                    val email = "${snapshot.child("email").value}"
                    val proveedor = "${snapshot.child("proveedor").value}"
                    var tiempoR = "${snapshot.child("tiempoR").value}"
                    val imagen = "${snapshot.child("imagen").value}"

                    if(tiempoR == "null"){
                        tiempoR = "0"
                    }

                    val fecha = Constantes.formatoFecha(tiempoR.toLong())

                    binding.tvNombres.text = nombres
                    binding.tvEmail.text = email
                    binding.tvProveedor.text = proveedor
                    binding.tvTRegistro.text = fecha

                    try {
                        Glide.with(mContext).load(imagen).placeholder(R.drawable.img_perfil).into(binding.ivPerfil)
                    }catch (e:Exception){
                        Toast.makeText(mContext, "${e.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}
