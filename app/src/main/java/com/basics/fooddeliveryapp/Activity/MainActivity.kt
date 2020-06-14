package com.basics.fooddeliveryapp.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.basics.fooddeliveryapp.ProfileActivity.LoginActivity
import com.basics.fooddeliveryapp.Fragment.*
import com.basics.fooddeliveryapp.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var frameLayout: FrameLayout
    lateinit var toolbar: Toolbar
    lateinit var navigationView: NavigationView
    lateinit var coordinatorLayout: CoordinatorLayout

    lateinit var sharedPrefrence: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        drawerLayout = findViewById(R.id.drawerLayout)
        frameLayout = findViewById(R.id.frame)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        sharedPrefrence = getSharedPreferences("Login Preference", Context.MODE_PRIVATE)
        setUpToolbar()
        openHome()


        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.closed_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.home -> {
                    openHome()
                    drawerLayout.closeDrawers()


                }

                R.id.myProfile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            ProfileFragment()
                        )
                        .commit()

                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()


                }

                R.id.favouriteRestaurants -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavouriteRestaurantsFragment()
                        )
                        .commit()
                    supportActionBar?.title = "FavouriteRestaurants"
                    drawerLayout.closeDrawers()


                }

                R.id.orderHistory -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            OrderHistoryFragment()
                        )
                        .commit()
                    supportActionBar?.title = "Order History"
                    drawerLayout.closeDrawers()

                }

                R.id.faqs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FaqsFragment()
                        )
                        .commit()
                    supportActionBar?.title = "Faqs"
                    drawerLayout.closeDrawers()


                }

                R.id.logout -> {
                    drawerLayout.closeDrawers()
                    var alertDialog = AlertDialog.Builder(this)
                    alertDialog.setTitle("Confirmation")
                    alertDialog.setMessage("Are your sure you want to log out?")

                    alertDialog.setPositiveButton("Yes") { text, listener ->
                        val intent = Intent(
                            this,
                            LoginActivity::class.java
                        )
                        val sharedPreferences =
                            getSharedPreferences("Login Preference", Context.MODE_PRIVATE)

                        val editor = sharedPreferences.edit()

                        editor.clear()
                        editor.commit()

                        startActivity(intent)
                        finishAffinity()

                    }

                    alertDialog.setNegativeButton("NO") { text, listener ->
                            openHome()
                        navigationView.setCheckedItem(R.id.home)
                    }

                    alertDialog.create()
                    alertDialog.show()

                }


            }
            return@setNavigationItemSelectedListener true
        }


        val headerView = navigationView.getHeaderView(0)

        val txtUsername: TextView = headerView.findViewById(R.id.txtUserName)
        val txtMobileNo: TextView = headerView.findViewById(R.id.txtMobileNo)

        txtUsername.text = sharedPrefrence.getString("name", "username")
        txtMobileNo.text = sharedPrefrence.getString("mobile", "+919191919191")


    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        val id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }


        return super.onOptionsItemSelected(item)
    }

    private fun openHome() {

        val transaction = supportFragmentManager.beginTransaction()
        val fragment = HomeFragment()
        transaction
            .replace(R.id.frame, fragment)
            .commit()

        supportActionBar?.title = "Home"

    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when (frag) {
            !is HomeFragment -> {openHome()
                navigationView.setCheckedItem(R.id.home)
            }
            else -> super.onBackPressed()

        }
    }


}

