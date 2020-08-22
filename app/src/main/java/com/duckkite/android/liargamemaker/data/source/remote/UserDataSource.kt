package com.duckkite.android.liargamemaker.data.source.remote

import android.net.Uri
import com.duckkite.android.liargamemaker.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.UploadTask
import java.io.File

interface UserDataSource {
    fun addMyProfile(user: User): Task<Void>
    fun updateMyProfile(user: User): Task<Void>
    fun getUser(userId: String): Task<DocumentSnapshot>
    fun fetchUser(userId: String): DocumentReference
    fun uploadProfileImage(userId: String, profileImage: Uri): UploadTask
}