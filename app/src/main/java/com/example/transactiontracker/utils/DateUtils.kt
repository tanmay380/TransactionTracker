package com.example.transactiontracker.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {

    fun startOfCurrentMonth(): Long {
        val cal = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        return cal.timeInMillis
    }

    fun endOfCurrentMonth(): Long {
        val cal = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }
        return cal.timeInMillis
    }

    fun formatDate(epoch: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = epoch
        return sdf.format(calendar.time)
    }

    fun formatDateWithTime(epoch: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = epoch
        return sdf.format(calendar.time)
    }




}
