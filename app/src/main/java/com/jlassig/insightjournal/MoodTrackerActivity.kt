package com.jlassig.insightjournal


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.time.LocalDate

class MoodTrackerActivity : AppCompatActivity() {
    private val context = this

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_tracker)

        val journal = Journal(context)
       journal.loadEntryList { entries, exception ->

           if (exception != null) {
               Log.d("JULIA", "entriesList wasn't loaded: $exception")
           } else {
               if (entries != null) {
                   if (entries.isNotEmpty()) {
                       getTheInfo(entries)

                   } else {
                       Log.d("JULIA", "there are no entries to display")
                   }
               }
           }
       }

        //so the user can go back to home screen after seeing the displayed entries.
        val homeButton = findViewById<Button>(R.id.homeButton)
        homeButton.setOnClickListener {
            finish()
        }

    }



    //grouping: https://www.geeksforgeeks.org/kotlin-grouping/
    //this function takes the strings from the getMostFrequent function, where we see by month for the last 6 months what the user
    //was feeling and what they were thinking about, and then puts the strings into a nice table.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTheInfo(entryList: List<Entry>) {

        ////   https://www.programiz.com/kotlin-programming/for-loop
        for (i in 6 downTo 1) {
            val monthList = getInfoByMonths(entryList, i)
            getMostFrequent(monthList, i)
        }


    }


    //for lists and how to add to them:
    //https://kotlinlang.org/docs/collections-overview.html#collection-types
    //this function creates a  new list of the entries that are in the last x amount of months
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getInfoByMonths(entryList: List<Entry>, numOfMonths: Int): List<Entry> {
        Log.d("JULIA", "Inside Get Info by Months")
        val newEntryList: MutableList<Entry> = mutableListOf()
        val targetMonth = getTargetMonth(numOfMonths)

        for (entry in entryList) {
            val dateString = entry.entryDate
            val entryDate = LocalDate.parse(dateString)
            val entryMonth = entryDate.monthValue
            if (entryMonth == targetMonth && entry.mood != "") {
                newEntryList.add(entry)
            }
        }
        return newEntryList
    }


    //this function pulls back the last 6 months of data. Month's name, the user's most frequent mood and what they were most
    //often thinking about when they recorded the mood (moodItem). It returns a nice string per month.
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMostFrequent(entryList: List<Entry>, numOfMonths: Int) {

        val moodsTable: TableLayout = findViewById(R.id.moodsTable)

        Log.d("JULIA", "Inside Get most frequent")
        //getting the most frequent mood for each month in the last 6 months
        val groupByMood = entryList.groupBy { it.mood }
        val mostFrequentMood = groupByMood.maxByOrNull { it.value.size }?.key ?: "--No entries--"
        //getting the thing the person was thinking about the most when they had their most frequent mood:
        val groupByMoodItem = groupByMood[mostFrequentMood]?.groupBy { it.moodItem }
        val mostFrequentMoodItem =
            groupByMoodItem?.maxByOrNull { it.value.size }?.key ?: "--No entries--"


        val monthName = when (getTargetMonth((numOfMonths))) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Not a month of year"
        }



        val tableRow = TableRow(this)
        val monthView = TextView(this)
        val moodView = TextView(this)
        val moodItemView = TextView(this)

        monthView.text = monthName
        moodView.text = mostFrequentMood
        moodItemView.text = mostFrequentMoodItem


        tableRow.addView(monthView)
        tableRow.addView(moodView)
        tableRow.addView(moodItemView)

        moodsTable.addView(tableRow


        )





    }


    //this function gets the target month. so if the program is looking at 6 months away, 6 gets entered as an argument for monthInt
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTargetMonth(monthInt: Int): Int {
        Log.d("JULIA", "Inside Get target month")

        //for testing of other dates:
        /*
        val todayString = "2024-01-05"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = LocalDate.parse(todayString, formatter)
         */
        val today = LocalDate.now()
        var targetMonth = today.minusMonths(monthInt.toLong()).monthValue
        if (targetMonth < 0) {
            targetMonth += 12
        }
        return targetMonth

    }




}
