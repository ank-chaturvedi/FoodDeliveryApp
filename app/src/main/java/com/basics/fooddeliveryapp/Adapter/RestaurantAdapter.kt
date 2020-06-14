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
import com.basics.fooddeliveryapp.model.Restaurant
import com.squareup.picasso.Picasso

class RestaurantAdapter(val context: Context,val list:ArrayList<Restaurant>):RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_restaurant_recycler,parent,false)
        return RestaurantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val foodName = list[position].name
        val rating = list[position].rating
        val costPerPerson = list[position].costForOne
        val restaurantImage = list[position].image
        val id = list[position].id


        holder.txtFoodName.text = foodName
        holder.txtRating.text = rating
        holder.txtCostPerPerson.text = "Rs. $costPerPerson For One"
        Picasso.get().load(restaurantImage).error(R.drawable.ic_launcher_foreground).into(holder.imgRestaurant)



        holder.llContent.setOnClickListener {
            val intent = Intent(context,
                RestaurantMenuActivity::class.java)

            intent.putExtra("restaurant_id",list[position].id)
            intent.putExtra("restaurant",foodName)

            context.startActivity(intent)
        }

        val checkFav = DbAsyncTask(context,list[position],1,id).execute()
        val isFav = checkFav.get()

        if(isFav){
            holder.imgFavourite.setImageResource(R.drawable.ic_favorite)
        }else{
            holder.imgFavourite.setImageResource(R.drawable.favorite_unchecked)
        }

        holder.imgFavourite.setOnClickListener {

            val checkFav = DbAsyncTask(context,list[position],1,id).execute()
            val isFav = checkFav.get()

            if(isFav){
                holder.imgFavourite.setImageResource(R.drawable.favorite_unchecked)


                val checkDeleted = DbAsyncTask(context,list[position],3,id).execute()

                val isDeleted = checkDeleted.get()

                if(isDeleted){
//                    holder.imgFavourite.setImageResource(R.drawable.favorite_unchecked)
               }


            }else{
                holder.imgFavourite.setImageResource(R.drawable.ic_favorite)

                val checkInserted = DbAsyncTask(context,list[position],2,id).execute()

                val isInserted = checkInserted.get()

                if(isInserted){
//                   holder.imgFavourite.setImageResource(R.drawable.ic_favorite)

                }
            }

        }
    }



    class RestaurantViewHolder(view:View):RecyclerView.ViewHolder(view){

        val imgRestaurant:ImageView = view.findViewById(R.id.imgRestaurant)
        val txtFoodName:TextView = view.findViewById(R.id.txtFoodName)
        val txtCostPerPerson:TextView = view.findViewById(R.id.txtCostPerPerson)
        val imgFavourite:ImageView = view.findViewById(R.id.imgFavourite)
        val txtRating:TextView = view.findViewById(R.id.txtRating)
        val llContent: LinearLayout  =  view.findViewById(R.id.llContent)


    }


    class DbAsyncTask(val context: Context, val restEntity: Restaurant, val mode: Int,val id:String) :
        AsyncTask<
                Void, Void, Boolean>() {

        val db =
            Room.databaseBuilder(context, DatabseEntity::class.java, "fav-restaurant-db").build()


        /*
            Mode1 -> check db if the book is favourite or not
            Mode2 -> Save the book into DB as favourite
            Mode3 -> Remove the favourite book
            Mode4 -> check by id
         */

        override fun doInBackground(vararg params: Void?): Boolean {


            when (mode){

                1 ->{
                    val rest: RestaurantEntity?  = db.restaurantDao().getRestById(restEntity.id.toString())
                    db.close()
                    return rest !=null
                }

                2->{
                    val entity = RestaurantEntity(
                        restEntity.id,
                        restEntity.name,
                        restEntity.costForOne,
                        restEntity.rating,
                        restEntity.image

                    )

                    db.restaurantDao().insert(entity)
                    db.close()
                    return true
                }

                3 ->{

                    val entity = RestaurantEntity(
                        restEntity.id,
                        restEntity.name,
                        restEntity.costForOne,
                        restEntity.rating,
                        restEntity.image

                    )

                    db.restaurantDao().delete(entity)
                    db.close()
                    return true

                }

                4 ->{
                    val rest: RestaurantEntity?  = db.restaurantDao().getRestById(restEntity.id.toString())
                    db.close()

                    return rest!=null && id==rest.id

                }



            }
            db.close()
            return false

        }

    }
}