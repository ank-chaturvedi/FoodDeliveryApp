package com.basics.fooddeliveryapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.basics.fooddeliveryapp.R

class CartAdapter(val context: Context,val list:List<HashMap<String,String>>):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_cart_recycler,parent,false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val values = list[position]
        val name = values["name"]
        val price = values["price"]

        holder.name.text = name
        holder.price.text = price


    }


    class CartViewHolder(view: View):RecyclerView.ViewHolder(view){
        val name:TextView = view.findViewById(R.id.txtItemName)
        val price:TextView = view.findViewById(R.id.txtPrice)
    }
}