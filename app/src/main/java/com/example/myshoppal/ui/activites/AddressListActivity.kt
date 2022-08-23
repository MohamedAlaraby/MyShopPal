package com.example.myshoppal.ui.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myshoppal.R
import kotlinx.android.synthetic.main.activity_address_list.*
class AddressListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        setUpActionBar()
    }
    fun setUpActionBar(){
        setSupportActionBar(toolbar_address_list_activity)
        val actionbar=supportActionBar
        if (actionbar != null)
        {
            //Params:showHomeAsUp –>if true-->> to show the user that selecting home will return one level up rather than to the top level of the app.
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_set_home_as_up_indicator)
        }
        toolbar_address_list_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }


}