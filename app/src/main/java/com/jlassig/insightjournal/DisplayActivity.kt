package com.jlassig.insightjournal


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.util.Log



class DisplayActivity: AppCompatActivity() {

    private val context = this
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

    @SuppressLint("SetTextI18n")
    private fun displayEntries(){

        //create a journal so we can use it to get the entries List
        val journal = Journal(context)
        val entriesList = journal.loadEntryList()
        entriesList.sortByDescending { entry: Entry->
            entry.entryDate }


        //this is where we are going to put the entries so the user can see them
        val entriesDisplay: LinearLayout = findViewById(R.id.entriesDisplay)

        //clean out the entriesDisplay container before we fill it up
        entriesDisplay.removeAllViews()

        /*
        go through the entries list, create a string for each entry with Date, Prompt and Entry. then put
        that string in a textView. then add that textView to the entriesDisplay in the activity_display.xml
        */
        if (entriesList.size != 0){
            for (entry in entriesList) {
                val textView = TextView(this)
                val entryText =
                    "Date: ${entry.entryDate}\nPrompt: ${entry.promptInfo}\nEntry: ${entry.entryText}\n-----------------------------------------------------------------------------------\n"
                textView.text = entryText
                entriesDisplay.addView(textView)
            }
        }else{
            val textView = TextView(this)
            textView.text = "There are no entries to display"
            entriesDisplay.addView((textView))

        }



    }





}