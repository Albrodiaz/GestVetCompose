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

fun String.isValidDate(): Boolean {
    val regex = Regex("^([0-2][0-9]|3[0-1])(\\/|-)(0[1-9]|1[0-2])\\2(\\d{4})\$")
    return matches(regex)
}

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPass(): Boolean {
    val regex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$")
    return matches(regex)
}