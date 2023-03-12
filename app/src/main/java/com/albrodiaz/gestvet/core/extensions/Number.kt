package com.albrodiaz.gestvet.core.extensions

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Long.toDate(): String {
    val date = Date(this)
    return SimpleDateFormat("dd/MM/yyyy").format(date)
}
fun Int.hourFormatter(): String = if (this.toString().length == 1) "0$this" else this.toString()
