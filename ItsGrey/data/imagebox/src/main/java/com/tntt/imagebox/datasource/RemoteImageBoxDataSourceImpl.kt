package com.tntt.imagebox.datasource

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

import com.tntt.imagebox.model.ImageBoxDto
import com.tntt.model.BoxData
import com.tntt.model.BoxState
import com.tntt.network.Firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class RemoteImageBoxDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RemoteImageBoxDataSource {

    val imageBoxCollection by lazy { firestore.collection("imageBox") }

    override suspend fun createImageBoxDto(imageBoxDto: ImageBoxDto): Flow<ImageBoxDto> = flow {
        imageBoxCollection
            .document(imageBoxDto.id)
            .set(imageBoxDto)
            .addOnSuccessListener { Log.d("function test", "success createImageBoxDto(${imageBoxDto})") }
            .await()
        emit(imageBoxDto)
    }

    override suspend fun getImageBoxDtoList(pageId: String): Flow<List<ImageBoxDto>> = flow {
        var imageBoxDtoList = mutableListOf<ImageBoxDto>()

        imageBoxCollection
            .whereEqualTo("pageId", pageId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val documentSnapshot = querySnapshot.documents.firstOrNull()

                if(documentSnapshot != null) {
                    val data = documentSnapshot.data
                    val id: String = data?.get("id") as String
                    val boxDataHashMap = data?.get("boxData") as HashMap<String, Float>
                    val image = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
                    val gson = Gson()
                    val boxData = gson.fromJson(gson.toJson(boxDataHashMap), BoxData::class.java)
                    imageBoxDtoList.add(ImageBoxDto(id, pageId, boxData, image))
                }
            }.await()
        emit(imageBoxDtoList)
    }

    override suspend fun updateImageBoxDto(imageBoxDto: ImageBoxDto): Flow<Boolean> = flow {
        var result: Boolean = true
        imageBoxCollection
            .document(imageBoxDto.id)
            .set(imageBoxDto)
            .addOnFailureListener {
                result = false
            }
        emit(result)
    }

    override suspend fun deleteImageBoxDto(id: String): Flow<Boolean> = flow {
        var result: Boolean = true
        imageBoxCollection
            .document(id)
            .delete()
            .addOnFailureListener {
                result = false
            }
        emit(result)
    }
}