package com.example.myshoppal.ui.activites

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.myshoppal.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

@Suppress("DEPRECATION")
class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setupActionBar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        btn_submit.setOnClickListener {
            val email=et_login_email.text.toString().trim { it<= ' ' }
            if (email.isEmpty()){
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).
                addOnCompleteListener {task->
                    hideProgressDialog()
                    if (task.isSuccessful)
                    {
                        Toast.makeText(this,"email sent successfully,check your gmail", Toast.LENGTH_LONG).show()
                        finish()
                    }else
                    {
                        Toast.makeText(this,task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


    }
    fun setupActionBar(){
        setSupportActionBar(toolbar_forgotpassword_activity)
        val actionBar =supportActionBar
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_set_home_as_up_indicator)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title=""
        }
        toolbar_forgotpassword_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}