package com.basics.fooddeliveryapp.Adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.basics.fooddeliveryapp.R
import com.basics.fooddeliveryapp.model.RestaurantMenu

class RestaurantMenuAdapter(var context:Context,var list:ArrayList<RestaurantMenu>):RecyclerView.Adapter<RestaurantMenuAdapter.MenuViewHolder>() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var itemAddListener:setOnItemAddListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_menu_recycler,parent,false)


        return MenuViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun onItemAddListener(itemAddListener:setOnItemAddListener){
        this.itemAddListener = itemAddListener
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val name  = list[position].name
        val price = list[position].costForOne
        val id = list[position].menuId

        sharedPreferences = context.getSharedPreferences("Cart Preference",Context.MODE_PRIVATE)

        holder.txtItemCount.text = "${position+1}"
        holder.txtItemName.text = name
        holder.txtItemPrice.text = "Rs.$price"

        holder.btnAddItem.setOnClickListener {

            val alreadyInCart = sharedPreferences.getString(id,null)

            if(alreadyInCart==null){
                val set = "$name \n $price"
                sharedPreferences.edit().putString(id,set).apply()
                holder.btnAddItem.text = "Remove"
                itemAddListener.onClick(true)
            }
            else{
                sharedPreferences.edit().remove(id).apply()
                holder.btnAddItem.text = "Add"

                if (sharedPreferences.all.isNotEmpty())
                    itemAddListener.onClick(true)
                else
                    itemAddListener.onClick(false)
            }

        }
    }
    class MenuViewHolder(view:View):RecyclerView.ViewHolder(view){

        var txtItemCount:TextView = view.findViewById(R.id.txtItemId)
        var txtItemName:TextView = view.findViewById(R.id.txtItemName)
        var txtItemPrice:TextView  = view.findViewById(R.id.txtItemPrice)
        var btnAddItem: Button = view.findViewById(R.id.btnAddItem)


    }

    interface setOnItemAddListener{
        fun onClick(isClicked:Boolean)
    }
}