package com.basics.fooddeliveryapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basics.fooddeliveryapp.R
import com.basics.fooddeliveryapp.model.OrderHistorySection

class OrderHistoryAdapter(val context:Context,val list:ArrayList<OrderHistorySection>):RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_order_history_parent,parent,false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val restName = list[position].restName
        val orderPlacedAt = list[position].orderPlacedAt
        val foodItemList = list[position].list
        val childAdapter =  OrderHistoryChildAdapter(context,foodItemList)

        holder.txtRestName.text = restName
        holder.txtOrderPlacedAt.text = orderPlacedAt
        holder.childRecyclerView.layoutManager = LinearLayoutManager(context)
        holder.childRecyclerView.adapter = childAdapter
    }


    class OrderViewHolder(val view: View):RecyclerView.ViewHolder(view){
        val txtRestName:TextView = view.findViewById(R.id.txtRestaurantName)
        val txtOrderPlacedAt:TextView = view.findViewById(R.id.txtOrderPlacedAt)
        val childRecyclerView:RecyclerView = view.findViewById(R.id.childRecycler)

    }
}