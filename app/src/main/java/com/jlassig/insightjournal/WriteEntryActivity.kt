package com.jlassig.insightjournal

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi


class WriteEntryActivity : AppCompatActivity() {

    private val context = this

    var selectedMood = ""
    var selectedMoodItem = ""

    var moodIsChosen = false
    var moodItemIsChosen = false

    private var promptString = ""



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_entry_activity)


        val moodSpinner = findViewById<Spinner>(R.id.moodSpinner)
        val moods = resources.getStringArray(R.array.mood_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, moods)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        moodSpinner.adapter = adapter


        val moodItemSpinner = findViewById<Spinner>(R.id.moodItemSpinner)
        val moodItems = resources.getStringArray(R.array.mood_items_array)
        val itemsAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, moodItems)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        moodItemSpinner.adapter = itemsAdapter


        moodSpinner.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                moodIsChosen = true
                selectedMood = moods[position]
                displayPrompt()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        moodItemSpinner.onItemSelectedListener= object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                moodItemIsChosen = true
                selectedMoodItem = moodItems[position]
                displayPrompt()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        val submitButton = findViewById<Button>(R.id.submitEntry)

        submitButton.setOnClickListener{
            writeEntry()

            finish()
        }

    }

    fun displayPrompt() {
        val moodPrompt = findViewById<TextView>(R.id.moodPrompt)

        if (moodIsChosen && moodItemIsChosen) {

            promptString = "Why are you feeling $selectedMood when you think about your $selectedMoodItem?"
            moodPrompt.text = promptString
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun writeEntry(){

        //get the text
        val entryText = findViewById<EditText>(R.id.journalEntry)
        //turn it into a string
        val textString = entryText.text.toString()

        val journal = Journal(context)
        journal.writeEntry(selectedMood, selectedMoodItem, promptString, textString)
    }



}