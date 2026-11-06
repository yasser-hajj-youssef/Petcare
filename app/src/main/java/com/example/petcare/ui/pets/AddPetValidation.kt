package com.example.petcare.ui.pets

import java.time.Year
fun validatePetName(name: String): String? = when {
    name.isBlank() -> "Name is required"
    name.trim().length > 30 -> "Keep the name under 30 characters"
    else -> null
}
fun validateBirthYear(text: String, currentYear: Int = Year.now().value): String? {
    if (text.isBlank()) return null // optional field
    val year = text.toIntOrNull() ?: return "Enter a year like 2021"
    return if (year in 1990..currentYear) null
    else "Year must be between 1990 and $currentYear"
}