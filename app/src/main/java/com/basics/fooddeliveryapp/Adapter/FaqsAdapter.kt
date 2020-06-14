package com.basics.fooddeliveryapp.Adapter

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.basics.fooddeliveryapp.R
import com.basics.fooddeliveryapp.model.FrequentQuestion

class FaqsAdapter(var context:Context,var list:ArrayList<FrequentQuestion>):RecyclerView.Adapter<FaqsAdapter.FaqsViewHolder>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_faqs_recycler,parent,false)

        return FaqsViewHolder(view)
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FaqsViewHolder, position: Int) {
        val question = list[position].question
        val answer = list[position].answer

        holder.txtQuestion.text  = question
        holder.txtAnswer.text = answer
    }

    class FaqsViewHolder(view:View):RecyclerView.ViewHolder(view){

        val txtQuestion:TextView = view.findViewById(R.id.txtQuesiton)
        val txtAnswer:TextView = view.findViewById(R.id.txtAnswer)

    }
}