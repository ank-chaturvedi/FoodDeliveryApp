package com.basics.fooddeliveryapp.Fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.basics.fooddeliveryapp.Adapter.FavouriteAdapter
import com.basics.fooddeliveryapp.R
import com.basics.fooddeliveryapp.database.DatabseEntity
import com.basics.fooddeliveryapp.database.RestaurantEntity


class FavouriteRestaurantsFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var adapter: FavouriteAdapter
    var list: ArrayList<RestaurantEntity> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourite_rastaurants, container, false)

        recyclerView = view.findViewById(R.id.favouriteRecycler)
        layoutManager = LinearLayoutManager(activity as Context)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)


        val dbAsyncTask = DbAsyncTask(activity as Context).execute()

        val restList = dbAsyncTask.get()
        adapter = FavouriteAdapter(activity as Context, arrayListOf<RestaurantEntity>())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        if (restList != null) {
            list.addAll(restList)
            adapter = FavouriteAdapter(activity as Context, list)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE
            progressLayout.visibility = View.GONE
        }




        return view
    }

    class DbAsyncTask(val context: Context) : AsyncTask
    <Void, Void, List<RestaurantEntity>>() {

        val db =
            Room.databaseBuilder(context, DatabseEntity::class.java, "fav-restaurant-db").build()

        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {


            val result = db.restaurantDao().getAll()
            db.close()
            return result


        }

    }

}
