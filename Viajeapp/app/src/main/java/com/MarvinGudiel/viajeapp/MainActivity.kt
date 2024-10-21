package com.MarvinGudiel.viajeapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.MarvinGudiel.viajeapp.Fragmentos.FragmentosChats
import com.MarvinGudiel.viajeapp.Fragmentos.FragmentosPerfil
import com.MarvinGudiel.viajeapp.Fragmentos.FragmentosPublicaciones
import com.MarvinGudiel.viajeapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        firebaseAuth = FirebaseAuth.getInstance()

        if(firebaseAuth.currentUser == null){
            irOpcionesLogin()
        }

        //fragmento por defecto
        verFragmentoPerfil()

        enableEdgeToEdge()
        setContentView(binding.root)
        binding.bottomNV.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.item_perfil->{
                    //visualizar fragmento perfil
                    verFragmentoPerfil()
                    true
                }
                R.id.item_chats->{
                    //visualizar fragmentos usuarios
                    verFragmentoChats()
                    true
                }
                R.id.item_publicaciones->{
                    //visualizar fragmentos usuarios
                    verFragmentoPublicaciones()
                    true
                }
                else -> {
                    false
                }
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun irOpcionesLogin() {
        startActivity(Intent(application, OpcionesLoginActivity::class.java))
        finishAffinity()
    }


    private fun verFragmentoPerfil(){
        binding.tvTitulo.text = "PERFIL"

        val fragment = FragmentosPerfil()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentoFL.id, fragment, "Fragment Perfil")
        fragmentTransaction.commit()
    }
    private fun verFragmentoChats(){
        binding.tvTitulo.text = "CHATS"

        val fragment = FragmentosChats()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentoFL.id, fragment, "Fragment Chats")
        fragmentTransaction.commit()
    }
    private fun verFragmentoPublicaciones(){
        binding.tvTitulo.text = "PUBLICACIONES"

        val fragment = FragmentosPublicaciones()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentoFL.id, fragment, "Fragment Publicaciones")
        fragmentTransaction.commit()
    }


}