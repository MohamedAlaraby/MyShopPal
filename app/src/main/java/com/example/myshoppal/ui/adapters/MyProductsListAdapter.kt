package com.example.myshoppal.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.R
import com.example.myshoppal.model.Product
import com.example.myshoppal.ui.activites.ProductDetailsActivity
import com.example.myshoppal.ui.fragments.ProductsFragment
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.items_list_products_frag_layout.view.*

class MyProductsListAdapter (private val context: Context,
                             private var list:ArrayList<Product>,
                             private val fragment: ProductsFragment):RecyclerView.Adapter<MyProductsListAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.items_list_products_frag_layout,parent,false)
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
             val model=list[position]
             GlideLoader(context).loadProductPicture(model.image,holder.itemView.iv_item_image)
             holder.itemView.tv_item_name.text=model.title
             holder.itemView.tv_item_price.text="$${model.price}"

             holder.itemView.ib_item_product_delete_icon.setOnClickListener {
             //delete a product
             fragment.deleteProduct(model.product_id)
             }
             holder.itemView.setOnClickListener {
                 val intent=Intent(fragment.activity,ProductDetailsActivity::class.java)
                 intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
                 intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,model.user_id)

                 fragment.activity?.startActivity(intent)
             }
    }//ON BIND VIEW HOLDER
    override fun getItemCount(): Int {
        return list.size
    }
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
}