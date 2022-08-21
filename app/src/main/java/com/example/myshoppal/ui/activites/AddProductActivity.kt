package com.example.myshoppal.ui.activites

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.Product
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class AddProductActivity : BaseActivity(), View.OnClickListener {
    private  var mSelectedImageFileUri: Uri?=null
    private  var mProductImageURL:String=" "

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
         setUpActionBar()

        btn_submit_add_new_product_activity.setOnClickListener(this)
        iv_add_update_product.setOnClickListener(this)
    }
    fun setUpActionBar(){
        setSupportActionBar(toolbar_add_product_activity)
        val actionbar=supportActionBar
        if (actionbar != null){
            //Params:showHomeAsUp –>if true-->> to show the user that selecting home will return one level up
            // rather than to the top level of the app.
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_set_home_as_up_indicator)
        }
        actionbar?.setDisplayShowTitleEnabled(false)
        toolbar_add_product_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if (v!=null){
            when(v.id){
                R.id.iv_add_update_product->{
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            Constants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE
                        )
                    }
                }
                R.id.btn_submit_add_new_product_activity->{
                   if(validateProductDetails()){
                       uploadProductImageToCloud()
                   }
                }
            }
        }
    }
    fun productUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(this,resources.getString(R.string.product_uploaded_success_message),Toast.LENGTH_LONG).show()
        finish()
    }
    fun uploadProductDetailsToCloud(){
        val userName=getSharedPreferences(Constants.MY_SHOP_PAL_PREFRENCES, MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME," ")!!
        val  productTitle=et_product_title_add_product_activity.text.toString().trim { it<=' '}
        val  productPrice=et_product_price_add_product_activity.text.toString().trim { it<=' '}
        val  productDesc=et_product_description_add_product_activity.text.toString().trim { it<=' '}
        val  productQuantity=et_product_quantity_add_product_activity.text.toString().trim { it<=' '}
        val image=mProductImageURL
        val product=Product(
            FireStoreClass().getCurrentUserID(),
            userName,productTitle,productPrice,productDesc,productQuantity,image
        )
        FireStoreClass().uploadProductDetails(this,product)
    }

    fun uploadProductImageToCloud(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().uploadImageToCloudStorage(this,mSelectedImageFileUri,Constants.PRODUCT_IMAGE)
    }
    fun imageUploadSuccess(imageURL: String) {
        mProductImageURL = imageURL
        uploadProductDetailsToCloud()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.permission_denied_string),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }//onRequestPermissionsResult

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode ==RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                iv_add_update_product.setImageDrawable(
                    ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))

                if (data != null) {
                    try {
                        // The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!
                        GlideLoader(this).loadUserPicture(
                            mSelectedImageFileUri!!,
                            iv_product_photo_add_product_activity
                        )
                    }catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode ==RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }
    private fun setupActionBar() {
        setSupportActionBar(toolbar_user_profile_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title=""
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_set_home_as_up_indicator)
        }
        toolbar_user_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }//onActivityResult
    //a fun to validate the new user entries
    private fun validateProductDetails():Boolean{
         when{
             mSelectedImageFileUri == null->{
                 showErrorSnackBar(resources.getString(R.string.err_msg_enter_the_image_of_the_product),true)
                 return false
             }
            TextUtils.isEmpty(et_product_title_add_product_activity.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title),true)
                return  false
            }
            TextUtils.isEmpty(et_product_price_add_product_activity.text.toString().trim { it <= ' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price),true)
                return  false
            }
            TextUtils.isEmpty(et_product_description_add_product_activity.text.toString().trim { it <= ' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_description),true)
                return   false
            }
            TextUtils.isEmpty(et_product_quantity_add_product_activity.text.toString().trim { it <= ' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_quantity),true)
                return  false
            }
            else -> return true
            }
    }//function
}