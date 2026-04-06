package com.example.bonepredict.data.remote

import android.net.Uri
import com.example.bonepredict.data.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirebaseService {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Auth
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    // Firestore Sync
    suspend fun syncPatient(patient: Patient) {
        val userId = getCurrentUserId() ?: return
        firestore.collection("users").document(userId)
            .collection("patients").document(patient.id)
            .set(patient).await()
    }

    suspend fun syncClinicalData(data: ClinicalData) {
        val userId = getCurrentUserId() ?: return
        firestore.collection("users").document(userId)
            .collection("clinical_data").document(data.id)
            .set(data).await()
    }

    // Storage
    suspend fun uploadCBCTImage(patientId: String, imageUri: Uri): String {
        val ref = storage.reference.child("cbct_scans/$patientId/${System.currentTimeMillis()}.jpg")
        ref.putFile(imageUri).await()
        return ref.downloadUrl.await().toString()
    }
}
