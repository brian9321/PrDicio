package com.example.fotoapp.Flujocamera.Api
import com.example.fotoapp.Flujocamera.Models.DatosPersonales
import com.example.fotoapp.Flujocamera.Models.ResponseData
import com.example.fotoapp.Flujocamera.Models.listOfUsers
import com.example.fotoapp.network.AppConstants
import com.example.fotoapp.network.NetworkConnectionInterceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface WSUser {
    @POST("sec_dev_interview")
    @Headers(
        "Content-Type: application/json",
        "Host: api.devdicio.net",
        "xc-token: J38b4XQNLErVatKIh4oP1jw9e_wYWkS86Y04TMNP"
    )
    suspend fun createUser(
        @Body datosPersonales: DatosPersonales
    ): Response<ResponseData>

    @GET("sec_dev_interview")
    @Headers(
        "Content-Type: application/json",
        "Host: api.devdicio.net",
        "xc-token: J38b4XQNLErVatKIh4oP1jw9e_wYWkS86Y04TMNP"
    )
    suspend fun getLatestUsers(
        @Query("limit") limit: Int = 20,
        @Query("sort") sort: String = "-id"
    ): Response<List<ResponseData>>

    companion object{
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): WSUser {
            val okkHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpClient)
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WSUser::class.java)
        }
    }
}