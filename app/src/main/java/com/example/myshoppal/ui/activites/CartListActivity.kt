package com.example.myshoppal.ui.activites
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.CartItem
import com.example.myshoppal.model.Product
import com.example.myshoppal.ui.adapters.MyCartListAdapter
import kotlinx.android.synthetic.main.activity_cart.*
class CartListActivity : BaseActivity() {
    private lateinit var mProductsList:ArrayList<Product>
    private lateinit var mCartItemsList:ArrayList<CartItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setUpActionBar()

    }
    private fun setUpActionBar(){
        setSupportActionBar(toolbar_cart_list_activity)
        val actionbar=supportActionBar
        if (actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_set_home_as_up_indicator)
            actionbar.title=""
        }
        toolbar_cart_list_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }//setUpActionBar()
    override fun onResume() {
        super.onResume()
        getAllProductsList()
    }

    fun getCartListSuccess(cartList:ArrayList<CartItem>) {
        hideProgressDialog()
        for(product in mProductsList){
            for(cartItem in cartList){
               if (product.product_id==cartItem.product_id){
                       cartItem.stock_quantity=product.stock_quantity
                       if (product.stock_quantity.toInt()==0){
                             cartItem.cart_quantity=product.stock_quantity
                       }
               }
            }
        }
        mCartItemsList=cartList

        if (mCartItemsList.size > 0 ){

        rv_cart_items_list.visibility= View.VISIBLE
        ll_checkout.visibility=View.VISIBLE
        tv_no_cart_item_found.visibility=View.GONE

        rv_cart_items_list.layoutManager=LinearLayoutManager(this@CartListActivity)
        rv_cart_items_list.setHasFixedSize(true)
        rv_cart_items_list.adapter=MyCartListAdapter(this@CartListActivity,mCartItemsList)
        var subTotal:Double=0.0
        for ( i in mCartItemsList){
           val availableQuantity=i.stock_quantity.toInt()
           if (availableQuantity > 0){
               subTotal+= (i.price.toDouble()) * (i.cart_quantity.toInt())
           }
        }
            if (subTotal > 0.0){

                 tv_sub_total.text="$${subTotal.toString()}"
                 tv_shipping_charge.text="$${0.1*subTotal}"
                 tv_total_amount.text=((subTotal)+(0.1*subTotal)).toString()
          }
        }
        else{
            rv_cart_items_list.visibility=View.GONE
            tv_no_cart_item_found.visibility=View.VISIBLE
            ll_checkout.visibility=View.GONE
        }
    }//getCartListSuccess()
    fun getCartItemsList(){
        //showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getCartList(this)
    }

    fun getAllProductsListSuccess(list:ArrayList<Product>){
        hideProgressDialog()
        mProductsList=list
        //after i get all the products ,i will get the cart items list
        getCartItemsList()
   }
    fun getAllProductsList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getAllProductsList(this)
    }

    fun removeItemFromCartSuccess(){
        hideProgressDialog()
        Toast.makeText(this@CartListActivity,
                    resources.getString(R.string.msg_item_removed_successfully),
                    Toast.LENGTH_LONG).show()
        getCartItemsList()
    }

    fun updateMyCartSuccess() {
        hideProgressDialog()
        getCartItemsList()
    }
}