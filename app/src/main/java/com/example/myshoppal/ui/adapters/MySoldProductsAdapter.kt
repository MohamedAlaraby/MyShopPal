package com.example.myshoppal.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.R
import com.example.myshoppal.ui.activites.SoldProductDetailsActivity
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import com.myshoppal.models.SoldProduct
import kotlinx.android.synthetic.main.items_list_products_frag_layout.view.*

class MySoldProductsAdapter(
   private val context: Context,
   private val  list:ArrayList<SoldProduct>
) :RecyclerView.Adapter<MySoldProductsAdapter.MyVH>(){





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
      return MyVH(LayoutInflater.from(context).inflate(R.layout.items_list_products_frag_layout,
          parent,false))
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        val model = list[position]
        holder.itemView.apply {
            GlideLoader(context).loadProductPicture(model.image, this.iv_item_image)
            tv_item_name.text = model.title
            tv_item_price.text = "$${model.price}"
            ib_item_product_delete_icon.visibility = View.GONE
            this.setOnClickListener {
                //sold product details intent
                val intent = Intent(context, SoldProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_SOLD_PRODUCT_ITEM, model)
                context.startActivity(intent)
            }
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    public class MyVH(itemView: View):RecyclerView.ViewHolder(itemView)
}