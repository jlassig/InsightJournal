package com.jlassig.insightjournal

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DisplayActivity: AppCompatActivity() {


    private val context = this
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        displayEntries()

        //so the user can go back to home screen after seeing the displayed entries.
        val homeButton = findViewById<Button>(R.id.homeButton)
        homeButton.setOnClickListener{
            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayEntries() {
       val journal = Journal(context)
       journal.loadEntryList { entries, exception ->

           val documentIds = journal.getDocumentIDS()

           if (exception != null) {
               Log.d("JULIA", "entriesList wasn't loaded: $exception")
           } else {
               val entriesDisplay: LinearLayout = findViewById(R.id.entriesDisplay)
               entriesDisplay.removeAllViews()

               if (entries != null) {

                   val sortedEntries = entries.sortedByDescending {
                       entry->
                       LocalDate.parse(entry.entryDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                   }


                       for (entry in sortedEntries) {
                           val textView = TextView(this)
                           val entryText =
                               "Date: ${entry.entryDate}\nPrompt: ${entry.promptInfo}\nEntry: ${entry.entryText}\n"
                           textView.text = entryText
                           val editButton = Button(this)
                           editButton.text = "Edit"
                           val deleteButton = Button(this)
                           deleteButton.text = "Delete"
                           val lineText = TextView(this)
                           lineText.text = "-----------------------------------------------------------------------------------\n"

                           entriesDisplay.addView(textView)
                           entriesDisplay.addView(editButton)
                           entriesDisplay.addView(deleteButton)
                           entriesDisplay.addView(lineText)


                           editButton.setOnClickListener {
                               val index = getIndex(entry, entries)
                               val documentID = documentIds[index]
                               val intent= Intent(this@DisplayActivity, EditActivity::class.java)
                               intent.putExtra("documentId", documentID)
                               startActivity(intent)


                           }
                           deleteButton.setOnClickListener{
                               val index = getIndex(entry, entries)
                               val documentID = documentIds[index]

                               val db = FirebaseFirestore.getInstance()
                               val entriesCollection = db.collection("entries")

                               entriesCollection.document(documentID)
                                   .delete()
                                   .addOnSuccessListener {
                                       Log.d("JULIA", "Document deleted!")
                                       onResume()

                                   }
                                   .addOnFailureListener{
                                       e->
                                       Log.d("JULIA", "Error deleting document $e")
                                   }

                           }

                       }
                   } else {
                       val textView = TextView(this)
                       textView.text = "There are no entries to display"
                       entriesDisplay.addView((textView))


                   }
               }
           }




   }

    private fun getIndex(entry: Entry, entriesList: List<Entry>?): Int {
        if (entriesList != null) {
            return entriesList.indexOf(entry)
        }
        return -1

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume(){
        super.onResume()
        displayEntries()
    }







}