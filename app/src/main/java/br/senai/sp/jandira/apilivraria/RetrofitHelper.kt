package br.senai.sp.jandira.apilivraria
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private const val baseurl = "http://10.107.144.6:8080"

    fun getInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}