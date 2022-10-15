package com.example.myapplication.datastore

data class UserPreferences(
    val showCompleted: Boolean,
    val sortOrder: SortOrder,
)


enum class SortOrder{
    UNSPECIFIED,
    NONE,
    BY_DEADLINE,
    BY_PRIORITY,
    BY_DEADLINE_AND_PRIORITY
}
