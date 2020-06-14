package com.basics.fooddeliveryapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.basics.fooddeliveryapp.R
import com.basics.fooddeliveryapp.model.FoodItems

class OrderHistoryChildAdapter(val context:Context,val list:List<FoodItems>):RecyclerView.Adapter<OrderHistoryChildAdapter.OrderChildViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderChildViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_order_histroy_child_single_row,parent,false)
        return OrderChildViewHolder(view)
    }

    override fun getItemCount(): Int {
       return  list.size
    }

    override fun onBindViewHolder(holder: OrderChildViewHolder, position: Int) {
        val itemName = list[position].itemName
        val price = list[position].cost

        holder.txtItemName.text = itemName
        holder.txtItemPrice.text = price
    }
    class OrderChildViewHolder(val view:View):RecyclerView.ViewHolder(view){
        val img:ImageView = view.findViewById(R.id.imgRemoveItem)
        val txtItemName: TextView = view.findViewById(R.id.txtItemName)
        val txtItemPrice:TextView = view.findViewById(R.id.txtPrice)
    }
}