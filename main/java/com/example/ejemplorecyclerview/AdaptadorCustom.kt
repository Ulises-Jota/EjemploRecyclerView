package com.example.ejemplorecyclerview

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class AdaptadorCustom(private var items: ArrayList<Platillo>, var listener: ClickListener, var longClickListener: LongClickListener): RecyclerView.Adapter<AdaptadorCustom.ViewHolder>() {

    var multiSeleccion = false

    var itemsSeleccionados: ArrayList<Int>? = null
    var viewHolder: ViewHolder? = null

    init {
        itemsSeleccionados = ArrayList()
    }

    // Infla el template en la vista y crea el viewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.template_platillo, parent, false)
        return ViewHolder(vista, listener, longClickListener)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position] // Obtiene el elemento actual
        holder.foto?.setImageResource(item.foto) // Mapearle a ese elemento los valores del holder
        holder.nombre?.text = item.nombre // Mapearle a ese elemento los valores del holder
        holder.precio?.text = "%s%s".format("$", item.precio) // "$ ${item?.precio}" // Mapearle a ese elemento los valores del holder
        holder.rating?.rating = item.rating // Mapearle a ese elemento los valores del holder

        if (itemsSeleccionados?.contains(position)!!){
            holder.vista.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.vista.setBackgroundColor(Color.WHITE)
        }
    }

    fun iniciarActionMode(){
        multiSeleccion = true
    }

    fun destruirActionMode(){
        multiSeleccion = false
        itemsSeleccionados?.clear()
        notifyDataSetChanged()
    }

    fun terminarActionMode(){
        // Eliminar elementos seleccionados
        for (item in itemsSeleccionados!!){
            itemsSeleccionados?.remove(item)
        }
        multiSeleccion = false
        notifyDataSetChanged()
    }

    fun seleccionarItem(index: Int) {
        if (multiSeleccion){
            if (itemsSeleccionados?.contains(index)!!){
                itemsSeleccionados?.remove(index)
            } else {
                itemsSeleccionados?.add(index)
            }

            notifyDataSetChanged()
        }
    }

    fun obtenerNumeroElementosSeleccionados(): Int{
        return itemsSeleccionados?.count()!!
    }

    fun eliminarSeleccionados(){
        if (itemsSeleccionados?.count()!! > 0){
            var itemsEliminados = ArrayList<Platillo>()
            for (index in itemsSeleccionados!!){
                itemsEliminados.add(items.get(index))
            }
            items.removeAll(itemsEliminados)
            itemsSeleccionados?.clear()
        }
    }

    // Clase para definir las variables y funciones a usar por el adaptador
    class ViewHolder(itemView: View, listener: ClickListener, longClickListener: LongClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        var vista = itemView
        var foto: ImageView? = null
        var nombre: TextView? = null
        var precio: TextView? = null
        var rating: RatingBar? = null
        var listener: ClickListener? = null
        var longListener: LongClickListener? = null

        init {
            foto = vista.findViewById(R.id.ivFoto)
            nombre = vista.findViewById(R.id.tvNombre)
            precio = vista.findViewById(R.id.tvPrecio)
            rating = vista.findViewById(R.id.rbRating)
            this.listener = listener
            this.longListener = longClickListener
            vista.setOnClickListener(this)
            vista.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            this.listener?.onClick(v!!, adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            this.longListener?.longClick(v!!, adapterPosition)
            return true
        }
    }
}