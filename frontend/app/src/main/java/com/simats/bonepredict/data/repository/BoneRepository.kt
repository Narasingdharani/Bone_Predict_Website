package com.example.bonepredict.data.repository

import com.example.bonepredict.data.local.dao.*
import com.example.bonepredict.data.model.*
import com.example.bonepredict.data.remote.FirebaseService
import kotlinx.coroutines.flow.Flow

class BoneRepository(
    private val patientDao: PatientDao,
    private val clinicalDataDao: ClinicalDataDao,
    private val predictionDao: PredictionDao,
    private val firebaseService: FirebaseService
) {
    // Patients
    val allPatients: Flow<List<Patient>> = patientDao.getAllPatients()

    suspend fun addPatient(patient: Patient) {
        patientDao.insertPatient(patient)
        try {
            firebaseService.syncPatient(patient)
        } catch (e: Exception) {
            // Handle sync error (e.g., log it, retry later)
        }
    }

    // Clinical Data
    suspend fun addClinicalData(data: ClinicalData) {
        clinicalDataDao.insertClinicalData(data)
        try {
            firebaseService.syncClinicalData(data)
        } catch (e: Exception) {
            // Handle sync error
        }
    }

    // Predictions
    suspend fun addPrediction(prediction: Prediction) {
        predictionDao.insertPrediction(prediction)
    }

    fun getPredictionsForData(clinicalDataId: String): Flow<List<Prediction>> {
        return predictionDao.getPredictionsForData(clinicalDataId)
    }
}
