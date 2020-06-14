package com.basics.fooddeliveryapp.ProfileActivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basics.fooddeliveryapp.Activity.MainActivity
import com.basics.fooddeliveryapp.R
import com.basics.fooddeliveryapp.util.ConectionManager
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONException
import org.json.JSONObject
import java.lang.NumberFormatException

class LoginActivity : AppCompatActivity() {
    lateinit var txtSignUp: TextView
    lateinit var txtForgotPassword: TextView
    lateinit var etMobileNo: TextInputLayout
    lateinit var etPassword: TextInputLayout
    lateinit var btnLogin: Button
    lateinit var LoginSharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtSignUp = findViewById(R.id.txtSignUp)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        etMobileNo = findViewById(R.id.etMobile)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)



        LoginSharedPref =
            getSharedPreferences(getString(R.string.login_preference), Context.MODE_PRIVATE)

        val alreadyLoggedIn = LoginSharedPref.getBoolean("is_logged_in", false)

        /*
            * this condition will check whether user is already signed in or if
            * already signed in then no need to fill login details again
         */

        if (alreadyLoggedIn) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }


        // will open the sign up activity
        txtSignUp.setOnClickListener {
            openSignUpActivity()

        }

        //will open the forgot password activity
        txtForgotPassword.setOnClickListener {
            openForgotPasswordActivity()
        }




        btnLogin.setOnClickListener {
            val mobile: String? = etMobileNo.editText?.text.toString().trim()
            val pass: String? = etPassword.editText?.text.toString().trim()

            val isValidMobile = validateMobile(mobile)
            val isValidPass = validatePass(pass)

            if (!ConectionManager().internetConnection(this)) {
                Toast.makeText(this, "Please! connect to internet", Toast.LENGTH_SHORT).show()
            } else if (!isValidMobile) {
                Toast.makeText(this, "Please!enter a valid mobile no", Toast.LENGTH_SHORT).show()
            } else if (!isValidPass) {
                Toast.makeText(this, "Please! enter a valid password", Toast.LENGTH_SHORT).show()

            } else {

                val queue = Volley.newRequestQueue(this)

                val url = "http://13.235.250.119/v2/login/fetch_result/"

                val jsonObject = JSONObject()
                jsonObject.put("mobile_number", mobile)
                jsonObject.put("password", pass)


                val jsonRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonObject, Response.Listener {

                        try {

                            val jsonObject = it.getJSONObject("data")

                            val success = jsonObject.getBoolean("success")

                            if (success) {


                                val userData = jsonObject.getJSONObject("data")
                                val name = userData.getString("name")
                                val email = userData.getString("email")
                                val mobile = userData.getString("mobile_number")
                                val address = userData.getString("address")
                                val userId = userData.getString("user_id")

                                LoginSharedPref.edit().putBoolean("is_logged_in", true).apply()
                                LoginSharedPref.edit().putString("name", name).apply()
                                LoginSharedPref.edit().putString("email", email).apply()
                                LoginSharedPref.edit().putString("mobile", mobile).apply()
                                LoginSharedPref.edit().putString("address", address).apply()
                                LoginSharedPref.edit().putString("user_id", userId).apply()


                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finishAffinity()

                            } else {
                                Toast.makeText(
                                    this,
                                    "Mobile no or Password is incorrect",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } catch (e: JSONException) {
                            Toast.makeText(this, "Please! try after some time", Toast.LENGTH_SHORT)
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


            }

        }


    }


    private fun openSignUpActivity() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        startActivity(intent)
    }


    private fun validateMobile(mobile: String?): Boolean {


        var isValid = true
        var isDigit = true



        if (mobile == null) {
            isValid = false
        } else {
            try {
                val num = mobile.toLong()

            } catch (e: NumberFormatException) {
                isDigit = false
            }

            if (!isDigit || mobile.length != 10)
                isValid = false
        }

        return isValid
    }


    private fun validatePass(pass: String?): Boolean {
        var isValid = true

        if (pass == null || pass?.length < 3) isValid = false


        return isValid
    }

    private fun openForgotPasswordActivity() {
        val intent = Intent(
            this,
            ForgotPasswordActivity::class.java
        )
        startActivity(intent)
    }


}
