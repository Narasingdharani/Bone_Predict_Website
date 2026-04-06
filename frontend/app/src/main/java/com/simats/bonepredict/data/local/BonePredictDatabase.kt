package com.example.bonepredict.data.local

import android.content.Context
import androidx.room.*
import com.example.bonepredict.data.local.dao.*
import com.example.bonepredict.data.model.*

@Database(
    entities = [
        User::class,
        Patient::class,
        ClinicalData::class,
        Prediction::class
    ],
    version = 3,
    exportSchema = false
)
abstract class BonePredictDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun patientDao(): PatientDao
    abstract fun clinicalDataDao(): ClinicalDataDao
    abstract fun predictionDao(): PredictionDao

    companion object {
        @Volatile
        private var INSTANCE: BonePredictDatabase? = null

        fun getDatabase(context: Context): BonePredictDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BonePredictDatabase::class.java,
                    "bone_predict_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
