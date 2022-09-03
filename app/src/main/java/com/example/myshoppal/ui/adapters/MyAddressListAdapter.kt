package com.example.myshoppal.ui.adapters
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myshoppal.R
import com.example.myshoppal.model.Address
import com.example.myshoppal.ui.activites.AddEditAddressActivity
import com.example.myshoppal.ui.activites.CheckoutActivity
import com.example.myshoppal.utils.Constants
import kotlinx.android.synthetic.main.item_address_layout.view.*
class MyAddressListAdapter(val context:Context,var list:ArrayList<Address>,private val selectedAddress:Boolean):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_address_layout,parent,false)
        )
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       val model= list[position]
       if (holder is MyViewHolder){
          holder.itemView. tv_address_full_name_item_address.text=model.name
          holder.itemView. tv_address_type_item_address.text=model.type
          holder.itemView.tv_address_mobile_number_item_address.text=model.phoneNumber
          holder.itemView.tv_address_details_item_address.text="${model.address} ${model.zipCode}"
         if (selectedAddress){
             holder.itemView.setOnClickListener {
                   Toast.makeText(context,
                       "selected address ${model.address} , ${model.zipCode}",
                       Toast.LENGTH_LONG).show()
                       val intent=Intent(context,CheckoutActivity::class.java)
                       intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS,model)
                       context.startActivity(intent)

             }
         }



       }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    fun notifyEditItem(activity:Activity,position: Int){
           val intent=Intent(context,AddEditAddressActivity::class.java)
           intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS,list[position])
           activity.startActivityForResult(intent,Constants.ADD_ADDRESS_REQUEST_CODE)
           notifyItemChanged(position)
    }

}