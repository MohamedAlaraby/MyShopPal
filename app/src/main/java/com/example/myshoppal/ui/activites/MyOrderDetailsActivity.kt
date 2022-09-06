package com.example.myshoppal.ui.activites

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.model.Order
import com.example.myshoppal.ui.adapters.MyCartListAdapter
import com.example.myshoppal.utils.Constants
import kotlinx.android.synthetic.main.activity_orders.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MyOrderDetailsActivity : AppCompatActivity() {
    lateinit var myOrder:Order
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        setUpActionBar()
        if (intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)){
            myOrder= intent.getParcelableExtra<Order>(Constants.EXTRA_MY_ORDER_DETAILS)!!
        }
        setUpUI(myOrder)
    }

    private fun setUpActionBar(){
        setSupportActionBar(toolbar_my_order_details_activity)
        val actionbar=supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_set_home_as_up_indicator)
            actionbar.title=""
        }
        toolbar_my_order_details_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }//setUpActionBar()
    private fun setUpUI(orderDetails: Order){
        tv_order_details_id.text=orderDetails.title
        val dateFormat="dd MMM yyyy HH:mm"
        val formatter=SimpleDateFormat(dateFormat, Locale.getDefault())
        val calender:Calendar =Calendar.getInstance()
        calender.timeInMillis=orderDetails.order_date_time
        val orderDateTime=formatter.format(calender.time)
        tv_order_details_date.text=orderDateTime
     /*
     To display the status of delivering
     */
     /*
         Get e difference between the order date time and current date time in hours.
         If the difference in hours is 1 or less then the order status will be PENDING.
         If the difference in hours is 2 or greater then 1 then the order status will be PROCESSING.
         And, if the difference in hours is 3 or greater then the order status will be DELIVERED.
     */
 val diffInMilliSeconds: Long = System.currentTimeMillis() - orderDetails.order_date_time
 val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
 Log.i("Difference in Hours", "$diffInHours")
 when {
     diffInHours<1  -> {
         tv_order_status.text = resources.getString(R.string.order_status_pending)
         tv_order_status.setTextColor(
             ContextCompat.getColor(
                 this@MyOrderDetailsActivity,
                 R.color.colorAccent
             )
         )
     }
     diffInHours < 2 -> {
         tv_order_status.text = resources.getString(R.string.order_status_in_process)
         tv_order_status.setTextColor(
             ContextCompat.getColor(
                 this@MyOrderDetailsActivity,
                 R.color.order_status_in_process
             )
         )
     }
     else -> {
         tv_order_status.text = resources.getString(R.string.order_status_delivered)
         tv_order_status.setTextColor(
             ContextCompat.getColor(
                 this@MyOrderDetailsActivity,
                 R.color.order_status_delivered
             )
         )
     }
 }
 // END
        rv_my_order_items_list.layoutManager=LinearLayoutManager(this)
        rv_my_order_items_list.setHasFixedSize(true)
        rv_my_order_items_list.adapter=MyCartListAdapter(this,orderDetails.items,false)

        tv_my_order_details_address_type.text=orderDetails.address.type
        tv_my_order_details_full_name.text=orderDetails.address.name
        tv_my_order_details_address.text="${orderDetails.address.address},${orderDetails.address.zipCode}"
        tv_my_order_details_additional_note.text=orderDetails.address.additionalNote
        if (orderDetails.address.otherDetails.isNotEmpty()){
            tv_my_order_details_other_details.visibility= View.VISIBLE
            tv_my_order_details_other_details.text=orderDetails.address.otherDetails
        }
        else{
            tv_my_order_details_other_details.visibility= View.GONE
        }
        tv_my_order_details_mobile_number.text=orderDetails.address.phoneNumber
        tv_order_details_sub_total.text="$${orderDetails.sub_total_amount}"
        tv_order_details_shipping_charge.text="$${orderDetails.shipping_charge}"
        tv_order_details_total_amount.text="$${orderDetails.total_amount}"


    }
}