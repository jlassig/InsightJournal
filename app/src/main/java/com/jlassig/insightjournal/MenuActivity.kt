package com.jlassig.insightjournal


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button



class MenuActivity : AppCompatActivity() {
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        val entryButton = findViewById<Button>(R.id.entryButton)
        val promptButton = findViewById<Button>(R.id.promptButton)
        val displayButton = findViewById<Button>(R.id.displayButton)
        val insightButton  = findViewById<Button>(R.id.insightButton)
        val quitButton = findViewById<Button>(R.id.quitButton)


        entryButton.setOnClickListener{

            val intent = Intent(this, WriteEntryActivity::class.java)
            startActivity(intent)

        }

        promptButton.setOnClickListener{

            val intent = Intent(this, PromptActivity::class.java)
            startActivity(intent)

        }

        displayButton.setOnClickListener{

            val intent = Intent(this, DisplayActivity::class.java)
            startActivity(intent)

        }

        insightButton.setOnClickListener {

            val intent = Intent(this, MoodTrackerActivity::class.java)
            startActivity(intent)
        }


        quitButton.setOnClickListener {
            finish()
        }



    }




}