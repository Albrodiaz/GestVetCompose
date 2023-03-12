package com.albrodiaz.gestvet.core.extensions

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*

fun String.dateToMillis(): Long {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    val date = formatter.parse(this)
    return date!!.time
}

fun String.hourToMillis(): Long {
    val formatter = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    val date = formatter.parse(this)
    return date!!.time
}

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()