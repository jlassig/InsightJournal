package com.jlassig.insightjournal

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException
import java.time.LocalDate
import android.util.Log
import kotlinx.serialization.Serializable


class Journal (private val context: Context){


    ////for storing the entry:

    private val internalDir: File = context.filesDir
    private val fileName = "entries.json"
    private val entryFile = File(internalDir, fileName)

    @RequiresApi(Build.VERSION_CODES.O)
    @Serializable(with = LocalDateSerializer::class)
    private val today: LocalDate = LocalDate.now()
    ////for adding dates further back for testing purposes:
    //private val today = LocalDate.of(2023, 9,20)


    @RequiresApi(Build.VERSION_CODES.O)
    fun writeEntry(mood: String, moodItem: String, promptInfo: String, entryText: String){

        //create a new Entry
        val newEntry = Entry(today, mood, moodItem, promptInfo, entryText)
        try{
            saveEntry(newEntry)
        }catch (e: IOException){
            println("Error: ${e.message}")
        }
    }


    private fun saveEntry(newEntry : Entry?) {

        val existingEntries: MutableList<Entry> = loadEntryList().toMutableList()
        if (newEntry != null) {
            existingEntries.add(newEntry)
        }

        val updatedEntriesList = Json.encodeToString(existingEntries)

        writeJsonFile(updatedEntriesList)
    }

    fun loadEntryList(): MutableList<Entry>{
        return if(entryFile.exists()){
            try {
                val jsonEntries = entryFile.readText()
                 Json.decodeFromString(jsonEntries)

            }catch(e:Exception){
                mutableListOf()
            }
        }
        else{
            mutableListOf()
        }
    }

    private fun writeJsonFile( entries: String){

        if (!entryFile.exists()) {
            entryFile.createNewFile()
        }
        //https://zetcode.com/kotlin/writefile/
        try {
            entryFile.writeText(entries)
        }catch(e: IOException)
        {
            println("Error writing Json info: ${e.message}")
        }

    }


    private fun clearJsonFile() {
        if (entryFile.exists()) {
            entryFile.delete()
        }
    }









}