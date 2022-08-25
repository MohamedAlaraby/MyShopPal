package com.example.myshoppal.ui.activites
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.Address
import com.example.myshoppal.utils.Constants
import kotlinx.android.synthetic.main.activity_add_edit_address.*
class AddEditAddressActivity : BaseActivity() {
    private var mAddressDetails:Address ?=null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)
        setUpActionBar()
        btn_submit_address.setOnClickListener{
            saveDataOnFireStore()
        }
        rg_type.setOnCheckedChangeListener {_, checkedID ->
            if (checkedID==R.id.rb_other_add_edit_address_activity){
                til_other_details.visibility= View.VISIBLE
            }else{
                til_other_details.visibility= View.GONE
            }
        }
        if(intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)){
            mAddressDetails=intent.getParcelableExtra((Constants.EXTRA_ADDRESS_DETAILS))
        }
        if (mAddressDetails!=null){
            if(mAddressDetails!!.id.isNotEmpty()){
                tv_title_add_edit_address_activity.text=getString(R.string.title_edit_address)
                et_full_name_add_edit_address_activity.setText( mAddressDetails!!.name)
                et_phone_number_add_edit_address_activity.setText( mAddressDetails!!.phoneNumber)
                et_address_add_edit_address_activity.setText( mAddressDetails!!.address)
                et_zip_code_add_edit_address_activity.setText( mAddressDetails!!.zipCode)
                et_additional_note_add_edit_address_activity.setText( mAddressDetails!!.additionalNote)

                when(mAddressDetails!!.type){
                    Constants.HOME->rb_home_add_edit_address_activity.isChecked=true
                    Constants.OFFICE->rb_office_add_edit_address_activity.isChecked=true
                    Constants.OTHER->rb_other_add_edit_address_activity.isChecked=true
                }
                if (rb_other_add_edit_address_activity.isChecked){
                    til_other_details.visibility=View.VISIBLE
                    et_other_details_add_edit_address_activity.setText( mAddressDetails!!.otherDetails)
                }
                btn_submit_address.setText(getString(R.string.btn_lbl_update))

            }
        }

    }//onCreate()
    fun setUpActionBar(){
        setSupportActionBar(toolbar_add_edit_address_activity)
        val actionbar=supportActionBar
        if (actionbar != null)
        {
            //Params:showHomeAsUp –>if true-->> to show the user that selecting home will return one level up rather than to the top level of the app.
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_set_home_as_up_indicator)
        }
        toolbar_add_edit_address_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    private fun saveDataOnFireStore(){
        val fullName:String=et_full_name_add_edit_address_activity.text.toString().trim { it <= ' '}
        val phoneNumber:String=et_phone_number_add_edit_address_activity.text.toString().trim { it <= ' '}
        val address:String=et_address_add_edit_address_activity.text.toString().trim { it <= ' '}
        val zipCode:String=et_zip_code_add_edit_address_activity.text.toString().trim { it <= ' '}
        val additionalNote:String=et_additional_note_add_edit_address_activity.text.toString().trim { it <=  ' '}
        val otherDetails:String=et_other_details_add_edit_address_activity.text.toString().trim { it <=  ' '}

        if (validateAddressDetails()) {
            showProgressDialog(resources.getString(R.string.please_wait))
            val addressType:String=when{
                rb_home_add_edit_address_activity.isChecked->Constants.HOME
                rb_office_add_edit_address_activity.isChecked->Constants.OFFICE
                else -> {Constants.OTHER}
            }
            val addressModel=Address(
                user_id =FireStoreClass().getCurrentUserID(),
                name = fullName, phoneNumber =phoneNumber, zipCode = zipCode, address = address,
                additionalNote = additionalNote, otherDetails = otherDetails,type=addressType
            )
            if (mAddressDetails!=null&&mAddressDetails!!.id.isNotEmpty())
                FireStoreClass().updateAddress(this@AddEditAddressActivity,addressModel,mAddressDetails!!.id)
            else
                FireStoreClass().addAddress(this@AddEditAddressActivity,addressModel)
        }
    }
    private fun validateAddressDetails():Boolean{
        return when{
            TextUtils.isEmpty(et_full_name_add_edit_address_activity.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_full_name),true)
                false
            }
            TextUtils.isEmpty(et_phone_number_add_edit_address_activity.text.toString().trim { it <= ' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_phone_number),true)
                false
            }
            TextUtils.isEmpty(et_address_add_edit_address_activity.text.toString().trim { it <= ' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_address),true)
                false
            }
            TextUtils.isEmpty(et_zip_code_add_edit_address_activity.text.toString().trim { it <= ' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_zip_code),true)
                false
            }


            rb_other_add_edit_address_activity.isChecked &&
                    TextUtils.isEmpty(et_zip_code_add_edit_address_activity.text.toString().trim { it <= ' '})->{
                    showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_zip_code),true)
                false
            }
            else -> {
                true
            }
        }
    }//function
    fun addUpdateAddressSuccess() {
        hideProgressDialog()
        val notifySuccessMessage:String=if (mAddressDetails!=null&&mAddressDetails!!.id.isNotEmpty()){
            resources.getString(R.string.msg_your_address_updated_successfully)
        }else{
            resources.getString(R.string.msg_your_address_updated_successfully)
        }

        Toast.makeText(this@AddEditAddressActivity,notifySuccessMessage,Toast.LENGTH_LONG).show()
        finish()
    }
}