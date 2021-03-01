package com.example.travelex

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_password_remind.*


class RemindPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_remind)
    }


    fun remindPassword() {
        val auth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(password_remind_email.text.toString())
            .addOnSuccessListener {
                Toast.makeText(this, getString(R.string.recovery_email_sent_success), Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, getString(R.string.recovery_email_sent_failure), Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}