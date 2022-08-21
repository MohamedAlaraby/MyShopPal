package com.example.myshoppal.ui.activites
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshoppal.R
import com.example.myshoppal.firestore.FireStoreClass
import com.example.myshoppal.model.User
import com.example.myshoppal.utils.Constants
import com.example.myshoppal.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException
class UserProfileActivity : BaseActivity(),View.OnClickListener {
    private lateinit var mUserDetails:User
    //the uri from your own mobile gallery
    private  var mSelectedImageFileUri:Uri?=null
    //the url from the web browser
    private  var mUserProfileImageURL:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_user_profile)
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        // If the profile is incomplete then user is from login screen and wants to complete the profile.
        if (mUserDetails.profileCompleted == 0) {
            // Update the title of the screen to complete profile.
            tv_title_user_profile_activity.text = resources.getString(R.string.title_complete_profile)
            // Here, the some of the edittext components are disabled because it is added at a time of Registration.
            et_first_name_profile_activity.isEnabled = false
            et_first_name_profile_activity.setText(mUserDetails.firstName)

            et_last_name.isEnabled = false
            et_last_name.setText(mUserDetails.lastName)

            et_email.isEnabled = false
            et_email.setText(mUserDetails.email)
        } else {
            // Call the setup action bar function.
            setupActionBar()
            // Update the title of the screen to edit profile.
            tv_title_user_profile_activity.text = resources.getString(R.string.title_edit_profile)

            // Load the image using the GlideLoader class with the use of Glide Library.
            GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.image,
                iv_user_profile_photo)
            // Set the existing values to the UI and allow user to edit except the Email ID.
            et_first_name_profile_activity.setText(mUserDetails.firstName)
            et_last_name.setText(mUserDetails.lastName)

            et_email.isEnabled = false
            et_email.setText(mUserDetails.email)

            if (mUserDetails.mobile != 0L) {
                et_mobile_number_profile_activity.setText(mUserDetails.mobile.toString())
            }
            if (mUserDetails.gender == Constants.MALE) {
                rb_male.isChecked = true
            } else {
                rb_female.isChecked = true
            }
        }
        // END

        // Assign the on click event to the user profile photo.
        iv_user_profile_photo.setOnClickListener(this@UserProfileActivity)
        // Assign the on click event to the SAVE button.
        btn_save_user_profile_details.setOnClickListener(this@UserProfileActivity)
    }//end of on create method

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_user_profile_photo -> {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this@UserProfileActivity)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            Constants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE
                        )
                    }
                }
                R.id.btn_save_user_profile_details -> {
                    if (validateUserProfileDetails()) {
                        // Show the progress dialog.
                        showProgressDialog(resources.getString(R.string.please_wait))
                        if (mSelectedImageFileUri != null) {
                            FireStoreClass().uploadImageToCloudStorage(
                                this@UserProfileActivity,
                                mSelectedImageFileUri,Constants.USER_PROFILE_IMAGE
                            )
                        } else {
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@UserProfileActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.permission_denied_string),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }//onRequestPermissionsResult

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode ==RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {

                        // The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!

                        GlideLoader(this@UserProfileActivity).loadUserPicture(
                            mSelectedImageFileUri!!,
                            iv_user_profile_photo
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode ==RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }
    private fun setupActionBar() {
        setSupportActionBar(toolbar_user_profile_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title=""
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_set_home_as_up_indicator)
        }

        toolbar_user_profile_activity.setNavigationOnClickListener { onBackPressed() }
    }//onActivityResult
    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_mobile_number_profile_activity.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }
    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()
        // Get the FirstName from editText and trim the space
        val firstName = et_first_name_profile_activity.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstName) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }
        // Get the LastName from editText and trim the space
        val lastName = et_last_name.text.toString().trim { it <= ' ' }
        if (lastName != mUserDetails.lastName) {
            userHashMap[Constants.LAST_NAME] = lastName
        }
        // Here we get the text from editText and trim the space
        val mobileNumber = et_mobile_number_profile_activity.text.toString().trim { it <= ' ' }
        val gender = if (rb_male.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }
        if (mUserProfileImageURL!!.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL!!
        }
        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        if (gender.isNotEmpty() && gender != mUserDetails.gender) {
            userHashMap[Constants.GENDER] = gender
        }
        if (mUserDetails.profileCompleted == 0) {
            userHashMap[Constants.PROFILE_COMPLETED] = 1
        }
        // call the registerUser function of FireStore class to make an entry in the database.
        FireStoreClass().updateUserProfileData(
            this@UserProfileActivity,
            userHashMap
        )
    }//end of updateUserProfileDetails()
    fun userProfileUpdateSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.profile_updated_successfully),
            Toast.LENGTH_SHORT
        ).show()


        // TODO Step 8: Redirect it to the DashboardActivity instead of MainActivity.
        // Redirect to the Main Screen after profile completion.
        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()
    }
    fun imageUploadSuccess(imageURL: String) {

        mUserProfileImageURL = imageURL

        updateUserProfileDetails()
    }
}