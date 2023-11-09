package com.jlassig.insightjournal

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.IOException
import java.time.LocalDate
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject


class Journal (private  val context: Context) {

    @RequiresApi(Build.VERSION_CODES.O)
    private val fullDate: LocalDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val today = fullDate.toString()

    ////for adding dates further back for testing purposes:
    //private val today = LocalDate.of(2023, 9,20)
    //private val today = "2023-10-03"

    //so I can get the userId of the logged in person:
    private val sharedPref = context.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
    private val userId = sharedPref.getString("userId", null)
    //for the document ID when I need to delete and update
    private val documentIdList = mutableListOf<String>()


    @RequiresApi(Build.VERSION_CODES.O)
    fun writeEntry(mood: String, moodItem: String, promptInfo: String, entryText: String) {

        Log.d("JULIA", "inside Write entry. user Id: $userId")
        //create a new Entry
        val newEntry = Entry(today, mood, moodItem, promptInfo, entryText, userId)
        try {
            writeToFirestore(newEntry)
        } catch (e: IOException) {
            println("Error: ${e.message}")
        }
    }


    private fun writeToFirestore(newEntry: Entry) {
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            val entriesCollection = db.collection("entries")

                val entry = mapOf(
                    "entryDate" to newEntry.entryDate,
                    "mood" to newEntry.mood,
                    "moodItem" to newEntry.moodItem,
                    "promptInfo" to newEntry.promptInfo,
                    "entryText" to newEntry.entryText,
                    "userId" to userId
                )
                entriesCollection.add(entry)
                    .addOnSuccessListener { documentReference ->
                        val entryId = documentReference.id

                        Log.d("JULIA", "Entry was written to firestore: $entryId")

                    }
                    .addOnFailureListener { e ->
                        Log.d("JULIA", "ERROR: $e")

                    }
        }

    }

    fun loadEntryList(callback: (List<Entry>?, Exception?) -> Unit) {
        Log.d("JULIA", "Inside LoadEntryList")


        val db = FirebaseFirestore.getInstance()
        val entriesCollection = db.collection("entries")
        val entriesList = mutableListOf<Entry>()
        Log.d("JULIA", "inside LoadEntryList. entriesList: $entriesList")
        Log.d("JULIA", "The user id is: $userId")

        entriesCollection.whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    documentIdList.add(document.id)
                    Log.d("JULIA", "Document: $document.data")
                     val entry = document.toObject<Entry>()
                     entriesList.add(entry)
                     Log.d("JULIA", "entries list in for-loop: $entriesList")

                    }

                callback(entriesList, null)
                Log.d("JULIA", "CALLBACK: $callback")
            }
            .addOnFailureListener { e ->
                Log.d("JULIA", "error in loadEntryList. $e")
                callback(null, e)
            }

    }

    fun getDocumentIDS(): MutableList<String>{
        return documentIdList
    }
}






