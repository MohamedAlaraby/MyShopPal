package com.example.myshoppal.ui.activites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.Address
import com.example.myshoppal.ui.adapters.MyAddressListAdapter
import com.myshoppal.utils.SwipeToEditCallback
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressListActivity :BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        setUpActionBar()
        tv_add_address_address_list_activity.setOnClickListener {
            startActivity(Intent(this@AddressListActivity,AddEditAddressActivity::class.java))

        }

    }

    override fun onResume() {
        super.onResume()

        getAddressList()
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


    fun successAddressListFromFirestore(addressList: ArrayList<Address>) {
        hideProgressDialog()
        for (i in addressList) {
            Log.i("Name and Address", "${i.name} ::  ${i.address}")
        }
        if (addressList.size > 0){
            rv_address_list_activity.visibility= View.VISIBLE
            tv_no_address_found.visibility= View.GONE
            rv_address_list_activity.layoutManager= LinearLayoutManager(this@AddressListActivity)
            rv_address_list_activity.setHasFixedSize(true)

            val adapter= MyAddressListAdapter(this@AddressListActivity, addressList)
            rv_address_list_activity.adapter=adapter

            val editSwipeHandler=object: SwipeToEditCallback(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                      val adapter=rv_address_list_activity.adapter as MyAddressListAdapter
                      adapter.notifyEditItem(this@AddressListActivity, position =viewHolder.adapterPosition )
                }
            }
            val editItemTouchHelper=ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(rv_address_list_activity)


        }
        else{
            rv_address_list_activity.visibility= View.GONE
            tv_no_address_found.visibility= View.VISIBLE
        }



    }
    fun getAddressList(){
        showProgressDialog(getString(R.string.please_wait))
        FireStoreClass().getAddressesList(this@AddressListActivity)
    }

}