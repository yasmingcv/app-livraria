package br.senai.sp.jandira.apilivraria

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("data")
    var data: T? = null
)
