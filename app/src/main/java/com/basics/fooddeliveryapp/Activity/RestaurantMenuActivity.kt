package com.basics.fooddeliveryapp.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basics.fooddeliveryapp.Adapter.RestaurantMenuAdapter
import com.basics.fooddeliveryapp.R
import com.basics.fooddeliveryapp.model.RestaurantMenu
import com.basics.fooddeliveryapp.util.ConectionManager
import org.json.JSONException

class RestaurantMenuActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var restaurantMenuAdapter: RestaurantMenuAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var toolbar: Toolbar
    lateinit var btnPlaceOrder: Button
    lateinit var sharedPreferences: SharedPreferences
    var restaurantId: String = "100"
    var menuListInfo = arrayListOf<RestaurantMenu>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)

        recyclerView = findViewById(R.id.menuRecyclerView)
        layoutManager = LinearLayoutManager(this)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)
        btnPlaceOrder = findViewById(R.id.placeOrder)
        toolbar = findViewById(R.id.toolbar)

        progressLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        sharedPreferences = getSharedPreferences("Cart Preference", Context.MODE_PRIVATE)

        var restaurantName = "null"
        btnPlaceOrder.visibility = View.GONE
        setUpToolbar()


        if (sharedPreferences.all.isNotEmpty()) {
            sharedPreferences.edit().clear().apply()
        }




        if (intent != null) {
            restaurantId = intent.getStringExtra("restaurant_id")
            restaurantName = intent.getStringExtra("restaurant")
        }




        if (restaurantId == "100") {

        } else {


            if (ConectionManager().internetConnection(this)) {

                val queue = Volley.newRequestQueue(this)

                val url = "http://13.235.250.119/v2/restaurants/fetch_result/${restaurantId}"


                var jsonRequest =
                    object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

                        try {
                            progressLayout.visibility = View.GONE
                            progressBar.visibility = View.GONE
                            val jsonObject = it.getJSONObject("data")

                            val success = jsonObject.getBoolean("success")
                            if (success) {

                                val data = jsonObject.getJSONArray("data")

                                for (i in 0 until data.length()) {

                                    val menuDetails = data.getJSONObject(i)


                                    val restaurantMenu = RestaurantMenu(
                                        menuDetails.getString("id").toString().trim(),
                                        menuDetails.getString("name").toString().trim(),
                                        menuDetails.getString("cost_for_one").toString().trim(),
                                        menuDetails.getString("restaurant_id").toString().trim()

                                    )

                                    menuListInfo.add(restaurantMenu)

                                    recyclerView.layoutManager = layoutManager


                                    restaurantMenuAdapter =
                                        RestaurantMenuAdapter(this, menuListInfo)
                                    recyclerView.adapter = restaurantMenuAdapter
                                    restaurantMenuAdapter.onItemAddListener(object :
                                        RestaurantMenuAdapter.setOnItemAddListener {
                                        override fun onClick(isClicked: Boolean) {
                                            if (isClicked) {
                                                btnPlaceOrder.visibility = View.VISIBLE
                                            } else {
                                                btnPlaceOrder.visibility = View.GONE
                                            }
                                        }

                                    })


                                }


                            }
                        } catch (e: JSONException) {
                            Toast.makeText(this, "Please! try after some time", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }, Response.ErrorListener {

                        Toast.makeText(this, "Please! try after some time", Toast.LENGTH_SHORT)
                            .show()


                    }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()

                            headers["Content-type"] = "application/json"
                            headers["token"] = "ec66f7766ff4a4"

                            return headers
                        }
                    }

                queue.add(jsonRequest)


            } else {
                //connect to internet
                Toast.makeText(this, "Please! connect to internet", Toast.LENGTH_SHORT).show()

            }


        }









        btnPlaceOrder.setOnClickListener {
            val intent = Intent(this, MyCartActivity::class.java)
            intent.putExtra("restaurant", restaurantName)
            intent.putExtra("id", restaurantId)
            startActivity(intent)

        }

    }


    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Menu"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        val id = item.itemId

        if (id == android.R.id.home) {
            finish()
        }


        return super.onOptionsItemSelected(item)
    }


}
