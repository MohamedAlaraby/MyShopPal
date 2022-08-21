package com.example.myshoppal.ui.fragments
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myshoppal.R
import com.example.myshoppal.databinding.FragmentDashboardBinding
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.Product
import com.example.myshoppal.ui.activites.CartListActivity
import com.example.myshoppal.ui.activites.SettingsActivity
import com.example.myshoppal.ui.adapters.MyDashboardListAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //if we want  to use options menu in fragment  we need to use this function
        setHasOptionsMenu(true)

    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings ->{
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            R.id.action_go_to_cart ->{
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun successDashboardItemsList(dashboardItemsList:ArrayList<Product>){
        hideProgressDialog()
        if(dashboardItemsList.size > 0 ){
            rv_dashboard_items.visibility=View.VISIBLE
            tv_no_dashboard_items_found.visibility=View.GONE

            rv_dashboard_items.layoutManager= GridLayoutManager(activity,2)
            rv_dashboard_items.setHasFixedSize(true)
            val adapter= MyDashboardListAdapter(requireContext(),dashboardItemsList,this)
            rv_dashboard_items.adapter=adapter

        }else{
            rv_dashboard_items.visibility=View.GONE
            tv_no_dashboard_items_found.visibility=View.VISIBLE
        }
    }//successDashboardItemsList
    fun getDashboardItemsList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getDashboardItemsList(this)
    }
    override fun onResume() {
        super.onResume()
        getDashboardItemsList()
    }

}