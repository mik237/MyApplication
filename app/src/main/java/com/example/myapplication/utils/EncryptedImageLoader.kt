package com.example.myapplication.utils


import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Parcelable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.HttpException
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.signature.ObjectKey
import com.google.gson.annotations.SerializedName
//import com.reasonlabs.familykeeper.data.remote.ApiManager
//import com.reasonlabs.familykeeper.utils.E2EEUtils
//import com.reasonlabs.familykeeper.utils.extensions.TAG
//import com.reasonlabs.familykeeper.utils.extensions.encodeToBase64
import kotlinx.parcelize.Parcelize
import okhttp3.*
import java.io.IOException
import java.io.InputStream

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(
            ImageModel::class.java,
            InputStream::class.java,
            EncryptedImageLoader.Factory(OkHttpClient.Builder().build())
        )
    }
}

class EncryptedImageLoader private constructor(private val client: OkHttpClient) :
    ModelLoader<ImageModel, InputStream> {

    class Factory(private val client: OkHttpClient) : ModelLoaderFactory<ImageModel, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ImageModel, InputStream> {
            return EncryptedImageLoader(client)
        }

        override fun teardown() {
            // Do nothing, this instance doesn't own the client.
        }
    }

    override fun buildLoadData(
        displayImage: ImageModel,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream> {
        val url = displayImage.imageUrl
        return ModelLoader.LoadData(ObjectKey(url), EncryptedImageFetcher(client, displayImage))
    }

    override fun handles(model: ImageModel): Boolean {
        return true
    }
}


class EncryptedImageFetcher(
    private val client: Call.Factory,
    private val displayImage: ImageModel
) : DataFetcher<InputStream> {

    private var stream: InputStream? = null
    private var responseBody: ResponseBody? = null

    @Volatile
    private var call: Call? = null


    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        val request = Request.Builder()
            .url(displayImage.imageUrl)
            .addHeader("Authorization", "Bearer ${displayImage.authToken}")
            //.addHeader(ApiManager.HEADER_KEY_MASTER_KEY_VERSION, "${displayImage.mkv}")
            //.addHeader(ApiManager.HEADER_KEY_X_ALB_ACCESS, ApiManager.HEADER_KEY_X_ALB_ACCESS_VALUE)
            .build()

        call = client.newCall(request)
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onLoadFailed(e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                responseBody = response.body
                if (response.isSuccessful) {
                    responseBody?.let {
                        val a = it.bytes()
                        val str = String(it.byteStream().readBytes())//.encodeToBase64()
//                        val enStr = E2EEUtils.decryptAES(str, displayImage.masterKey)
//                        val stream = E2EEUtils.decryptInputStream(
//                            it.byteStream(),
//                            displayImage.masterKey
//                        )
//                        callback.onDataReady(it.byteStream())
                        callback.onDataReady(stream)
                    } ?: callback.onLoadFailed(HttpException("respondBody is null", 404))
                } else {
                    callback.onLoadFailed(HttpException(response.message, response.code))
                }
            }
        })
    }

    override fun cleanup() {
        stream?.close()
        responseBody?.close()
    }

    override fun cancel() {
        call?.cancel()
    }

    override fun getDataClass(): Class<InputStream> = InputStream::class.java


    override fun getDataSource(): DataSource = DataSource.REMOTE
}


@Parcelize
data class ImageModel(
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("authToken")
    val authToken: String = "",
    @SerializedName("mkv")
    val mkv: Int? = null,
    @SerializedName("masterKey")
    val masterKey: String = ""
) : Parcelable