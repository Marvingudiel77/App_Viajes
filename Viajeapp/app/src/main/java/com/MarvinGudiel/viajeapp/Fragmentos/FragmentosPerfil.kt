package com.MarvinGudiel.viajeapp.Fragmentos

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.MarvinGudiel.viajeapp.OpcionesLoginActivity
import com.MarvinGudiel.viajeapp.R
import com.MarvinGudiel.viajeapp.databinding.FragmentFragmentosPerfilBinding
import com.google.firebase.auth.FirebaseAuth


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

        binding.btnCerrarSesion.setOnClickListener { firebaseAuth.signOut()
        startActivity(Intent(mContext, OpcionesLoginActivity::class.java))
            activity?.finishAffinity()
        }
    }
}
