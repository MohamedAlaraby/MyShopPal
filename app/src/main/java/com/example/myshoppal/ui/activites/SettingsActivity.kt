package com.example.myshoppal.ui.activites
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.User
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
class SettingsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var  mUserDetails:User
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setUpActionBar()
        btn_logout.setOnClickListener(this)
        tv_edit.setOnClickListener(this)
    }//on create method
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_logout ->
                {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    //FLAG_ACTIVITY_NEW_TASK >>add new activity lifecycle to the stack
                    //FLAG_ACTIVITY_CLEAR_TASK>>remove all the previous activities in the stack
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    //finish()
                }
                R.id.tv_edit ->
                {
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }
            }
        }
    }



    fun setUpActionBar(){
        setSupportActionBar(toolbar_settings_activity)
        val actionbar=supportActionBar
        if (actionbar != null){

        //Params:showHomeAsUp –>if true-->> to show the user that selecting home will return one level up rather than to the top level of the app.
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_set_home_as_up_indicator)
        }
        toolbar_settings_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
    fun getUserDetails(){
         showProgressDialog(resources.getString(R.string.please_wait))
         FireStoreClass().getUserDetails(this)

    }
    fun userDetailsSuccess(user: User){
        mUserDetails=user
        hideProgressDialog()
        GlideLoader(this).loadUserPicture(user.image,iv_user_photo)
        tv_name.text="${user.firstName} ${user.lastName}"
        tv_email.text="${user.email}"
        tv_gender.text="${user.gender}"
        tv_mobile_number.text="${user.mobile}"

    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

}