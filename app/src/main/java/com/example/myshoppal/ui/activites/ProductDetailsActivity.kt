package com.example.myshoppal.ui.activites
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.CartItem
import com.example.myshoppal.model.Product
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_details.*
class ProductDetailsActivity : BaseActivity(),View.OnClickListener
{
    private lateinit var mProductDetails: Product
    private var mProductID:String=""
    private var mProductOwnerID:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setUpActionBar()

        //receiving my own product id value
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID) ){
            mProductID=intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        //receiving my own product owner id value
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID) ){
            mProductOwnerID=intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }
        //getCurrentUserID()>>return by the id of the user who logged in our app
        if (mProductOwnerID==FireStoreClass().getCurrentUserID()){
            //if the logged in user is the owner of the product,
                // i don't want to display the add to cart because it's mine already.
            btn_add_to_cart.visibility= View.GONE
            btn_go_to_cart_activity.visibility= View.VISIBLE
        }else{
            btn_add_to_cart.visibility= View.VISIBLE
            btn_go_to_cart_activity.visibility= View.VISIBLE
        }

        getProductDetails()
        btn_add_to_cart.setOnClickListener(this)
        btn_go_to_cart_activity.setOnClickListener(this)
    }//on create()
    fun setUpActionBar(){
        setSupportActionBar(toolbar_product_details_activity)
        val actionbar=supportActionBar
        if (actionbar != null){
             //Params:showHomeAsUp –>if true-->> to show the user that selecting home will
             // return one level up rather than to the top level of the app.
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_set_home_as_up_indicator)
            actionbar.title=""
        }
        toolbar_product_details_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }//setUpActionBar()
    fun productDetailsSuccess(product:Product){
       mProductDetails=product
       GlideLoader(this).loadProductPicture(product.image,iv_product_detail_image)
       tv_product_details_title.text=product.title
       tv_product_details_price.text="$${product.price}"
       tv_product_details_description.text=product.description
       tv_product_details_available_quantity.text=product.stock_quantity
       if (product.stock_quantity.toInt() ==0){
           hideProgressDialog()
           btn_add_to_cart.visibility=View.GONE
           tv_product_details_available_quantity.text=resources.getString(R.string.lbl_out_of_stock)
           tv_product_details_available_quantity.setTextColor(
           ContextCompat.getColor(this@ProductDetailsActivity,R.color.colorSnackBarError)
           )
       }else{
           if (FireStoreClass().getCurrentUserID()==mProductDetails.user_id)
           {
               hideProgressDialog()
           }else
           {
               FireStoreClass().checkIfItemExistsInCart(this,mProductID)
           }
       }
   }//productDetailsSuccess()
    private fun addToCart(){
       val cartItem=CartItem(
           user_id =FireStoreClass().getCurrentUserID(),
           product_owner_id=mProductOwnerID,
           product_id = mProductID,
           title = mProductDetails.title,
           price = mProductDetails.price,
           image = mProductDetails.image,
           cart_quantity = Constants.DEFAULT_CART_QUANTITY,
           stock_quantity = mProductDetails.stock_quantity
       )
       showProgressDialog(resources.getString(R.string.please_wait))
       FireStoreClass().addCartItems(this,cartItem)
   }
    private fun getProductDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getProductDetails(this,mProductID)
  }
    fun addTocartSuccess(){
     hideProgressDialog()
     Toast.makeText(this,resources.getString(R.string.cart_item_uploaded_success_message),
         Toast.LENGTH_LONG).show()
     btn_add_to_cart.visibility= View.GONE
     btn_go_to_cart_activity.visibility= View.VISIBLE
     finish()
 }
    fun productExistsInCart(){
     hideProgressDialog()
     btn_go_to_cart_activity.visibility=View.VISIBLE
     btn_add_to_cart.visibility=View.GONE
 }
    override fun onClick(v: View?) {
        if (v!=null)
        {
            when(v.id){
                R.id.btn_add_to_cart->{
                    addToCart()
                }
            }
            when(v.id){
                R.id.btn_go_to_cart_activity->{
                          startActivity(Intent(this,CartListActivity::class.java))
                }
            }
        }
    }
}