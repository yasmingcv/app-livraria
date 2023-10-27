package br.senai.sp.jandira.apilivraria

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
interface ApiService {

    @POST("/categoria/cadastrarCategoria")
    suspend fun createCategory(@Body body: JsonObject): Response<JsonObject>



}