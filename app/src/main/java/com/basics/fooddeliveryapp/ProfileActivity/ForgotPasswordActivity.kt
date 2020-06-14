package com.basics.fooddeliveryapp.ProfileActivity

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

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var etMobileNo:TextInputLayout
    lateinit var etEmail: TextInputLayout
    lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etMobileNo = findViewById(R.id.etMobile)
        etEmail = findViewById(R.id.etEmail)
        btnNext = findViewById(R.id.btnNext)




        btnNext.setOnClickListener {

            val mobile= etMobileNo.editText?.text.toString().trim()
            val email = etEmail.editText?.text.toString().trim()


            val queue = Volley.newRequestQueue(this)

            val url = "http://13.235.250.119/v2/forgot_password/fetch_result/"

            val jsonObject = JSONObject()

            jsonObject.put("mobile_number",mobile)
            jsonObject.put("email",email)

            val intent = Intent(this@ForgotPasswordActivity,
                ChangePasswordActivity::class.java)
            intent.putExtra("mobile",mobile)



            val jsonRequest = object :JsonObjectRequest(Method.POST,url,jsonObject,Response.Listener{

                try{

                    val jsonObject = it.getJSONObject("data")

                    val success = jsonObject.getBoolean("success")


                    if(success){

                        startActivity(intent)


                    }
                    else{
                        Toast.makeText(this,"Please enter correct details",Toast.LENGTH_SHORT).show()
                    }


                } catch (e:JSONException){
                    Toast.makeText(this,"some error occurred",Toast.LENGTH_SHORT).show()
                }





            },Response.ErrorListener {

                Toast.makeText(this,"Please!try after some time",Toast.LENGTH_SHORT).show()

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
