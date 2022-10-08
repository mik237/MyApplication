package com.example.myapplication.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImageUploadApiService {

    @Multipart
    @POST("AddCustomer.php")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("customer_name") name: RequestBody,
        @Part("reference") reference: RequestBody,
    ): Response<UploadImageResponse>
}