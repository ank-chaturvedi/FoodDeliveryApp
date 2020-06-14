package com.basics.fooddeliveryapp.ProfileActivity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basics.fooddeliveryapp.R
import com.basics.fooddeliveryapp.util.ConectionManager
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    lateinit var etName: TextInputLayout
    lateinit var etEmail: TextInputLayout
    lateinit var etMobileNo: TextInputLayout
    lateinit var etDeliveryAddress: TextInputLayout
    lateinit var etPassword: TextInputLayout
    lateinit var etConfirmPassword: TextInputLayout
    lateinit var toolbar: Toolbar

    lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMobileNo = findViewById(R.id.etMobile)
        etDeliveryAddress = findViewById(R.id.etDeliveryAddress)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        toolbar = findViewById(R.id.toolbar)
        setUpToolbar()









        btnRegister.setOnClickListener {
            var name: String? = etName.editText?.text.toString().trim()
            var email: String? = etEmail.editText?.text.toString().trim()
            var mobileNo: String? = etMobileNo.editText?.text.toString().trim()
            var deliveryAddress: String? = etDeliveryAddress.editText?.text.toString().trim()
            var password: String? = etPassword.editText?.text.toString()
            var confirmPassword: String? = etConfirmPassword.editText?.text.toString()


            val passwordValid = isValidPassword(password, confirmPassword)

            val nameValid = isValidName(name)

            val validMobile = isValidMobile(mobileNo)


            /*
                * this nested  block's will check the entered details whether they are correct to
                * send on server or need correction.
                * if all the field are filled correct then the else part will execute
             */
            if (!passwordValid) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Please enter valid password",
                    Toast.LENGTH_SHORT
                ).show()

            } else if (!nameValid) {
                Toast.makeText(this, "Please enter correct name", Toast.LENGTH_SHORT).show()
            } else if (email == null || deliveryAddress == null) {
                Toast.makeText(this, "Please fill details", Toast.LENGTH_SHORT).show()
            } else if (email!!.isEmpty() || mobileNo!!.isEmpty() || deliveryAddress!!.isEmpty()) {
                Toast.makeText(this, "Please fill the details", Toast.LENGTH_SHORT).show()
            } else if (!validMobile) {
                Toast.makeText(this, "Please enter a valid mobile no", Toast.LENGTH_SHORT).show()
            } else {
                val queue = Volley.newRequestQueue(this@RegisterActivity)

                val url = "http://13.235.250.119/v2/register/fetch_result"

                val jsonObject = JSONObject()

                // store the data in jsonObject to send on the url
                jsonObject.put("name", name)
                jsonObject.put("mobile_number", mobileNo)
                jsonObject.put("password", password)
                jsonObject.put("address", deliveryAddress)
                jsonObject.put("email", email)


                /*
                    * check internet is connected or not if connected post the
                    * details otherwise ask user to connect to internet
                 */


                if (ConectionManager().internetConnection(this@RegisterActivity)) {

                    /*
                        * jsonRequest object is created will send the data to the url
                        * and if successful the it will direct the user to successful screen
                        * or if some error occurs will show the toast message according to the error
                     */
                    val jsonRequest =
                        object : JsonObjectRequest(Method.POST, url, jsonObject,
                            Response.Listener {

                                /*
                                    * this block will fetch the data for url
                                    * if no error occur
                                    * and catch blocks will toast a message "some error occurred"
                                 */

                                try {
                                    val jsonValues = it.getJSONObject("data")

                                    val success = jsonValues.getBoolean("success")

                                    if (success) {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            "successfully registered",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            "Email or Mobile no is already registered",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }

                                } catch (e: JSONException) {
                                    Toast.makeText(
                                        this,
                                        "Please! enter valid details",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                }

                            }, Response.ErrorListener {

                                Toast.makeText(this, "Please! try again later", Toast.LENGTH_SHORT)
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
                    var alertDialog = AlertDialog.Builder(this)
                    alertDialog.setTitle("Network Connection")
                    alertDialog.setMessage("Internet is not Connected")

                    alertDialog.setPositiveButton("open setting") { text, listener ->

                        val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingIntent)
                        finish()

                    }
                    alertDialog.setNegativeButton("exit") { text, listener ->
                        finishAffinity()
                    }

                    alertDialog.create()
                    alertDialog.show()
                }


            }

        }
    }


    private fun isValidPassword(password: String?, confirmPassword: String?): Boolean {

        return !(password == null || confirmPassword == null || password!!.length < 4 || !password.equals(
            confirmPassword
        ))

    }

    private fun isValidName(name: String?): Boolean = !(name == null || name?.length < 3)


    private fun isValidMobile(mobile: String?) = (mobile != null && mobile?.length == 10)


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
