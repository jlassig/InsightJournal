package com.jlassig.insightjournal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlin.random.Random

class PromptActivity : AppCompatActivity() {
    private val context = this

    private var mood = ""
    private var moodItem = ""

    private var promptString = ""


    private val promptList = listOf(
        "What were you most grateful for today and why?",
        "Write about an experience you had with your grandma or grandpa.",
        "Describe in detail one of your favorite kinds of foods.",
        "Where do you see yourself in 5 years?",
        "Is there anything you did today that you will do different next time?",
        "Write about a family trip you've taken.",
        "Name a person that you are following on social media and tell why you follow them.",
        "What is the name and cost of your favorite snack food today?",
        "What goals are you currently working on?",
        "Describe a weird dream you've had and then try to give the interpretation.",
        "What was the best part of my day today?",
        "Write about an experience you had with one of your parents.",
        "Write about a favorite trip that you took. Would you ever go back?",
        "What is the top headline today?",
        "Is this where you imagined yourself 5 years ago? Is it better or worse and why?",
        "If you could invent anything, what would it be?",
        "Write about an experience you had with one of your siblings.",
        "Name the song and artist that is at the top of your playlist right now.",
        "What new movie is getting released this week?",
        "If this was your last decade on Earth, what would you do differently? Make a goal about that.",
        "What do you look for in the perfect friend. Why do you choose those attributes?",
        "Which of your family members do you most admire and why?",
        "What is your greatest dream?"
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prompt)

        //displaying the prompt to the screen
        displayPrompt()

        //for the submit button
        val submitPromptButton = findViewById<Button>(R.id.submitPromptEntry)
        submitPromptButton.setOnClickListener {
            writeEntry()
            finish()
        }

    }


    //this gets a random prompt from the promptList
//https://www.baeldung.com/kotlin/list-get-random-item
    private fun getAPrompt(): String {
        val index = Random.nextInt(promptList.size)
        return (promptList[index])

    }

    private fun displayPrompt() {
        val randomPrompt = findViewById<TextView>(R.id.randomPrompt)

        promptString = getAPrompt()
        randomPrompt.text = promptString
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun writeEntry() {
        //get the text
        val promptEntryText = findViewById<EditText>(R.id.promptEntry)
        //turn it into a string
        val textString = promptEntryText.text.toString()
        val journal = Journal(context)
        journal.writeEntry(mood, moodItem, promptString, textString)
    }


}
