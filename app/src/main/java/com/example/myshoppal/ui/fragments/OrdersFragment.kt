package com.example.myshoppal.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.Order
import com.example.myshoppal.ui.adapters.MyOrdersListAdapter
import kotlinx.android.synthetic.main.fragment_orders.*

class OrdersFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.fragment_orders, container, false)
        return root
    }
    fun populateOrdersInUI(ordersList:ArrayList<Order>){
        hideProgressDialog()
        if (ordersList.size>0){

                fragment_orders_rv_my_order_items.visibility=View.VISIBLE
                fragment_orders_tv_no_orders_found.visibility=View.GONE
                fragment_orders_rv_my_order_items.layoutManager=LinearLayoutManager(requireActivity())
                fragment_orders_rv_my_order_items.setHasFixedSize(true)
                fragment_orders_rv_my_order_items.adapter=MyOrdersListAdapter(requireActivity(),ordersList)


        }else{
            fragment_orders_tv_no_orders_found.visibility=View.VISIBLE
            fragment_orders_rv_my_order_items.visibility=View.GONE
        }
    }
    private fun getMyOrdersList(){
        showProgressDialog(getString(R.string.please_wait))
        FireStoreClass().getMyOrdersList(this)
    }
    override fun onResume() {
        super.onResume()
        getMyOrdersList()
    }

}