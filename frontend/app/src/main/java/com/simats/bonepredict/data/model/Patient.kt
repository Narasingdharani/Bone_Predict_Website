package com.example.bonepredict.data.model

import androidx.room.*
import java.util.UUID

@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey
    val id: String = "P-${System.currentTimeMillis() % 1000000}",
    val firstName: String,
    val lastName: String,
    val dob: String,
    val gender: String,
    val contactNumber: String,
    val doctorId: String, // Link to User
    val riskStatus: String = "",   // Populated from predictions join
    val lastVisit: String = "",    // Last assessment date
    val createdAt: Long = System.currentTimeMillis()
)
