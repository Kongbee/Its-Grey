package com.tntt.textbox.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.tntt.model.BoxData
import com.tntt.model.BoxState
import com.tntt.network.Firestore
import com.tntt.textbox.model.TextBoxDto
import java.util.UUID
import javax.inject.Inject

class RemoteTextBoxDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): RemoteTextBoxDataSource {

    val textBoxCollection by lazy { firestore.collection("textBox") }

    override fun createTextBoxDto(textBoxDto: TextBoxDto): String {
        val id = UUID.randomUUID().toString()
        textBoxDto.id = id
        textBoxCollection
            .document(id)
            .set(textBoxDto)
        return id
    }

    override fun getTextBoxDtoList(pageId: String): List<TextBoxDto> {
        val textBoxDtoList = mutableListOf<TextBoxDto>()

        textBoxCollection
            .whereEqualTo("pageId", pageId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val documentSnapshot = querySnapshot.documents
                for (document in documentSnapshot) {
                    val data = document.data

                    val id = data?.get("id") as String
                    val text = data?.get("text") as String
                    val fontSizeRatio = data?.get("fontSizeRatio") as Float
                    val boxData = data?.get("boxData") as BoxData
                    textBoxDtoList.add(TextBoxDto(id, pageId, text, fontSizeRatio, boxData))
                }
            }
        return textBoxDtoList
    }

    override fun updateTextBoxDtoList(textBoxDtoList: List<TextBoxDto>): Boolean {
        var result: Boolean = true

        for (textBoxDto in textBoxDtoList) {
            textBoxCollection
                .document(textBoxDto.id)
                .set(textBoxDto)
                .addOnFailureListener {
                    result = false
                }
        }
        return result
    }

    override fun deleteTextBoxDto(id: String): Boolean {
        var result: Boolean = true

        textBoxCollection
            .document(id)
            .delete()
            .addOnFailureListener {
                result = false
            }
        return result
    }
}