package com.o7services.recyclercrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.o7services.recyclercrud.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    lateinit var gso : GoogleSignInOptions
    lateinit var googleSignInClient : GoogleSignInClient

    var googleSigninResult=registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {

            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken!!)
        }catch (e:ApiException){

        }


    }
    fun firebaseAuthWithGoogle(token: String){
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential).addOnSuccessListener {
            Toast.makeText(this, "Successfully registered ", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(this, "${it.message} ", Toast.LENGTH_LONG).show()

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("5785146692-svsbkllq1fjrhju618a9ofaats7imljr.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient=GoogleSignIn.getClient(this,gso)
        binding.tvgooglesignin.setOnClickListener {
            val signInIntent:Intent=googleSignInClient.signInIntent
            googleSigninResult.launch(signInIntent)
        }

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