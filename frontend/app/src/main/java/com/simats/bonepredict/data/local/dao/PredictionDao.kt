package com.example.bonepredict.data.local.dao

import androidx.room.*
import com.example.bonepredict.data.model.Prediction
import kotlinx.coroutines.flow.Flow

@Dao
interface PredictionDao {
    @Query("SELECT * FROM predictions WHERE clinicalDataId = :clinicalDataId")
    fun getPredictionsForData(clinicalDataId: String): Flow<List<Prediction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrediction(prediction: Prediction)
}
