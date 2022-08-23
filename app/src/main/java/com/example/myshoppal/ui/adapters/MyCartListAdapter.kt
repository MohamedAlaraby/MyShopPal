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
import com.example.myshoppal.utils.Constants
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
             holder.itemView.apply {
                 tv_cart_item_title.text=model.title
                 tv_cart_item_price.text="$${model.price}"
                 tv_cart_quantity.text=model.cart_quantity
            }
            if(model.cart_quantity=="0"){
                holder.itemView.apply {
                    ib_add_cart_item.visibility=View.GONE
                    ib_remove_cart_item.visibility=View.GONE
                    tv_cart_quantity.text=context.resources.getString(R.string.lbl_out_of_stock)
                    tv_cart_quantity.setTextColor(
                        ContextCompat.getColor(
                            context,R.color.colorSnackBarError
                        )
                    )
                }
            }
            else
            {
                holder.itemView.apply {
                    ib_add_cart_item.visibility=View.VISIBLE
                    ib_remove_cart_item.visibility=View.VISIBLE
                    tv_cart_quantity.setTextColor(
                        ContextCompat.getColor(
                            context,R.color.colorSecondaryText
                        )
                    )
                }
            }
            holder.itemView.ib_delete_cart_item.setOnClickListener {
                when(context){
                    is CartListActivity ->{
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                        FireStoreClass().removeItemFromCart(context,model.id)
                    }
                }
            }
        holder.itemView.ib_remove_cart_item.setOnClickListener {
            when(context){
                is CartListActivity ->{
                    if (model.cart_quantity=="1"){
                       FireStoreClass().removeItemFromCart(context,model.id)
                    }else{
                        //if quantity greater than 1
                        val cartQuantity=model.cart_quantity.toInt()
                        val itemHashMap=HashMap<String,Any>()
                        itemHashMap[Constants.CART_QUANTITY]=(cartQuantity-1).toString()
                        context.showProgressDialog(context.resources.getString(R.string.please_wait))
                        FireStoreClass().updateMyCart(context = context,
                                               itemHashMap =itemHashMap,
                                               cart_id = model.id
                        )
                    }
                }
            }
        }
        holder.itemView.ib_add_cart_item.setOnClickListener {
            when(context){
                is CartListActivity ->{
                    val cartQuantity=model.cart_quantity.toInt()
                        if (cartQuantity < model.stock_quantity.toInt()){

                            val itemHashMap=HashMap<String,Any>()
                            itemHashMap[Constants.CART_QUANTITY]=(cartQuantity+1).toString()
                            context.showProgressDialog(context.resources.getString(R.string.please_wait))
                            FireStoreClass().updateMyCart(context = context,
                                itemHashMap =itemHashMap,
                                cart_id = model.id
                            )
                        }else{
                               context.showErrorSnackBar(
                                   context.resources.getString(R.string.msg_for_available_stock,model.stock_quantity),
                                   true)

                        }


                }
            }
        }


    }//on bind()
    override fun getItemCount(): Int {
       return cartList.size
    }
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}