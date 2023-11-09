package com.jlassig.insightjournal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class NewUserActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)


        auth = FirebaseAuth.getInstance()

        val newUserButton = findViewById<Button>(R.id.newUserButton)
        newUserButton.setOnClickListener {
            createNewUser()
        }

        val homeButton = findViewById<Button>(R.id.homeButton2)
        homeButton.setOnClickListener {
            finish()
        }

        onStart()
    }
    private fun createUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val loggedInText = findViewById<TextView>(R.id.textView8)
                    loggedInText.text = "You have created a new account."
                } else {
                    Toast.makeText(
                        baseContext,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun createNewUser() {
         val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)

        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            createUserWithEmailAndPassword(email, password)
            finish()
        } else {
            Toast.makeText(
                baseContext,
                "Email and password are required.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}



