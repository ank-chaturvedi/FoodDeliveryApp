package com.basics.fooddeliveryapp.Fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.basics.fooddeliveryapp.R


class ProfileFragment : Fragment() {
    lateinit var txtName:TextView
    lateinit var txtMobile:TextView
    lateinit var txtEmail:TextView
    lateinit var txtAddress:TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var imgUserAvatar:ImageView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_profile,container,false)


        txtName = view.findViewById(R.id.txtUserName)
        txtMobile = view.findViewById(R.id.txtMobileNo)
        txtEmail = view.findViewById(R.id.txtEmail)
        txtAddress = view.findViewById(R.id.txtAddress)
        imgUserAvatar = view.findViewById(R.id.imgUserAvatar)

        sharedPreferences = activity!!.getSharedPreferences("Login Preference", Context.MODE_PRIVATE)


        txtName.text = sharedPreferences.getString("name","username")
        txtMobile.text = sharedPreferences.getString("mobile","+000000000000")
        txtEmail.text = sharedPreferences.getString("email","example@gmail.com")
        txtAddress.text = sharedPreferences.getString("address","address of yours")



        return view
    }

}
