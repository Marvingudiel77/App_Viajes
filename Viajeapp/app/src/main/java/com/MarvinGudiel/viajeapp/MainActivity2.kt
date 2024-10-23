package com.MarvinGudiel.viajeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.MarvinGudiel.viajeapp.ui.fragmentlistapublicaciones.FragmentListapublicaciones

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FragmentListapublicaciones.newInstance())
                .commitNow()
        }
    }
}