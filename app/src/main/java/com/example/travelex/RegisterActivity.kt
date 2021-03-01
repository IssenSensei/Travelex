package com.example.travelex

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelex.database.TravelexDatabase
import com.example.travelex.database.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
    }

    fun register() {
        auth.createUserWithEmailAndPassword(
            register_email.text.toString(),
            register_password.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    MainScope().launch {
                        TravelexDatabase.getDatabase(applicationContext, this).userDao.insert(
                            User(
                                auth.uid!!,
                                register_name.text.toString(),
                                register_email.text.toString(),
                                ""
                            )
                        )
                    }.invokeOnCompletion {
                        navigateMain()
                    }
                } else {
                    Toast.makeText(
                        baseContext, getString(R.string.invalid_data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun navigateMain() {
        val startMain = Intent(this, MainActivity::class.java)
        startActivityForResult(startMain, 0)
    }

    fun navigateLogin() {
        setResult(RESULT_CANCELED)
        finish()
    }
}