package com.example.firebaseimage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.firebaseimage.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVars()
        registerClickEvents()
    }

    private fun registerClickEvents() {
        binding.uploadBtn.setOnClickListener {
            uploadImage()

        }
        binding.showAllBtn.setOnClickListener {
            startActivity(Intent(this, ImagesActivity::class.java))
        }

        binding.imageView.setOnClickListener {
          resultLauncher.launch("image/*")
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.GetContent() ) {
        imageUri=it
        binding.imageView.setImageURI(imageUri)
    }

    private fun initVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }
    private  fun uploadImage(){

        binding.progressBar.visibility=View.VISIBLE
        storageRef=storageRef.child(System.currentTimeMillis().toString())
        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener{task->
                binding.progressBar.visibility=View.GONE
                binding.imageView.setImageResource(R.drawable.vector)
                if(task.isSuccessful){
                    storageRef.downloadUrl.addOnSuccessListener {uri->
                    val map=HashMap<String,Any>()
                    map["pic"]=uri.toString()
                    firebaseFirestore.collection("images").add(map).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(this, "uploaded successfully", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility=View.GONE
                            binding.imageView.setImageResource(R.drawable.vector)
                        }
                        else{

                            Toast.makeText(this, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()

                        }
                        binding.progressBar.visibility=View.GONE
                        binding.imageView.setImageResource(R.drawable.vector)
                    }.addOnFailureListener{
                        Toast.makeText(this, "upload fialed", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility=View.GONE
                        binding.imageView.setImageResource(R.drawable.vector)
                    }

                    }.addOnFailureListener{
                        Toast.makeText(this, "upload fialed", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility=View.GONE
                        binding.imageView.setImageResource(R.drawable.vector)
                    }

                }else{
                    binding.progressBar.visibility=View.GONE
                    binding.imageView.setImageResource(R.drawable.vector)
                    Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}