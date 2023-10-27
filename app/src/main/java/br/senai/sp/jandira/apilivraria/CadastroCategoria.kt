package br.senai.sp.jandira.apilivraria

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class CadastroCategoria : AppCompatActivity() {

    //Criação do atributo que vai representar a API service
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_categoria)

        //Criação da instancia ativa do Retrofit
        apiService = RetrofitHelper.getInstance().create(ApiService::class.java)

        //Recupera o objeto de editText do formulário de cadastro de categoraia
        val txtCategoria = findViewById<EditText>(R.id.txtCategoria)

        //Trata o evento de clique ou toque no botão cadastrar
        findViewById<Button>(R.id.btnCadastrarCategoria).setOnClickListener{

            //Recupera o valor digitado pelo usuário
            val nomeCategoria = txtCategoria.text

            //Envia o dado de categoria para cadastro na API
            createCategory(nomeCategoria.toString())

        }
    } //onCreate

    private fun createCategory(nome_categoria: String){

        lifecycleScope.launch {

            //Criação do corpo de dados em formato JSON
            val body = JsonObject().apply {
                addProperty("nome_categoria", nome_categoria)
            }

            //Chamada e envio de dados para a rota de cadastrar categoria
            val result = apiService.createCategory(body)

            if(result.isSuccessful){
                val msg = result.body()?.get("mensagemStatus")
                Log.e("CREATE-CATEGORY", "STATUS: ${msg}")
            } else{
                Log.e("CREATE-CATEGORY", "STATUS: ${result.message()}")
            }

        }

    }

} //class