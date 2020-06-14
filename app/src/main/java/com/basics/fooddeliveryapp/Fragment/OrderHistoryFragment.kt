package com.basics.fooddeliveryapp.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basics.fooddeliveryapp.Adapter.OrderHistoryAdapter
import com.basics.fooddeliveryapp.R
import com.basics.fooddeliveryapp.model.FoodItems
import com.basics.fooddeliveryapp.model.OrderHistorySection
import org.json.JSONException


class OrderHistoryFragment : Fragment() {
    lateinit var orderHistoryRecyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var orderHistoryAdapter: OrderHistoryAdapter
    lateinit var userDetailsPreferences: SharedPreferences
    lateinit var progressLayout :RelativeLayout
    lateinit var progressBar: ProgressBar
     var orderHistoryList = arrayListOf<OrderHistorySection>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)

        userDetailsPreferences =
            (activity as Context).getSharedPreferences("Login Preference", Context.MODE_PRIVATE)

        orderHistoryRecyclerView = view.findViewById(R.id.orderHistoryRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)



        val userId = userDetailsPreferences.getString("user_id", null)

        if (userId == null) {
            Toast.makeText(activity as Context, "Some error occured", Toast.LENGTH_SHORT).show()
        } else {

            val queue = Volley.newRequestQueue(activity as Context)

            val url = "http://13.235.250.119/v2/orders/fetch_result/${userId}"

            val jsonRequest = object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

                    val jsonObject = it.getJSONObject("data")

                    val success = jsonObject.getBoolean("success")

                        if (success) {
                            progressLayout.visibility = View.GONE
                            progressBar.visibility = View.GONE
                            print("hello world")
                        val jsonArray = jsonObject.getJSONArray("data")

                        for (i in 0 until jsonArray.length()) {
                            val data = jsonArray.getJSONObject(i)

                            val restName = data.getString("restaurant_name")
                            val orderPlacedAt = data.getString("order_placed_at")
                            println("restName:${restName}")


                            val foodItems = data.getJSONArray("food_items")
                            val foodList = arrayListOf<FoodItems>()


                            for (i in 0 until foodItems.length()) {

                                val foodData = foodItems.getJSONObject(i)

                                val name = foodData.getString("name")
                                val cost = foodData.getString("cost")
                                val id = foodData.getString("food_item_id")

                                val food = FoodItems(
                                    id,
                                    name,
                                    cost
                                )
                                foodList.add(food)

                            }

                            val foodSection = OrderHistorySection(
                                restName,
                                orderPlacedAt,
                                foodList
                            )

                          orderHistoryList.add(foodSection)


                        }
                        layoutManager = LinearLayoutManager(activity as Context)
                        orderHistoryRecyclerView.layoutManager  = layoutManager
                        orderHistoryAdapter = OrderHistoryAdapter(activity as Context,orderHistoryList)
                        orderHistoryRecyclerView.adapter = orderHistoryAdapter
                            orderHistoryRecyclerView.addItemDecoration(
                                DividerItemDecoration(
                                    orderHistoryRecyclerView.context,
                                    DividerItemDecoration.VERTICAL
                                )
                            )


                    } else {
                            Toast.makeText(activity as Context, "Please!try after some time", Toast.LENGTH_SHORT).show()


                        }




            }, Response.ErrorListener {
                Toast.makeText(activity as Context, "Please!try after some time", Toast.LENGTH_SHORT).show()


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



        return view
    }

}
