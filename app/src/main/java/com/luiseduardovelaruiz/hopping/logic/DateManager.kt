package com.luiseduardovelaruiz.hopping.logic

import java.text.SimpleDateFormat
import java.util.*

class DateManager {
    fun getTodaysDateWithDashFormat(): String{
        return SimpleDateFormat("dd-MM-yyyy").format(Date())
    }

    fun getTodaysDateWithPeriodFormat(): String{
        return SimpleDateFormat("dd.MM.yyyy").format(Date())
    }
}