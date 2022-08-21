package com.example.myshoppal.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.databinding.FragmentProductsBinding
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.Product
import com.example.myshoppal.ui.activites.AddProductActivity
import com.example.myshoppal.ui.adapters.MyProductsListAdapter
import kotlinx.android.synthetic.main.fragment_products.*

class ProductsFragment : BaseFragment() {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_product ->{
                startActivity(Intent(activity,AddProductActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun successProductsListFromFirestore(productsList:ArrayList<Product> ){
        hideProgressDialog()
        if (productsList.size > 0 ){
            rv_my_product_items.visibility=View.VISIBLE
            tv_no_products_found.visibility=View.GONE

            rv_my_product_items.layoutManager=LinearLayoutManager(activity)
            rv_my_product_items.setHasFixedSize(true)
            rv_my_product_items.adapter=MyProductsListAdapter(requireContext(),productsList,this)

        }else{
            rv_my_product_items.visibility=View.GONE
            tv_no_products_found.visibility=View.VISIBLE
        }
    }
    fun getProductsListFromFirestore(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getProductsList(this)
    }
    override fun onResume() {
        super.onResume()
        getProductsListFromFirestore()
    }
    fun deleteProduct(productID:String){
        showAlertDialogToDeleteProductU(productID)
    }
    fun successDeleteProduct(){
         hideProgressDialog()
         Toast.makeText(context,getString(R.string.product_deleted_successfully),Toast.LENGTH_LONG).show()
         getProductsListFromFirestore()
    }
    fun showAlertDialogToDeleteProductU(productID: String){
        val builder= AlertDialog.Builder(requireActivity())
        builder.apply {
            setTitle(resources.getString(R.string.delete_dialog_title))
            setMessage(resources.getString(R.string.delete_dialog_message))
            setIcon(android.R.drawable.ic_dialog_alert)
            //performing a positive action
            setPositiveButton(resources.getString(R.string.yes)){dialogInterface,_->
                showProgressDialog(resources.getString(R.string.please_wait))
                dialogInterface.dismiss()
                FireStoreClass().deleteProduct(this@ProductsFragment,productID)
            }
            //performing a negative action
            setNegativeButton(resources.getString(R.string.no)){dialogInterface,_->
                dialogInterface.dismiss()
            }
            //create alert dialog
            val alertDialog:AlertDialog=builder.create()
            //set other dialog properties
            alertDialog.apply {
                setCancelable(false)
                show()
            }


        }




    }
}