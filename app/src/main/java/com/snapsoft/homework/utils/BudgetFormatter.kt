package com.snapsoft.homework.utils

import java.text.NumberFormat
import java.util.*


fun formatBudget(budget: Int): String {
    return if (budget == 0) {
        "The budget is not known"
    } else {
        "Budget: ${NumberFormat.getNumberInstance(Locale.US).format(budget)} USD"
    }
}
