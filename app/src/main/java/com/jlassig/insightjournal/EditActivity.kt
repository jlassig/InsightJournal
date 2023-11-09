package com.jlassig.insightjournal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class EditActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val entriesCollection = db.collection("entries")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //document ID passed in from the DisplayActivity class.
        val documentId = intent.getStringExtra("documentId")
        if (documentId != null) {
            displayEntry(documentId)
        }
        //the button to go back to DisplayActivity without editing. 
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

    }


    private fun displayEntry(documentId: String){

        entriesCollection.document(documentId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val entry = document.toObject<Entry>()
                    if(entry != null){

                        val dateTextView = findViewById<TextView>(R.id.dateTextView)
                        val promptTextView = findViewById<TextView>(R.id.promptTextView)
                        val entryTextBox = findViewById<EditText>(R.id.entryTextBox)

                        dateTextView.text = entry.entryDate
                        promptTextView.text = entry.promptInfo
                        val entryText = entry.entryText ?: ""
                        entryTextBox.setText(entryText)



                        val updateButton = findViewById<Button>(R.id.editButton)
                        updateButton.setOnClickListener {

                            val updatedText = entryTextBox.text.toString()

                            updateEntry(updatedText, documentId)

                        }



                    }


                }
            }

    }

    private fun updateEntry(updatedText: String, documentId: String){
            entriesCollection.document(documentId)
                .update("entryText", updatedText)
                .addOnSuccessListener {
                    finish()
                }
                .addOnFailureListener {e->
                    Log.d("JULIA", "Update didn't work. error: $e")
                }



    }




}