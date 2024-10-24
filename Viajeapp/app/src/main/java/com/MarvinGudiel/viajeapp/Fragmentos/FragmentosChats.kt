package com.MarvinGudiel.viajeapp.Fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.MarvinGudiel.viajeapp.R
import com.MarvinGudiel.viajeapp.Fragmentos.FragmentUsuarios

class FragmentosChats : Fragment() {

    private lateinit var btnUsuarios: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fragmentos_chats, container, false)

        btnUsuarios = view.findViewById(R.id.btnUsuarios)
        btnUsuarios.setOnClickListener {
            val fragment = FragmentUsuarios()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentoFL, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}


