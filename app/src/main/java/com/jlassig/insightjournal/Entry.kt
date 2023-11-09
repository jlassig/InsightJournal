package com.jlassig.insightjournal

import com.google.firebase.firestore.PropertyName

data class Entry(
    @PropertyName("entryDate") val entryDate: String?,
    @PropertyName("mood") val mood: String?,
    @PropertyName("moodItem") val moodItem: String?,
    @PropertyName("promptInfo") val promptInfo: String?,
    @PropertyName("entryText") var entryText: String?,
    @PropertyName("userId") val userId: String?)
{ constructor() : this(null, null, null, null, null, null) }

