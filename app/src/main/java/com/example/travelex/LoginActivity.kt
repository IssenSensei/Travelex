package com.example.travelex

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelex.database.TravelexDatabase
import com.example.travelex.database.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val rcSignIn = 9001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        sign_in_button.setOnClickListener {
            googleSignIn()
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateMain()
        }
    }

    private fun googleSignIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, rcSignIn)
    }

    fun login() {
        auth.signInWithEmailAndPassword(
            login_email.text.toString(),
            login_password.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    navigateMain()
                } else {
                    Toast.makeText(
                        baseContext, getString(R.string.authentication_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == rcSignIn) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                Toast.makeText(baseContext, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    if (task.result!!.additionalUserInfo!!.isNewUser) {
                        MainScope().launch {
                            TravelexDatabase.getDatabase(applicationContext, this).userDao.insert(
                                User(
                                    auth.currentUser!!.uid,
                                    if (auth.currentUser!!.displayName != null) auth.currentUser!!.displayName!! else "",
                                    if (auth.currentUser!!.email != null) auth.currentUser!!.email!! else "",
                                    if (auth.currentUser!!.photoUrl != null) auth.currentUser!!.photoUrl!!.toString() else ""
                                )
                            )
                        }.invokeOnCompletion {
                            navigateMain()
                        }
                    }
                } else {
                    Toast.makeText(baseContext, getString(R.string.error), Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }


    private fun navigateMain() {
        val startMain = Intent(this, MainActivity::class.java)
        startActivityForResult(startMain, 0)
    }

    fun navigateRegister() {
        val startRegister = Intent(this, RegisterActivity::class.java)
        startActivityForResult(startRegister, 1)
    }

    fun navigateRemindPassword() {
        val startRemind = Intent(this, RemindPasswordActivity::class.java)
        startActivityForResult(startRemind, 2)
    }
}