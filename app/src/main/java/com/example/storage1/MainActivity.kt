package com.example.storage1

import android.app.Instrumentation.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.storage1.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    lateinit var firebaseDatabase:FirebaseStorage
    lateinit var reference: StorageReference
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseDatabase = FirebaseStorage.getInstance()
        reference = firebaseDatabase.getReference("my_images")
        binding.imageLoad.setOnClickListener {
            getImageContent.launch("image/*")
        }

    }
    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
            uri ?: return@registerForActivityResult
            val myTask = reference.child("rasm1").putFile(uri)

            binding.myProgress.visibility = View.VISIBLE

            myTask.addOnSuccessListener {
                if(it.task.isSuccessful){
                   val dowloadUri =  it.metadata?.reference?.downloadUrl
                    dowloadUri?.addOnSuccessListener {
                        Picasso.get().load(it).into(binding.imageLoad)
                        binding.myProgress.visibility = View.GONE
                    }
                }
            }
            myTask.addOnFailureListener{
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
}