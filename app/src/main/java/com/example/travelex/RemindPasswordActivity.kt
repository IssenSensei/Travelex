package com.example.travelex

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_password_remind.*


class RemindPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_remind)
    }


    fun remindPassword(view: View) {
        val auth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(password_remind_email.text.toString())
            .addOnSuccessListener {
                Toast.makeText(this, "Wiadomość wyłana pomyślnie, sprawdź skrzynkę pocztową!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Wystąpił błąd, spróbuj ponownie", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}