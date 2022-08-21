package com.example.myshoppal.ui.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.CartItem
import com.example.myshoppal.ui.activites.CartListActivity
import com.example.myshoppal.ui.adapters.MyCartListAdapter.CartViewHolder
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_cart_layout.view.*
class MyCartListAdapter(
   val context:Context,
   val cartList:ArrayList<CartItem>
): RecyclerView.Adapter<CartViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_list_cart_layout,parent,false)
        )
    }
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
            val model=cartList[position]
            GlideLoader(context).loadProductPicture(model.image,holder.itemView.iv_cart_item_image)

            holder.itemView.tv_cart_item_title.text=model.title
            holder.itemView.tv_cart_item_price.text="$${model.price}"

            holder.itemView.tv_cart_quantity.text=model.cart_quantity
            if (model.cart_quantity=="0"){
                holder.itemView.ib_add_cart_item.visibility=View.GONE
                holder.itemView.ib_remove_cart_item.visibility=View.GONE
                holder.itemView.tv_cart_quantity.text=context.resources.getString(R.string.lbl_out_of_stock)
                holder.itemView.tv_cart_quantity.setTextColor(
                    ContextCompat.getColor(
                        context,R.color.colorSnackBarError
                    )
                )

            }else{
                holder.itemView.ib_add_cart_item.visibility=View.VISIBLE
                holder.itemView.ib_remove_cart_item.visibility=View.VISIBLE

                holder.itemView.tv_cart_quantity.setTextColor(
                    ContextCompat.getColor(
                        context,R.color.colorSecondaryText
                    )
                )
            }
            holder.itemView.ib_delete_cart_item.setOnClickListener {
                when(context){
                    is CartListActivity ->{
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                        FireStoreClass().removeItemFromCart(context,model.id)
                    }
                }


            }

    }
    override fun getItemCount(): Int {
       return cartList.size
    }
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}