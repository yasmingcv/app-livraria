package br.senai.sp.jandira.apilivraria

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CadastroLivroImagem : AppCompatActivity() {
    //atributos para manipulacao de imagens (objetos de uri)
    private var imageUriGRD: Uri? = null
    private var imageUriPEQ: Uri? = null

    //atributo para acesso e manipulacao do storage
    private lateinit var storageRef: StorageReference

    //atributo para acesso e manipulacao do firestore database
    private lateinit var firebaseFirestore: FirebaseFirestore

    //atributos de imageview
    private var btnImageGRD: ImageView? = null
    private var btnImagePEQ: ImageView? = null

    //atributos de button
    private var btnUpload: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cadastro_livro_imagem)

        //inicializa as variaveis do firestore
        initeVars()

        //teste de receber body

        val body = intent.getStringExtra("bodyJSON")

        Log.e("BODY JSON CHEGANDO", "${body}", )

        //recupera os objetos de view das imagens
        btnImageGRD = findViewById<ImageView>(R.id.imgGRD)
        btnImagePEQ = findViewById<ImageView>(R.id.imgPEQ)

        //recupera o objeto de button para realizar upload
        btnUpload = findViewById<Button>(R.id.btnCadastrarLivro)

        //recupera a imagem grande da galeria
        btnImageGRD?.setOnClickListener{
            resultLauncherGRD.launch("image/*")
        }

        //recupera a imagem pequena da galeria
        btnImagePEQ?.setOnClickListener{
            resultLauncherPEQ.launch("image/*")
        }

        btnUpload?.setOnClickListener {
            upload()
        }
    }

    //inicializa os atributos referentes ao firebase

    private fun initeVars(){
        storageRef = FirebaseStorage.getInstance().reference.child("images")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    //lancador para pegar as imagens grandes da galeria
    private val  resultLauncherGRD = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUriGRD = it
        btnImageGRD?.setImageURI(it)
        Log.e("IMG GRD", it.toString(), )
    }

    private val  resultLauncherPEQ = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUriPEQ = it
        btnImagePEQ?.setImageURI(it)
        Log.e("IMG PEQ", it.toString(), )
    }

    //upload das imagens
    private fun upload(){
        imageUriGRD?.let {
            val riversRef = storageRef.child("${it.lastPathSegment}-${System.currentTimeMillis()}.jpg")
            val uploadTask = riversRef.putFile(it)
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    riversRef.downloadUrl.addOnSuccessListener { uri ->
                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()
                        firebaseFirestore.collection("images").add(map).addOnCompleteListener { firestoreTask ->
                            if (firestoreTask.isSuccessful){
                                Toast.makeText(this, "UPLOAD IMAGEM GRANDE OK!", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(this, firestoreTask.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                            btnImageGRD?.setImageResource(R.drawable.upload)
                        }
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    btnImageGRD?.setImageResource(R.drawable.upload)
                }
            }
        }

        imageUriPEQ?.let {
            val riversRef = storageRef.child("${it.lastPathSegment}-${System.currentTimeMillis()}.jpg")
            val uploadTask = riversRef.putFile(it)
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    riversRef.downloadUrl.addOnSuccessListener { uri ->
                        val map = HashMap<String, Any>()
                        map["pic"] = uri.toString()
                        firebaseFirestore.collection("images").add(map).addOnCompleteListener { firestoreTask ->
                            if (firestoreTask.isSuccessful){
                                Toast.makeText(this, "UPLOAD IMAGEM PEQUENA OK!", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(this, firestoreTask.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                            btnImagePEQ?.setImageResource(R.drawable.upload)
                        }
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    btnImagePEQ?.setImageResource(R.drawable.upload)
                }
            }
        }
    }
}