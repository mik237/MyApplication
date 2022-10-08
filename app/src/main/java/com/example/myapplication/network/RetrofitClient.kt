package com.example.myapplication.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val UPLOAD_IMAGE_BASE_URL = "http://75.119.143.175:8080/ErpNext/"
    fun getImageUploadApiService(): ImageUploadApiService{
        val retrofit = Retrofit.Builder()
            .baseUrl(UPLOAD_IMAGE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ImageUploadApiService::class.java)
    }
}