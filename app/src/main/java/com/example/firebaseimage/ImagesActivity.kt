package com.example.firebaseimage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseimage.databinding.ActivityImagesBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class ImagesActivity : AppCompatActivity(), ImagesAdapter.setOnclick {
    private lateinit var binding:ActivityImagesBinding
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var  adapter:ImagesAdapter
    private  var mList= mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initVars()
        getImages()


    }

    private fun initVars() {
        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        firebaseFirestore = FirebaseFirestore.getInstance()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
       adapter=ImagesAdapter(mList,this)
        binding.recyclerView.adapter=adapter

    }
    private fun getImages(){
        firebaseFirestore.collection("images")
            .get().addOnSuccessListener {
                for(i in it)
                {
                    mList.add(i.data["pic"].toString())
                }
                adapter.notifyDataSetChanged()
            }
    }

    override fun onClick(path: String) {

        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(path)
     val filename=storageRef.name

        val externalDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val tempFile = File(externalDir, "$filename.jpeg")
        storageRef.getFile(tempFile).addOnSuccessListener {
            // File downloaded successfully
            Toast.makeText(this, "Downloaded Successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            // Handle download failure
            Toast.makeText(this, "Failed to Download", Toast.LENGTH_SHORT).show()
        }


    }
}