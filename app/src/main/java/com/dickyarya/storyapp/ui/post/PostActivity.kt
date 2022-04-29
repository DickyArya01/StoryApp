package com.dickyarya.storyapp.ui.post

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dickyarya.storyapp.api.Api
import com.dickyarya.storyapp.databinding.ActivityPostBinding
import com.dickyarya.storyapp.model.PostResponse
import com.dickyarya.storyapp.preferences.LoginPreference
import com.dickyarya.storyapp.ui.main.StoryActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private lateinit var preference: LoginPreference
    private var getFile: File? = null

    companion object{
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if (!allPermissionGranted()){
                showToast("Permission not granted")
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = LoginPreference(this)

        if (!allPermissionGranted()){
           requestPermission()
        }

        setActionBar()

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnUpload.setOnClickListener {
            if(binding.etDescription.text.isNullOrEmpty()){
                showToast("Description must be filled")
            }else{
                uploadImage()
            }
        }
    }

    private fun reduceSizeImage(file: File): File{
        return file
    }

    private fun uploadImage(){
        if (getFile != null){
            showLoading(true)

            val file = reduceSizeImage( getFile as File)

           val description: String = binding.etDescription.text.toString()
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            val mTOKEN: String= preference.getString(LoginPreference.TOKEN).toString()

            val token = "Bearer $mTOKEN"

           val service =
               Api.getApi().postStory(token, imageMultipart, description)
            service.enqueue(object : Callback<PostResponse>{
                override fun onResponse(
                    call: Call<PostResponse>,
                    response: Response<PostResponse>
                ) {
                    showLoading(false)
                    moveIntentStory()
                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }else{
            showToast("Please input image first")
        }
    }

    private fun moveIntentStory() {
       Intent(this@PostActivity, StoryActivity::class.java).also {
           startActivity(it)
       }
    }

    private fun startGallery(){
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraActivity.launch(intent)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val selectedImg: Uri = it.data?.data as Uri
            val myPhoto = uriToFile(selectedImg, this@PostActivity)

            getFile = myPhoto

            binding.ivImageView.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCameraActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
       if(it.resultCode == CAMERA_X_RESULT){
           val myPhoto = it.data?.getSerializableExtra("picture") as File
           val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

           getFile = myPhoto
           val result = rotateBitmap(
               BitmapFactory.decodeFile(myPhoto.path),
               isBackCamera
           )

           binding.ivImageView.setImageBitmap(result)
       }
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
        )
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all{
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setActionBar(){
        supportActionBar?.title = "Post Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun showToast(text: String){
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(state: Boolean){
        if(state){
            binding.progressBar4.visibility = View.VISIBLE
        }else{
            binding.progressBar4.visibility = View.GONE
        }
    }

}