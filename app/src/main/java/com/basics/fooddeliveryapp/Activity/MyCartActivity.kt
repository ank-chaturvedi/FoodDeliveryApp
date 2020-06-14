package com.basics.fooddeliveryapp.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basics.fooddeliveryapp.Adapter.CartAdapter
import com.basics.fooddeliveryapp.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MyCartActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var cartAdapter: CartAdapter
    lateinit var txtRestName: TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var userSharedPreferences: SharedPreferences
    lateinit var btnPlaceOrder: Button
    lateinit var toolbar: Toolbar
    private val foodId = arrayListOf<String>()

    private val foodList = arrayListOf<HashMap<String, String>>()

    var totalCost = 0
    private var userId: String? = null
    private lateinit var restaurantId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)

        txtRestName = findViewById(R.id.txtRestName)
        recyclerView = findViewById(R.id.cartRecyclerView)
        layoutManager = LinearLayoutManager(this)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        toolbar = findViewById(R.id.toolbar)
        setUpToolbar()



        sharedPreferences = getSharedPreferences("Cart Preference", Context.MODE_PRIVATE)
        userSharedPreferences = getSharedPreferences("Login Preference", Context.MODE_PRIVATE)
        userId = userSharedPreferences.getString("user_id", null)


        val allEntry = sharedPreferences.all
        if (intent != null) {
            txtRestName.text = intent.getStringExtra("restaurant")
            restaurantId = intent.getStringExtra("id")
        }


        allEntry.forEach {
            foodId.add(it.key)
        }

        for (i in 0 until foodId.size) {
            val values = sharedPreferences.getString(foodId[i], null)?.split("\n")

            if (values != null) {
                val name = values[0].trim()
                val price = values[1].trim()

                val map = HashMap<String, String>()
                map["name"] = name
                map["price"] = price
                totalCost += try {
                    price.toInt()
                } catch (e: ClassCastException) {
                    0
                }


                foodList.add(map)
                cartAdapter = CartAdapter(this, foodList)

                recyclerView.adapter = cartAdapter
                recyclerView.layoutManager = layoutManager
                recyclerView.addItemDecoration(
                    DividerItemDecoration(
                        recyclerView.context,
                        DividerItemDecoration.VERTICAL
                    )
                )


            }
        }

        btnPlaceOrder.text = "Place Order($totalCost)"


        btnPlaceOrder.setOnClickListener {

            val queue = Volley.newRequestQueue(this)

            val url = "http://13.235.250.119/v2/place_order/fetch_result/"


            val jsonObject = JSONObject()
            jsonObject.put("user_id", userId)
            jsonObject.put("restaurant_id", restaurantId)
            jsonObject.put("total_cost", totalCost.toString())

            val jsonArray = JSONArray()


            foodId.forEach {
                val foodJsonObject = JSONObject()

                foodJsonObject.put("food_item_id", it)

                jsonArray.put(foodJsonObject)
            }


            jsonObject.put("food", jsonArray)


            val jsonRequest =
                object : JsonObjectRequest(Method.POST, url, jsonObject, Response.Listener {
                    try {
                        val cartObject = it.getJSONObject("data")

                        val success = cartObject.getBoolean("success")
                        if (success) {
                            val intent = Intent(this, OrderPlacedActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "enter valid details", Toast.LENGTH_SHORT).show()

                        }
                    } catch (e: JSONException) {
                        Toast.makeText(this, "json exception", Toast.LENGTH_SHORT).show()

                    }
                }, Response.ErrorListener {
                    Toast.makeText(this, "some error occured", Toast.LENGTH_SHORT).show()


                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "ec66f7766ff4a4"
                        return headers
                    }
                }



            queue.add(jsonRequest)
        }


    }


    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Cart"
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
