package com.example.myshoppal.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.R
import com.example.myshoppal.model.Order
import com.example.myshoppal.ui.activites.MyOrderDetailsActivity
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.items_list_products_frag_layout.view.*

class MyOrdersListAdapter(
    private val context: Context, private val ordersList:ArrayList<Order>
): RecyclerView.Adapter<MyOrdersListAdapter.OrdersVH>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersVH {
        return OrdersVH(
            LayoutInflater.from(context).inflate(R.layout.items_list_products_frag_layout,parent,false)
        )
    }

    override fun onBindViewHolder(holder: OrdersVH, position: Int) {
        val model=ordersList[position]

        holder.itemView.apply {
            GlideLoader(context).loadProductPicture(model.image,this.iv_item_image)
            tv_item_name.text=model.title
            tv_item_price.text="$${model.total_amount}"
            ib_item_product_delete_icon.visibility=View.GONE
            this.setOnClickListener {
                val intent=Intent(context,MyOrderDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_MY_ORDER_DETAILS,model)
                context.startActivity(intent)

            }
        }

    }

    override fun getItemCount(): Int {
       return ordersList.size
    }
    class OrdersVH(itemView: View) : RecyclerView.ViewHolder(itemView)
}