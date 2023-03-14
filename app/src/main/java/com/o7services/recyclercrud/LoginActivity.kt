package com.o7services.recyclercrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.o7services.recyclercrud.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(auth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.btnlogin.setOnClickListener {
            if (binding.etemail.text.toString().isEmpty()){
                binding.etemail.error="enter email"
            }
            else if (binding.etpassword.text.toString().isEmpty()){
                binding.etpassword.error="enter email"
            }
            else{
                auth.signInWithEmailAndPassword(binding.etemail.text.toString(),
                    binding.etpassword.text.toString()).addOnSuccessListener {
                    Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Cannot Login ${it.toString()}", Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.tvcreateaccount.setOnClickListener {
            val intent= Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}