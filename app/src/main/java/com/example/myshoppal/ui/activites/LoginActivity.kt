package com.example.myshoppal.ui.activites

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.User
import com.example.myshoppal.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


@Suppress("DEPRECATION")
class LoginActivity : BaseActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        tv_forgot_password.setOnClickListener(this)
        btn_login.setOnClickListener (this)
        tv_register.setOnClickListener (this)
    }//on create ()

    override fun onClick(view: View?) {
        if (view != null){
            when(view.id)
            {
                R.id.tv_forgot_password->{
                    startActivity(Intent(this, ForgotPasswordActivity::class.java))
                }
                R.id.btn_login->{
                    loginRegisteredUser()
                }
                R.id.tv_register->{
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                }
            }
        }
    }
    private fun validateLoginDetails() :Boolean{
        return when{
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            else->{
                return true
            }

        }
    }
    private fun loginRegisteredUser(){
         if (validateLoginDetails()){
             //show the progress dialog
             showProgressDialog(resources.getString(R.string.please_wait))
             //get the text from the et and trim it
             val email=et_email.text.toString().trim { it <= ' '}
             val password=et_password.text.toString().trim { it <= ' '}
             //login using the firebase authentication
             FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                 .addOnCompleteListener{
                     task->
                     if (task.isSuccessful){
                           FireStoreClass().getUserDetails(this@LoginActivity)
                     }else{
                         hideProgressDialog()
                         showErrorSnackBar(task.exception!!.message.toString(),true)
                     }
                 }
         }
    }
    fun userLoggedInSuccess(user:User){
        hideProgressDialog()
        //redirect the user the the main screen after logged in
        if (user.profileCompleted==0){
            val intent=Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }else{
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()
     }

}