package com.example.myshoppal.ui.activites

import android.app.Dialog
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*

@Suppress("DEPRECATION")
open class BaseActivity : AppCompatActivity() {
 private lateinit var mProgressDialog:Dialog
 private var doubleBackToExitPressedOnce=false

    fun showErrorSnackBar(message:String,errorMessage:Boolean){
       val snackbar=Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
       val snackBarView=snackbar.view
        if (errorMessage){
            snackBarView.setBackgroundColor(
                 ContextCompat.getColor(this@BaseActivity,R.color.colorSnackBarError)
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity,R.color.colorSnackBarSuccess)
            )
        }
        snackbar.show()
}
    fun showProgressDialog(text:String){
           mProgressDialog= Dialog(this)
           mProgressDialog.setContentView(R.layout.dialog_progress)
           mProgressDialog.tv_progress_text.text=text
           //to make the user unable to delete it
           mProgressDialog.setCancelable(false)
           mProgressDialog.setCanceledOnTouchOutside(false)
           //show
           mProgressDialog.show()
    }
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }
    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            //return means don't do any thing else in this fun
            return
        }
        this.doubleBackToExitPressedOnce=true
        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_LONG
        ).show()
        Handler(Looper.getMainLooper()).postDelayed({
           doubleBackToExitPressedOnce=false
        }, 2000)
    }
}//the class