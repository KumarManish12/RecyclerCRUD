package com.o7services.recyclercrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.o7services.recyclercrud.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnregister.setOnClickListener {
            if (binding.etname.text.toString().isEmpty()) {
                binding.etname.error = "enter email"
            } else if (binding.etphone.text.toString().isEmpty()) {
                binding.etphone.error = "enter email"
            } else {
                auth.createUserWithEmailAndPassword(binding.etname.text.toString(), binding.etphone.text.toString()).addOnSuccessListener {
                    val userModel = UserModel()
                    userModel.name = binding.etname.text.toString()
                    userModel.mobile = binding.etphone.text.toString()
                    db.collection("Users").document(auth.currentUser?.uid?:"")
                        .set(userModel)
                        .addOnSuccessListener { documentReference ->
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Cannot register ${it.toString()}", Toast.LENGTH_LONG).show()

                        }
                }
                    .addOnFailureListener {  }
            }
        }
    }
}