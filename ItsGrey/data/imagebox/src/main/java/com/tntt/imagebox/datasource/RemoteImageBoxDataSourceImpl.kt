package com.tntt.imagebox.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.tntt.imagebox.model.ImageBoxDto
import com.tntt.model.BoxData
import com.tntt.model.BoxState
import com.tntt.network.Firestore
import java.util.UUID
import javax.inject.Inject

class RemoteImageBoxDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RemoteImageBoxDataSource {

    val imageBoxCollection by lazy { firestore.collection("imageBox") }

    override fun createImageBoxDto(imageBoxDto: ImageBoxDto): String {
        val imageBoxId = UUID.randomUUID().toString()
        imageBoxDto.id = imageBoxId
        imageBoxCollection
            .document(imageBoxId)
            .set(imageBoxDto)
        return imageBoxId
    }

    override fun getImageBoxDto(pageId: String): ImageBoxDto {
        var imageBoxDto: ImageBoxDto = ImageBoxDto("1", "1", BoxData(0.0f, 0.0f, 0.0f, 0.0f))

        imageBoxCollection
            .whereEqualTo("pageId", pageId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val documentSnapshot = querySnapshot.documents.firstOrNull() ?: throw NullPointerException(":data:imagebox - datasource/RemoteImageBoxDataSourceImpl.getImageBoxDto().documentSnapshot")

                val data = documentSnapshot.data
                val id: String = data?.get("id") as String
                val boxData: BoxData = data?.get("boxData") as BoxData

                imageBoxDto = ImageBoxDto(id, pageId, boxData)
            }
        return imageBoxDto
    }

    override fun updateImageBoxDto(imageBoxDto: ImageBoxDto): Boolean {
        var result: Boolean = true
        imageBoxCollection
            .document(imageBoxDto.id)
            .set(imageBoxDto)
            .addOnFailureListener {
                result = false
            }
        return result
    }

    override fun deleteImageBoxDto(id: String): Boolean {
        var result: Boolean = true
        imageBoxCollection
            .document(id)
            .delete()
            .addOnFailureListener {
                result = false
            }
        return result
    }
}