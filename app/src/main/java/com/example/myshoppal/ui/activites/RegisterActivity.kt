package com.example.myshoppal.ui.activites

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

@Suppress("DEPRECATION")
class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        tv_login.setOnClickListener {
            onBackPressed()
        }
      setupActionBar()
      btn_register.setOnClickListener {
          registerUser()

      }
    }//on create
     fun setupActionBar(){
        setSupportActionBar(toolbar_register_activity)
        val actionBar =supportActionBar
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_set_home_as_up_indicator)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_register_activity.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    //a fun to validate the new user entries
     private fun validateRegisterDetails():Boolean{
        return when{
            TextUtils.isEmpty(et_first_name.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name),true)
                false
            }
            TextUtils.isEmpty(et_last_name.text.toString().trim { it <= ' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name),true)
                false
            }
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            TextUtils.isEmpty(et_confirm_password.text.toString().trim { it <= ' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password),true)
                false
            }
            et_password.text.toString().trim { it<=' '} != et_confirm_password.text.toString().trim { it<=' '} -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),true)
                false
            }
            !cb_terms_and_condition.isChecked ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition),true)
                false
            }
            else -> {
             //   showErrorSnackBar(getString(R.string.registered_successfully),false)
                true
            }
        }
     }//function
     private fun registerUser(){
     //check with validate function if the entries are validate or not
        if(validateRegisterDetails()){
            showProgressDialog(resources.getString(R.string.please_wait))

            val email:String=et_email.text.toString().trim{it<=' '}
            val password:String=et_password.text.toString().trim{it<=' '}
            //create instance and create a register user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                OnCompleteListener<AuthResult>{task ->
                    //if the registration is completed successfully
                    if(task.isSuccessful){
                        //firebase registered user
                        val firebaseUser=task.result.user
                        val user=User(
                            firebaseUser!!.uid,
                            et_first_name.text.toString().trim { it<=' '},
                            et_last_name.text.toString().trim { it<=' '},
                            et_email.text.toString().trim { it<=' '},
                        )
                        FireStoreClass().registerUser(this@RegisterActivity,user)
                       //  FirebaseAuth.getInstance().signOut()
                       // finish()
                    }
                    else{
                        //if the registration is not completed successfully
                       hideProgressDialog()
                       showErrorSnackBar("${task.exception!!.message}",true)
                    }
                }
            )
        }
    }//registerUser
     fun userRegistrationSuccess(){
        //Hide the progress bar
        hideProgressDialog()
        Toast.makeText(this,resources.getString(R.string.register_success),
            Toast.LENGTH_LONG).show()
    }
}
