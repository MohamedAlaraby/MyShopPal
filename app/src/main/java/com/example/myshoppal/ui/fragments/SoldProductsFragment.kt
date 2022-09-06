package com.example.myshoppal.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.ui.adapters.MySoldProductsAdapter
import com.myshoppal.models.SoldProduct
import kotlinx.android.synthetic.main.fragment_sold_products.*

class SoldProductsFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_products, container, false)
    }

    fun successGettingSoldProducts(soldProductList: ArrayList<SoldProduct>) {
         hideProgressDialog()
         //Here i can display the content of the list which is the sold products
         if (soldProductList.size>0){
             tv_no_sold_products_found.visibility=View.GONE
             rv_sold_product_items.visibility=View.VISIBLE
             rv_sold_product_items.layoutManager=LinearLayoutManager(requireContext())
             rv_sold_product_items.setHasFixedSize(true)
             rv_sold_product_items.adapter=MySoldProductsAdapter(
                 requireContext(),soldProductList
             )

         }else{
             tv_no_sold_products_found.visibility=View.VISIBLE
         }


    }
   private fun getSoldProductsList(){
         showProgressDialog(getString(R.string.please_wait))
         FireStoreClass().getSoldProductsList(this@SoldProductsFragment)
    }

    override fun onResume() {
        super.onResume()
        getSoldProductsList()
    }


}