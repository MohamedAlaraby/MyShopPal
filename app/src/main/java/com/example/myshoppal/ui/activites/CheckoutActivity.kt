package com.example.myshoppal.ui.activites
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.Address
import com.example.myshoppal.model.CartItem
import com.example.myshoppal.model.Order
import com.example.myshoppal.model.Product
import com.example.myshoppal.ui.adapters.MyCartListAdapter
import com.example.myshoppal.utils.Constants
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.item_address_layout.*
class CheckoutActivity : BaseActivity() {
    private  var mAddressDetails:Address?=null
    private lateinit var mProductList:ArrayList<Product>
    private lateinit var mCartItemList:ArrayList<CartItem>
    private var mSubTotal:Double=0.0
    private var mTotalAmount:Double=0.0
    private lateinit var mOrderDetails:Order


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setUpActionBar()
        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            mAddressDetails=intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)
        }
       if (mAddressDetails!=null){
           tv_checkout_address_type.text = mAddressDetails?.type
           tv_checkout_full_name.text = mAddressDetails?.name
           tv_checkout_address.text = mAddressDetails?.address
           tv_checkout_additional_note.text = mAddressDetails?.additionalNote
           if (mAddressDetails?.otherDetails!!.isNotEmpty()){
               tv_address_details_item_address.text = mAddressDetails?.otherDetails!!
           }
           checkout_tv_mobile_number.text=mAddressDetails?.phoneNumber

       }
        getProductsList()
        btn_place_order.setOnClickListener {
            placeAnOrder()
        }
    }
    private fun setUpActionBar(){
        setSupportActionBar(toolbar_checkout_activity)
        val actionbar=supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_set_home_as_up_indicator)
            actionbar.title=""
        }
        toolbar_checkout_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }//setUpActionBar()
    private fun getProductsList(){
         //show progress dialog
         showProgressDialog(getString(R.string.please_wait))
         FireStoreClass().getAllProductsList(this@CheckoutActivity)
    }
    fun successGetProductsFromFireStore(productsList:ArrayList<Product>){
         mProductList=productsList
         getCartItemList()
    }
    private fun getCartItemList(){
      FireStoreClass().getCartList(this@CheckoutActivity)
   }
    fun successCartListFromFireStore(cartList:ArrayList<CartItem>){
        hideProgressDialog()
        for (product in mProductList){
            for (cartItem in cartList){
               if (product.product_id==cartItem.product_id){
                   cartItem.stock_quantity=product.stock_quantity
               }
            }
        }
        mCartItemList=cartList
        rv_cart_list_items.layoutManager=LinearLayoutManager(this@CheckoutActivity)
        rv_cart_list_items.setHasFixedSize(true)
        rv_cart_list_items.adapter=MyCartListAdapter(this@CheckoutActivity, mCartItemList, false)
        for (item in mCartItemList){
            val availableQuantity=item.stock_quantity.toInt()
            if (availableQuantity>0){
                //so the item is exists in the stock and i can order it
                val price=item.price.toDouble()
                val quantity=item.cart_quantity.toInt()
                mSubTotal+=(price*quantity)
            }
        }
        tv_checkout_sub_total.text=mSubTotal.toString()
        tv_checkout_shipping_charge.text=(mSubTotal*0.1).toString()
        if (mSubTotal>0){
            ll_checkout_place_order.visibility= View.VISIBLE
            mTotalAmount=mSubTotal+(mSubTotal*0.1)
            tv_checkout_total_amount.text=mTotalAmount.toString()
        }else{
            ll_checkout_place_order.visibility= View.GONE
        }
    }
   private fun placeAnOrder(){
        showProgressDialog(getString(R.string.please_wait))
        if (mAddressDetails != null){
            mOrderDetails=Order(
            user_id = FireStoreClass().getCurrentUserID(),
            items = mCartItemList,
            address = mAddressDetails!!,
            title= "My Order ${System.currentTimeMillis()}" ,
            image = mCartItemList[0].image,
            sub_total_amount = mSubTotal.toString(),
            shipping_charge =(mSubTotal*0.1).toString(),
            total_amount = mTotalAmount.toString(),
             order_date_time =System.currentTimeMillis()
            )
            FireStoreClass().placeOrder(this@CheckoutActivity,mOrderDetails)
        }
    }
    fun orderPlacedSuccess(){
      FireStoreClass().updateAllDetails(this, cartList =mCartItemList,mOrderDetails)
    }
    fun allDetailsUpdatedSuccess(){
        hideProgressDialog()
        Toast.makeText(this@CheckoutActivity,getString(R.string.msg_order_placed_success),Toast.LENGTH_LONG).show()
        val intent=Intent(this,DashboardActivity::class.java)
        //These flags will close all the previous activities because I won't need them in my next journey
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


}