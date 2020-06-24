package com.example.ejemplorecyclerview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    var lista: RecyclerView? = null
    var adaptador: RecyclerView.Adapter<AdaptadorCustom.ViewHolder>? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    var isActionMode = false
    var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val platillos = ArrayList<Platillo>()
        val swipeToRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh)

        platillos.add(Platillo("Platillo 1", 250.0, 3.5F, R.drawable.p1))
        platillos.add(Platillo("Platillo 2", 280.0, 4.5F, R.drawable.p2))
        platillos.add(Platillo("Platillo 3", 140.0, 5.0F, R.drawable.p3))
        platillos.add(Platillo("Platillo 4", 220.0, 2.5F, R.drawable.p4))
        platillos.add(Platillo("Platillo 5", 210.0, 3.0F, R.drawable.p5))
        platillos.add(Platillo("Platillo 6", 300.0, 3.8F, R.drawable.p6))
        platillos.add(Platillo("Platillo 7", 350.0, 1.5F, R.drawable.p7))
        platillos.add(Platillo("Platillo 8", 285.0, 2.0F, R.drawable.p8))
        platillos.add(Platillo("Platillo 9", 370.0, 4.0F, R.drawable.p9))
        platillos.add(Platillo("Platillo 10", 250.0, 2.7F, R.drawable.p10))

        lista = findViewById(R.id.lista)
        lista?.setHasFixedSize(true)

        layoutManager = LinearLayoutManager(this)
        lista?.layoutManager = layoutManager

        val callback = object: androidx.appcompat.view.ActionMode.Callback {

            override fun onActionItemClicked(mode: androidx.appcompat.view.ActionMode?, item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.iEliminar -> {
                        // Toast.makeText(applicationContext, "Eliminar objetos", Toast.LENGTH_SHORT).show()
                        (adaptador as AdaptadorCustom).eliminarSeleccionados()
                    }
                    else -> {return true}
                }

                (adaptador as AdaptadorCustom).terminarActionMode()
                mode?.finish()
                isActionMode = false

                return true
            }

            @SuppressLint("ResourceType")
            override fun onCreateActionMode(mode: androidx.appcompat.view.ActionMode?, menu: Menu?): Boolean {
                // Inicializar Action Mode
                (adaptador as AdaptadorCustom).iniciarActionMode()
                actionMode = mode
                // Inflar menu
                menuInflater.inflate(R.layout.menu_contextual, menu!!)
                return true
            }

            override fun onPrepareActionMode(mode: androidx.appcompat.view.ActionMode?, menu: Menu?): Boolean {
                mode?.title = "0 seleccionados"
                return false
            }

            override fun onDestroyActionMode(mode: androidx.appcompat.view.ActionMode?) {
                // Destruir el Action Mode
                (adaptador as AdaptadorCustom).destruirActionMode()
                isActionMode = false
            }
        }

        adaptador = AdaptadorCustom(platillos, object: ClickListener{
            override fun onClick(vista: View, index: Int) {
                Toast.makeText(applicationContext, platillos[index].nombre, Toast.LENGTH_SHORT).show()
            }

        }, object: LongClickListener{
            override fun longClick(vista: View, index: Int) {
                // Log.d("LONG_CLICK", "Prueba...")
                if (!isActionMode){
                    startSupportActionMode(callback)
                    isActionMode = true
                    (adaptador as AdaptadorCustom).seleccionarItem(index)
                } else {
                    // Hacer selecciones o deselecciones
                    (adaptador as AdaptadorCustom).seleccionarItem(index)
                }

                actionMode?.title = "${(adaptador as AdaptadorCustom).obtenerNumeroElementosSeleccionados()} seleccionados"
            }
        })

        lista?.adapter = adaptador

        swipeToRefresh.setOnRefreshListener {
            // Log.d("SWIPE_TO_REFRESH", "La información se está actualizando...")

            // Simular que se refresca (tarda un tiempo y cambian los datos)
            for (i in 1..100000000){
            }
            swipeToRefresh.isRefreshing = false
            platillos.add(Platillo("Luego de actualizar", 300.0, 3.8F, R.drawable.p6))
            adaptador?.notifyDataSetChanged()
        }
    }
}
