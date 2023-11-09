package com.jlassig.insightjournal

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

//this first activity is for creating a user or logging in a user. Once the user is logged in, then
//and only then can they access the journal application.
class MainActivity : AppCompatActivity() {
   private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            fun loginUser(email: String, password: String) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            //getting the user info from firebase
                            val user = FirebaseAuth.getInstance().currentUser
                            val userId = user?.uid

                            //storing the user's info for use across the app
                            val sharedPref = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
                            val editor = sharedPref.edit()
                            editor.putString("userId", userId)
                            editor.apply()

                            //now go to the main menu:
                            val intent = Intent(this, MenuActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Login failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(
                    baseContext,
                    "Email and password are required.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        val goToCreateUserButton = findViewById<Button>(R.id.goToCreateUserButton)

        goToCreateUserButton.setOnClickListener {
            val intent= Intent(this, NewUserActivity::class.java)
            startActivity(intent)
        }

        val quitProgramButton = findViewById<Button>(R.id.quitProgramButton)
        quitProgramButton.setOnClickListener {
            finish()
        }

    }



}