package com.example.bonepredict.data.model

import androidx.room.*
import java.util.UUID

@Entity(
    tableName = "clinical_data",
    foreignKeys = [
        ForeignKey(
            entity = Patient::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["patientId"])]
)
data class ClinicalData(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val patientId: String,
    
    // Demographics/Lifestyle
    val weight: Float,
    val smokingStatus: String,
    val alcoholConsumption: String,
    
    // Medical History
    val hasDiabetes: Boolean,
    val hasHypertension: Boolean,
    val hasOsteoporosis: Boolean,
    
    // Periodontal Status
    val probingDepth: Float,
    val cal: Float,
    val bleedingOnProbing: Boolean,
    val bleedingIndex: Float = 0f,
    val plaqueIndex: Float = 0f,
    val toothMobility: String = "",
    val gingivalPhenotype: String = "",
    
    // Imaging
    val cbctImageUrl: String? = null,
    
    val createdAt: Long = System.currentTimeMillis()
)
