package com.o7services.recyclercrud

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.o7services.recyclercrud.databinding.ActivityLoginBinding
import java.util.*


class LoginActivity : AppCompatActivity() {
    lateinit var binding:ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    lateinit var gso : GoogleSignInOptions
    lateinit var googleSignInClient : GoogleSignInClient
   val callbackManager = CallbackManager.Factory.create()

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
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FacebookSdk.sdkInitialize(this)
        binding.loginButton.setReadPermissions(Arrays.asList("email"))


       binding.loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val loggedOut = AccessToken.getCurrentAccessToken() == null
                val credential = FacebookAuthProvider.getCredential(AccessToken.getCurrentAccessToken()?.token ?:"")
                auth.signInWithCredential(credential).addOnSuccessListener {
                }.addOnFailureListener {

                }
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })
        auth = Firebase.auth
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