package com.basics.fooddeliveryapp.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basics.fooddeliveryapp.Adapter.RestaurantAdapter
import com.basics.fooddeliveryapp.R
import com.basics.fooddeliveryapp.model.Restaurant
import com.basics.fooddeliveryapp.util.ConectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class HomeFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RestaurantAdapter
    lateinit var restaurantList: ArrayList<Restaurant>
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    var priceComparator = Comparator<Restaurant> { rest1, rest2 ->
        if (rest1.costForOne.trim().compareTo(rest2.costForOne.trim(), true) == 0) {
            rest1.name.compareTo(rest2.name, true)
        } else {
            rest1.costForOne.trim().compareTo(rest2.costForOne.trim(), true)
        }
    }


    var ratingComparator = Comparator<Restaurant> { rest1, rest2 ->
        if (rest1.rating.compareTo(rest2.rating, true) == 0) {
            rest1.name.compareTo(rest2.name, true)
        } else {
            rest1.rating.compareTo(rest2.rating, true)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true)
        recyclerView = view.findViewById(R.id.restaurantsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity as Context)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        restaurantList = ArrayList()

        if (ConectionManager().internetConnection(activity as Context)) {


            val queue = Volley.newRequestQueue(activity as Context)

            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"


            val jsonRequest = object : JsonObjectRequest(Method.GET, url, null, Response.Listener {


                val jsonObject = it.getJSONObject("data")

                val success = jsonObject.getBoolean("success")

                if (success) {

                    progressBar.visibility = View.GONE
                    progressLayout.visibility = View.GONE
                    val data = jsonObject.getJSONArray("data")


                    for (i in 0 until data.length()) {

                        val restaurantJsonObject = data.getJSONObject(i)

                        val restaurantObject = Restaurant(
                            restaurantJsonObject.getString("id").toString().trim(),
                            restaurantJsonObject.getString("name").toString().trim(),
                            restaurantJsonObject.getString("rating").toString().trim(),
                            restaurantJsonObject.getString("cost_for_one").toString().trim(),
                            restaurantJsonObject.getString("image_url").toString().trim()
                        )
                        restaurantList.add(restaurantObject)


                    }
                    recyclerAdapter = RestaurantAdapter(activity as Context, restaurantList)
                    recyclerView.layoutManager = LinearLayoutManager(activity)
                    recyclerView.adapter = recyclerAdapter


                } else {
                    Toast.makeText(
                        activity as Context,
                        "Please!try after some time",
                        Toast.LENGTH_SHORT
                    ).show()

                }


            }, Response.ErrorListener {
                Toast.makeText(
                    activity as Context,
                    "Please!try after some time",
                    Toast.LENGTH_SHORT
                ).show()

            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val header = HashMap<String, String>()

                    header["Content-type"] = "application/json"
                    header["token"] = "ec66f7766ff4a4"


                    return header
                }
            }



            queue.add(jsonRequest)


        } else{
            var alertDialog = AlertDialog.Builder(activity as Context)
            alertDialog.setTitle("Network Connection")
            alertDialog.setMessage("Internet is not Connected")

            alertDialog.setPositiveButton("open setting") { text, listener ->

                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity!!.finish()

            }
            alertDialog.setNegativeButton("exit") { text, listener ->
                activity!!.finishAffinity()
            }

            alertDialog.create()
            alertDialog.show()
        }






        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId

        if (id == R.id.sort) {
            val choiceItems = arrayOf("Cost(Low to High)", "Cost(High to Low)", "Rating")
            var post = 0
            var alertDialog = AlertDialog.Builder(activity as Context)
            alertDialog.setTitle("Sort By?")

            alertDialog.setSingleChoiceItems(
                choiceItems, 0
            ) { dialog, which -> post = which }

            alertDialog.setPositiveButton("ok") { _, _ ->
                when (post) {
                    0 -> {
                        Collections.sort(restaurantList, priceComparator)
                    }
                    1 -> {
                        Collections.sort(restaurantList, priceComparator)
                        restaurantList.reverse()
                    }
                    2 -> {
                        Collections.sort(restaurantList, ratingComparator)
                        restaurantList.reverse()

                    }
                }
                recyclerAdapter.notifyDataSetChanged()

            }

            alertDialog.setNegativeButton("cancel") { _, _ ->

            }

            alertDialog.create()
            alertDialog.show()


        }
        return super.onOptionsItemSelected(item)

    }

}
