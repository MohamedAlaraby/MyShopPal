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
import com.example.myshoppal.ui.fragments.DashboardFragment
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.items_list_dashboard_frag_layout.view.*
class MyDashboardListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>,
    private val fragment: DashboardFragment
):RecyclerView.Adapter<MyDashboardListAdapter.MyViewHolder>() {
    override fun getItemCount(): Int {
       return list.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.items_list_dashboard_frag_layout, parent, false)
        )
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model=list[position]
            GlideLoader(context).loadProductPicture(model.image,holder.itemView.iv_dashboard_item_image)
            holder.itemView.tv_dashboard_item_title.text=model.title
            holder.itemView.tv_dashboard_item_price.text="$${model.price}"


            holder.itemView.setOnClickListener {
                val intent= Intent(context, ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,model.user_id)
                context.startActivity(intent)
            }
    }
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
}