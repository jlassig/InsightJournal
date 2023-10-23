package com.jlassig.insightjournal


import kotlinx.serialization.Serializable
import java.time.LocalDate
//this class holds the data everytime an Entry gets created or loaded from the Json to display
@Serializable
data class Entry (
    @Serializable(with = LocalDateSerializer::class)
    val entryDate: LocalDate,
    val mood: String,
    val moodItem: String,
    val promptInfo: String,
    val entryText: String?
)
