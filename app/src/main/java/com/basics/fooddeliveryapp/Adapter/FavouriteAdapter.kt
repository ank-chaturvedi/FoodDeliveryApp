package com.basics.fooddeliveryapp.Adapter


import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.basics.fooddeliveryapp.R
import com.basics.fooddeliveryapp.Activity.RestaurantMenuActivity
import com.basics.fooddeliveryapp.database.DatabseEntity
import com.basics.fooddeliveryapp.database.RestaurantEntity
import com.squareup.picasso.Picasso

class FavouriteAdapter(val context: Context, val list:ArrayList<RestaurantEntity>) :
    RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favourite_single_row, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val foodName = list[position].name
        val rating = list[position].rating
        val costPerPerson = list[position].cost
        val restaurantImage = list[position].image


        holder.txtFoodName.text = foodName
        holder.txtRating.text = rating
        holder.txtCostPerPerson.text = "Rs. $costPerPerson For One"
        Picasso.get().load(restaurantImage).error(R.drawable.ic_launcher_foreground)
            .into(holder.imgRestaurant)



        holder.llContent.setOnClickListener {
            val intent = Intent(context, RestaurantMenuActivity::class.java)

            intent.putExtra("restaurant_id", list[position].id)
            intent.putExtra("restaurant", foodName)

            context.startActivity(intent)
        }


        holder.imgFavourite.setOnClickListener {
            val delete = DbAsyncTask(context,list[position]).execute()

            val isDeleted = delete.get()

            if(isDeleted){
                list.removeAt(position)
                notifyDataSetChanged()
            }

        }
    }


    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imgRestaurant: ImageView = view.findViewById(R.id.imgRestaurant)
        val txtFoodName: TextView = view.findViewById(R.id.txtFoodName)
        val txtCostPerPerson: TextView = view.findViewById(R.id.txtCostPerPerson)
        val imgFavourite: ImageView = view.findViewById(R.id.imgFavourite)
        val txtRating: TextView = view.findViewById(R.id.txtRating)
        val llContent: LinearLayout = view.findViewById(R.id.llContent)


    }


    class DbAsyncTask(val context: Context, val restEntity: RestaurantEntity) :
        AsyncTask<
                Void, Void, Boolean>() {

        val db =
            Room.databaseBuilder(context, DatabseEntity::class.java, "fav-restaurant-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {



            db.restaurantDao().delete(restEntity)

            return true
        }


    }







}