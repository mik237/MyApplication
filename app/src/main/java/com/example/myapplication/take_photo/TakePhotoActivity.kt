package com.example.myapplication.take_photo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.custom_views.TextDrawable
import com.example.myapplication.databinding.ActivityTakePhotoBinding
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class TakePhotoActivity : AppCompatActivity() {

    lateinit var binding: ActivityTakePhotoBinding
    private var latestTmpUri: Uri? = null

    private var avatarSet = false;

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    binding.imagePreview.setImageURI(uri)
                }
            }
        }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            latestTmpUri = uri
            avatarSet = false
            uri?.let { binding.imagePreview.setImageURI(uri) }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakePhotoBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setClickListener()

        val textDrawable =
            TextDrawable(
                this@TakePhotoActivity,
                "M",
                ContextCompat.getColor(this, R.color.textBg),
                ContextCompat.getColor(this, R.color.textColor), 20F
            )

        binding.imagePreview.setImageDrawable(textDrawable)


    }

    private fun setClickListener() {
        binding.selectImageButton.setOnClickListener { takeImage() }
        binding.takeImageButton.setOnClickListener { selectImageFromGallery() }
        binding.btnSetAvatar.setOnClickListener {
            avatarSet = true
            binding.imagePreview.setImageResource(R.drawable.ic_avatar_1)
        }
        binding.btnUpload.setOnClickListener {
            if(avatarSet)
                uploadAvatar()
                else
                    uploadImge() }
    }

    private fun uploadAvatar() {
            //get bitmap drawable
        val bitmap = binding.imagePreview.drawable.toBitmap()
        lifecycleScope.launch (Dispatchers.IO){
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            val bitmapArray = bos.toByteArray()
            val file = File.createTempFile("avatar", ".png", cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }
            try {
                val fos = FileOutputStream(file)
                fos.write(bitmapArray)
                fos.flush()
                fos.close()
                bos.flush()
                bos.close()
            }
            catch (e: Exception){}

            val name = "ABC".toRequestBody(MultipartBody.FORM)
            val reference = "Islamabad".toRequestBody(MultipartBody.FORM)

            var mediaType = ".jpg"


            val requestFile = file.asRequestBody(mediaType.toMediaTypeOrNull())

            // MultipartBody.Part is used to send also the actual file name
             val image = MultipartBody.Part.createFormData("image", file.name, requestFile)
            //Now I have a file.
                val response = RetrofitClient.getImageUploadApiService().uploadImage(image, name, reference)
                if(response.isSuccessful){
                    Log.d("TAG", "uploadImge: ${response.body().toString()}")
//                    Toast.makeText(this@TakePhotoActivity, "Success", Toast.LENGTH_LONG).show()
                }
                else{
                    Log.d("TAG", "uploadImge: ${response.errorBody().toString()}")
//                    Toast.makeText(this@TakePhotoActivity, "Failed", Toast.LENGTH_LONG).show()
                }

        }


    }

    private fun uploadImge() {

        val name = "ABC".toRequestBody(MultipartBody.FORM)
        val reference = "Islamabad".toRequestBody(MultipartBody.FORM)
        latestTmpUri?.let {imgUri->
            lifecycleScope.launchWhenStarted {
                val image = prepareFilePart( "image", imgUri)

                if(image!=null){
                    val response = RetrofitClient.getImageUploadApiService().uploadImage(image, name, reference)
                    if(response.isSuccessful){
                        Log.d("TAG", "uploadImge: ${response.body().toString()}")
                        Toast.makeText(this@TakePhotoActivity, "Success", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this@TakePhotoActivity, "Failed", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(this@TakePhotoActivity, "Image not there", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                avatarSet=false
                takeImageResult.launch(uri)
            }
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }


    fun prepareFilePart( partName: String, fileUri: Uri): MultipartBody.Part? {

        // use the FileUtils to get the actual file by uri
//        val file = FileUtils.getFile(context, fileUri) ?: return null

        val file = File.createTempFile("image", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        // create RequestBody instance from file
        var mediaType = ".jpg"
        this.contentResolver.getType(fileUri)?.let {
            mediaType = it
        }

        val requestFile = file.asRequestBody(mediaType.toMediaTypeOrNull())

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
}