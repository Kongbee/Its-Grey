package com.tntt.imagebox.repository

import com.tntt.imagebox.datasource.RemoteImageBoxDataSource
import com.tntt.imagebox.model.ImageBoxDto
import com.tntt.model.ImageBoxInfo
import com.tntt.repo.ImageBoxRepository
import javax.inject.Inject

class ImageBoxRepositoryImpl @Inject constructor(
    private val imageBoxDataSource: RemoteImageBoxDataSource
) : ImageBoxRepository {

    override fun createImageBoxInfo(pageId: String, imageBoxInfo: ImageBoxInfo): String {
        val imageBoxDto = ImageBoxDto("", pageId, imageBoxInfo.boxData)
        return imageBoxDataSource.createImageBoxDto(imageBoxDto)
    }

    override fun getImageBoxInfo(pageId: String): ImageBoxInfo {
        val imageBoxDto = imageBoxDataSource.getImageBoxDto(pageId)
        return ImageBoxInfo(imageBoxDto.id, imageBoxDto.boxData)
    }

    override fun updateImageBoxInfo(pageId: String, imageBoxInfo: ImageBoxInfo): Boolean {
        return imageBoxDataSource.updateImageBoxDto(ImageBoxDto(imageBoxInfo.id, pageId, imageBoxInfo.boxData))
    }

    override fun deleteImageBoxInfo(id: String): Boolean {
        return imageBoxDataSource.deleteImageBoxDto(id)
    }
}