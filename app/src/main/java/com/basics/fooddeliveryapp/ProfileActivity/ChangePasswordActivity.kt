package com.basics.fooddeliveryapp.ProfileActivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basics.fooddeliveryapp.R
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONException
import org.json.JSONObject

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var etOtp:TextInputLayout
    lateinit var etPassword:TextInputLayout
    lateinit var etConfirmPassword:TextInputLayout
    lateinit var btnSubmit:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)


        etOtp = findViewById(R.id.etOtp)
        etPassword  = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSubmit = findViewById(R.id.btnSubmit)

        val mobileNo = intent.getStringExtra("mobile")


        btnSubmit.setOnClickListener {

            val password = etPassword.editText?.text.toString().trim()
            val confirmPassword = etConfirmPassword.editText?.text.toString().trim()
            val otp = etOtp.editText?.text.toString().trim()


            if(!password.equals(confirmPassword)){
                Toast.makeText(this,"Please enter correct password",Toast.LENGTH_SHORT).show()
            }

            else{
                val queue = Volley.newRequestQueue(this)

                val url  = "http://13.235.250.119/v2/reset_password/fetch_result/"

                val jsonObject = JSONObject()
                jsonObject.put("mobile_number",mobileNo)
                jsonObject.put("password",password)
                jsonObject.put("otp",otp)


                val jsonRequest = object:JsonObjectRequest(Method.POST,url,jsonObject,Response.Listener {

                    try {
                        val jsonObject = it.getJSONObject("data")

                        val success = jsonObject.getBoolean("success")

                        if(success){
                            val intent = Intent(this,
                                LoginActivity::class.java)
                            val sharedPreferences = getSharedPreferences("Login Preference",
                                Context.MODE_PRIVATE)


                            val editor = sharedPreferences.edit()

                            editor.clear()
                            editor.commit()

                            startActivity(intent)
                            finishAffinity()

                        }else{
                            Toast.makeText(this,"Invalid Details",Toast.LENGTH_SHORT).show()
                        }

                    } catch (e:JSONException){
                        Toast.makeText(this,"Please!try after some time",Toast.LENGTH_SHORT).show()
                    }



                },Response.ErrorListener {

                    Toast.makeText(this,"Some error occurred",Toast.LENGTH_SHORT).show()


                }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String,String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "ec66f7766ff4a4"
                        return headers
                    }
                }

                queue.add(jsonRequest)
            }




        }


    }


}
