package com.duckkite.android.liargamemaker.data.source.remote

import android.net.Uri
import com.duckkite.android.liargamemaker.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.FileInputStream

class UserRepository(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
): UserDataSource {
    override fun addMyProfile(user: User): Task<Void> {
        return db.collection(USER_TABLE).document(user.uuid).set(user)
    }

    override fun updateMyProfile(user: User): Task<Void> {
        return db.collection(USER_TABLE).document(user.uuid).set(user)
    }

    override fun getUser(userId: String): Task<DocumentSnapshot> {
        return db.collection(USER_TABLE).document(userId).get()
    }

    override fun fetchUser(userId: String): DocumentReference {
        return db.collection(USER_TABLE).document(userId)
    }

    override fun uploadProfileImage(userId: String, profileImage: Uri): UploadTask {
        return storage.reference.child("profileImages/${userId}${profileImage.lastPathSegment}").putFile(profileImage)
    }

    companion object {
        private const val USER_TABLE = "Users"
    }
}