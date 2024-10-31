package com.MarvinGudiel.viajeapp.Fragmentos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.MarvinGudiel.viajeapp.Adaptadores.AdaptadorChats
import com.MarvinGudiel.viajeapp.Modelos.Chats
import com.MarvinGudiel.viajeapp.R
import com.MarvinGudiel.viajeapp.databinding.FragmentFragmentosChatsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentosChats : Fragment() {

    private lateinit var binding: FragmentFragmentosChatsBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var miUid = ""
    private lateinit var chatsArrayList: ArrayList<Chats>
    private lateinit var adaptadorChats: AdaptadorChats
    private lateinit var mContext: Context
    private lateinit var btnUsuarios: Button // Botón Usuarios

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflamos la vista desde el archivo XML en lugar del binding, para acceder al botón `btnUsuarios`
        val view = inflater.inflate(R.layout.fragment_fragmentos_chats, container, false)

        // Inicializamos `binding` y el botón `btnUsuarios`
        binding = FragmentFragmentosChatsBinding.bind(view)
        btnUsuarios = view.findViewById(R.id.btnUsuarios)

        // Configura el clic del botón para cambiar de fragmento
        btnUsuarios.setOnClickListener {
            val fragment = FragmentUsuarios()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentoFL, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        miUid = firebaseAuth.uid.orEmpty()
        cargarChats()
    }

    private fun cargarChats() {
        chatsArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("chats")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatsArrayList.clear()
                for (ds in snapshot.children) {
                    val chatKey = ds.key.orEmpty()
                    if (chatKey.contains(miUid)) {
                        val modeloChats = Chats()
                        modeloChats.keyChat = chatKey
                        chatsArrayList.add(modeloChats)
                    }
                }

                adaptadorChats = AdaptadorChats(mContext, chatsArrayList)
                binding.chatsRV.adapter = adaptadorChats
            }

            override fun onCancelled(error: DatabaseError) {
                // Muestra un mensaje de error si ocurre algún problema al cargar los chats
                println("Error al cargar chats: ${error.message}")
            }
        })
    }
}



