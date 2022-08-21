package com.example.myshoppal.firestore
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.myshoppal.model.CartItem
import com.example.myshoppal.model.Product
import com.example.myshoppal.model.User
import com.example.myshoppal.ui.activites.*
import com.example.myshoppal.ui.fragments.DashboardFragment
import com.example.myshoppal.ui.fragments.ProductsFragment
import com.example.myshoppal.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireStoreClass {

   private val mFirestore=FirebaseFirestore.getInstance()
   fun removeItemFromCart(context:Context,cart_id:String){
         mFirestore.collection(Constants.CART_ITEMS)
             .document(cart_id)
             .get()
             .addOnSuccessListener {
                 when(context){
                     is CartListActivity ->{
                         context.removeItemFromCartSuccess()
                     }
                 }
             }
             .addOnFailureListener {
                 when(context){
                     is CartListActivity ->{
                         context.removeItemFromCartSuccess()
                         context.hideProgressDialog()
                         Log.e(context.javaClass.simpleName,"Error while deleting cart item")


                     }
                 }
             }
    }//removeItemFromCart()
   fun getCartList(activity: CartListActivity){
       mFirestore.collection(Constants.CART_ITEMS)
           .whereEqualTo(Constants.USER_ID,getCurrentUserID())
           .get()
           .addOnSuccessListener {querySnapshot->
               if (querySnapshot.documents.size > 0){
                   val list:ArrayList<CartItem> = ArrayList()
                   for (i in querySnapshot.documents){
                       val cartItem=i.toObject(CartItem::class.java)!!
                      //i.id -->>> Returns The id of the document.
                       cartItem.id=i.id
                       list.add(cartItem)
                   }
                   activity.getCartListSuccess(list)
               }
           }
           .addOnFailureListener {
               activity.hideProgressDialog()
               Log.e(activity.javaClass.simpleName,"Error while getting the cart list",it)
           }
   }
   fun getAllProductsList(activity: CartListActivity){
       mFirestore.collection(Constants.PRODUCTS )
           .get()
           .addOnSuccessListener {querySnapShot ->
               Log.e("Products List",querySnapShot.documents.toString())
               val list:ArrayList<Product> =ArrayList()
               for (i in querySnapShot.documents){
                   val product=i.toObject(Product::class.java)
                   if (product != null) {
                       //i.d >> document id
                       product.product_id=i.id
                       list.add(product)
                   }
               }
               activity.getAllProductsListSuccess(list)
           }
           .addOnFailureListener {

               activity.hideProgressDialog()
               Log.e(activity.javaClass.simpleName,"Error while getting the products list")
           }
   }




   fun checkIfItemExistsInCart(activity: ProductDetailsActivity,productID: String){
       mFirestore.collection(Constants.CART_ITEMS)
           //it should display if it is not our own product or already have that in our cart.
      /*where to meaning
        Creates and returns a new Query with the additional filter that documents must contain the specified field and
        the value should be equal to the specified value.
        Params:
        field – The name of the field to compare
        value – The value for comparison
        Returns:
        The created Query.
       */

           .whereEqualTo(Constants.USER_ID,getCurrentUserID())
           .whereEqualTo(Constants.PRODUCT_ID,productID)
           .get()

           .addOnSuccessListener { createdQuery ->
               Log.e(activity.javaClass.simpleName,createdQuery.documents.toString())
               if (createdQuery.documents.size > 0){
                   //that means that the product is in our cart
                   activity.productExistsInCart()
               }else{
                   activity.hideProgressDialog()
               }
           }
           .addOnFailureListener {
                   e->
               activity.hideProgressDialog()
               Log.e(activity.javaClass.simpleName,"Error while checking the existing cart list")
           }

    }//checkIfItemExistsInCart()
   fun addCartItems(activity: ProductDetailsActivity ,cartItem: CartItem){
        mFirestore.collection(Constants.CART_ITEMS)
            .document()
            .set(cartItem, SetOptions.merge())
            .addOnSuccessListener {
                activity.addTocartSuccess()
            }
            .addOnFailureListener {
                 activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error when uploading a cart item",it)
            }


    }
   fun registerUser(activity: RegisterActivity, userInfo:User){
      //the users is a collection name .if the collection is already created then it will not create the same one.
      mFirestore.collection(Constants.USERS)
      //document id for the users fields here the document is the user id
                .document(userInfo.id)
      //if we want to merge later more user info like the image or the gender or mobile if we didn't do that at he registration phase
                .set(userInfo, SetOptions.merge())

                .addOnSuccessListener {
                   //here call a function from the base activity for transferring the result in it
                   activity.userRegistrationSuccess()
                }
               .addOnFailureListener {
                  activity.hideProgressDialog()
                  Log.e(activity.javaClass.simpleName,"error when registration the user",it)
               }
   }//register user()
   fun uploadProductDetails(activity: AddProductActivity,productDetails:Product){
       mFirestore.collection(Constants.PRODUCTS).document()
           .set(productDetails)
           .addOnSuccessListener {task->
               activity.productUploadSuccess()
           }
           .addOnFailureListener {e->
               activity.hideProgressDialog()
               Log.e(activity::class.java.simpleName,"Error while uploading the product",e)
           }
   }//uploadProductDetails()
   fun getCurrentUserID():String{
       //an instance of current user using firebaseAuth
       val currentUser=FirebaseAuth.getInstance().currentUser
       //a variable to assign the current user id if it is not null or else it will be black
       var currentUserID=""
       if (currentUser!=null){
           currentUserID=currentUser.uid
       }
       return currentUserID
   }
   fun getUserDetails(activity: Activity){
    //here we pass the collection name from which we wants the data.
    mFirestore.collection(Constants.USERS)
        //THE DOCUMENT ID TO GET THE FIELDS FROM THE USER.
        .document(getCurrentUserID())
        .get()
        .addOnSuccessListener { document->
            Log.i(activity.javaClass.simpleName,document.toString())
            //here we have received the document snapShot which is converted into the user data model object.
            val user=document.toObject(User::class.java)
            val sharedPreferences=activity.getSharedPreferences(
                    Constants.MY_SHOP_PAL_PREFRENCES, Context.MODE_PRIVATE
            )
            val editor:SharedPreferences.Editor=sharedPreferences.edit()
            editor.putString(Constants.LOGGED_IN_USERNAME,"${user!!.firstName} ${user.lastName}")
            editor.apply()
            when(activity){
                is LoginActivity ->{
                     //calling to a function of the base activity for transferring result to it
                    activity.userLoggedInSuccess(user)
                }
                is SettingsActivity -> {
                    activity.userDetailsSuccess(user)
                }
            }
        }
        .addOnFailureListener { e->
            when(activity){
                is LoginActivity ->{
                    //calling to a function of the base activity for transferring result to it
                    activity.hideProgressDialog()
                }
                is SettingsActivity -> {
                    activity.hideProgressDialog()
                }
            }
            Log.i(activity.javaClass.simpleName,"Error while getting the user details.",e)
        }
   }
   fun getProductsList(fragment:Fragment){
       mFirestore.collection(Constants.PRODUCTS)
           //here we want to get all the product that belong to the logged user
           .whereEqualTo(Constants.USER_ID,getCurrentUserID())
           .get()
           .addOnSuccessListener {document->
               Log.e("Products list",document.documents.toString())
               val productsList:ArrayList<Product> =ArrayList()

               for (i in document.documents){
                     val product=i.toObject(Product::class.java)
                     product!!.product_id=i.id
                     productsList.add(product)
               }
               when(fragment){
                   is ProductsFragment->{
                     fragment.successProductsListFromFirestore(productsList)
                   }
               }
           }

   }
   fun getDashboardItemsList(fragment:DashboardFragment){
        //here we will fetch all the products not to a certain user,In order to he can buy new ones.
        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener {document->
                Log.e(fragment.javaClass.simpleName,document.documents.toString())
                var productsList:ArrayList<Product> = ArrayList()
                for (i in document.documents){
                    var product=i.toObject(Product::class.java)!!
                    product.product_id = i.id
                    productsList.add(product)
                }
                fragment.successDashboardItemsList(productsList)
            }
            .addOnFailureListener {e->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName,"Error while getting the dashboard products!!",e)
            }
    }//getDashboardItemsList()
   fun updateUserProfileData(activity: Activity,userHashMap: HashMap<String,Any>){
       mFirestore.collection(Constants.USERS).document(getCurrentUserID())
           .update(userHashMap)
           .addOnSuccessListener {
               when(activity){
                   is UserProfileActivity ->{
                       activity.userProfileUpdateSuccess()
                   }
               }
           }
           .addOnFailureListener {
               when(activity){
                   is UserProfileActivity ->{
                       activity.hideProgressDialog()

                   }
               }
               Log.e(activity.javaClass.simpleName,"Error while updating the user details.",it)

           }
   }//updateUserProfileData()
   fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?,imageType:String) {
        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
             imageType+System.currentTimeMillis() + "."
                    + Constants.getFileExtension(activity,imageFileURI)
        )
        //adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener{taskSnapshot ->
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener{uri ->
                        // START
                        // Here call a function of base activity for transferring the result to it.
                        when (activity) {
                            is UserProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                            is AddProductActivity->{
                               activity.imageUploadSuccess(uri.toString())
                            }
                        }
                        // END
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddProductActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
   fun deleteProduct(fragment: ProductsFragment,productID:String){
         mFirestore.collection(Constants.PRODUCTS)
             .document(productID)
             .delete()
             .addOnSuccessListener {
                 fragment.successDeleteProduct()
             }
             .addOnFailureListener {e->
                fragment.hideProgressDialog()
                Log.e(fragment::class.java.simpleName,"Error while deleting the product !!",e)
             }
   }
   fun getProductDetails(activity: ProductDetailsActivity,productID: String){
       mFirestore.collection(Constants.PRODUCTS)
           .document(productID)
           .get()
           .addOnSuccessListener { document->
               Log.e(activity.javaClass.simpleName,document.toString())

               val product=document.toObject(Product::class.java)
               if (product!=null)
                   activity.productDetailsSuccess(product)

           }
           .addOnFailureListener { e->
              activity.hideProgressDialog()
              Log.e(activity::class.java.simpleName,"Sorry there is something went wrong while fetching the data from the cloud",e)
           }
   }

}