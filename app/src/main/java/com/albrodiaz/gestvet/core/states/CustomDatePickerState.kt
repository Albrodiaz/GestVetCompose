package com.albrodiaz.gestvet.core.states

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
@ExperimentalMaterial3Api
fun rememberCustomDatePickerState(
    @Suppress("AutoBoxing") initialSelectedDateMillis: Long? = System.currentTimeMillis(),
    @Suppress("AutoBoxing") initialDisplayedMonthMillis: Long? = initialSelectedDateMillis,
    yearRange: IntRange = DatePickerDefaults.YearRange,
    initialDisplayMode: DisplayMode = DisplayMode.Picker
): DatePickerState = rememberSaveable(
    saver = DatePickerState.Saver()
) {
    DatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis,
        initialDisplayedMonthMillis = initialDisplayedMonthMillis,
        yearRange = yearRange,
        initialDisplayMode = initialDisplayMode
    )
}