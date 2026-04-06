package com.example.bonepredict.data.local.dao

import androidx.room.*
import com.example.bonepredict.data.model.ClinicalData
import kotlinx.coroutines.flow.Flow

@Dao
interface ClinicalDataDao {
    @Query("SELECT * FROM clinical_data WHERE patientId = :patientId")
    fun getClinicalDataForPatient(patientId: String): Flow<List<ClinicalData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClinicalData(data: ClinicalData)

    @Query("SELECT * FROM clinical_data WHERE id = :id")
    suspend fun getClinicalDataById(id: String): ClinicalData?
}
