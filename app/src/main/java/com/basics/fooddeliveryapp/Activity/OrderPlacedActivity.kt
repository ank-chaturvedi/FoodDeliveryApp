package com.basics.fooddeliveryapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.basics.fooddeliveryapp.R

class OrderPlacedActivity : AppCompatActivity() {
    lateinit var btnOrderPlaced: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)

        btnOrderPlaced = findViewById(R.id.btnOrderConfirmed)


        btnOrderPlaced.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}
