package com.example.myshoppal.ui.activites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.Address
import com.example.myshoppal.ui.adapters.MyAddressListAdapter
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.SwipeToEditCallback
import com.myshoppal.utils.SwipeToDeleteCallback
import kotlinx.android.synthetic.main.activity_address_list.*

@Suppress("DEPRECATION")
class AddressListActivity :BaseActivity() {
    private var mSelectedAddress:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        setUpActionBar()
        tv_add_address_address_list_activity.setOnClickListener {
            startActivityForResult(Intent(this@AddressListActivity,AddEditAddressActivity::class.java)
                ,Constants.ADD_ADDRESS_REQUEST_CODE)
        }
        if (intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)){
            mSelectedAddress=intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS,false)
        }
        if (mSelectedAddress){
            tv_title_address_list_activity.text = getString(R.string.title_select_address)
        }
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
    fun successAddressListFromFirestore(addressList: ArrayList<Address>){
        hideProgressDialog()
        for (i in addressList) {
            Log.i("Name and Address", "${i.name} ::  ${i.address}")
        }
        if (addressList.size > 0){
            rv_address_list_activity.visibility= View.VISIBLE
            tv_no_address_found.visibility= View.GONE
            rv_address_list_activity.layoutManager= LinearLayoutManager(this@AddressListActivity)
            rv_address_list_activity.setHasFixedSize(true)

            val adapter= MyAddressListAdapter(this@AddressListActivity, addressList,mSelectedAddress)
            rv_address_list_activity.adapter=adapter
             if (!mSelectedAddress){
                 //I came from Setting activity cause mSelectedAddress flag is false
                 //Swipe to edit manipulation
                 //start
                 val editSwipeHandler=object: SwipeToEditCallback(this){
                     override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                         val myAdapter=rv_address_list_activity.adapter as MyAddressListAdapter
                         myAdapter.notifyEditItem(this@AddressListActivity, position =viewHolder.adapterPosition )
                     }
                 }

                 val editItemTouchHelper=ItemTouchHelper(editSwipeHandler)
                 editItemTouchHelper.attachToRecyclerView(rv_address_list_activity)
                 //end
                 //Swipe to delete manipulation
                 //start
                 val deleteSwipeHandler= object : SwipeToDeleteCallback(this) {
                     override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                         showProgressDialog(getString(R.string.please_wait))
                         FireStoreClass().deleteAddress(this@AddressListActivity,
                             addressList[viewHolder.adapterPosition].id)

                     }
                 }
                 val deleteItemTouchHelper=ItemTouchHelper(deleteSwipeHandler)
                 deleteItemTouchHelper.attachToRecyclerView(rv_address_list_activity)
                 //end
             }else{
                 //I came from cart list activity cause mSelectedAddress flag is true
             }
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

    fun successDeleteAddress() {
        hideProgressDialog()
        Toast.makeText(this@AddressListActivity,
            getString(R.string.msg_your_address_deleted_successfully),
            Toast.LENGTH_LONG).show()
        //to refresh the recycler view
        getAddressList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK){
            getAddressList()
        }
    }
}