package com.example.myshoppal.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myshoppal.R
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseFragment : Fragment() {
    lateinit var mProgressDialog:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }
    fun showProgressDialog(text:String){
        mProgressDialog= Dialog(requireActivity())
        mProgressDialog.apply {
            setCancelable(false)
            setContentView(R.layout.dialog_progress)
            tv_progress_text.text=text
            setCanceledOnTouchOutside(false)
            show()
        }
    }
    fun hideProgressDialog(){
      mProgressDialog.dismiss()
    }
}