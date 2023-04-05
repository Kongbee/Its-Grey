package com.tntt.imagebox.repository

import android.util.Log
import com.tntt.imagebox.datasource.RemoteImageBoxDataSource
import com.tntt.imagebox.model.ImageBoxDto
import com.tntt.layer.datasource.RemoteLayerDataSource
import com.tntt.model.ImageBoxInfo
import com.tntt.repo.ImageBoxRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ImageBoxRepositoryImpl @Inject constructor(
    private val imageBoxDataSource: RemoteImageBoxDataSource,
    private val layerDataSource: RemoteLayerDataSource,
) : ImageBoxRepository {

    override suspend fun createImageBoxInfo(pageId: String, imageBoxInfo: ImageBoxInfo): Flow<String> = flow {
        layerDataSource.saveImage(imageBoxInfo.image, imageBoxInfo.id).collect() { url ->
            val imageBoxDto = ImageBoxDto(imageBoxInfo.id, pageId, imageBoxInfo.boxData, url.toString())
            imageBoxDataSource.createImageBoxDto(imageBoxDto).collect() { imageBoxId ->
                emit(imageBoxId)
            }
        }
    }

    override suspend fun getImageBoxInfoList(pageId: String): Flow<List<ImageBoxInfo>> = flow {
        val imageBoxInfoList = mutableListOf<ImageBoxInfo>()
        imageBoxDataSource.getImageBoxDtoList(pageId).collect() { imageBoxDtoList ->
            for (imageBoxDto in imageBoxDtoList) {
                layerDataSource.getImage(imageBoxDto.url).collect() { bitmap ->
                    imageBoxInfoList.add(ImageBoxInfo(imageBoxDto.id, imageBoxDto.boxData, bitmap))
                }
            }
            emit(imageBoxInfoList)
        }
    }

    override suspend fun updateImageBoxInfo(
        pageId: String,
        imageBoxInfo: ImageBoxInfo
    ): Flow<Boolean> = flow {
        layerDataSource.saveImage(imageBoxInfo.image, imageBoxInfo.id).collect() { url ->
            imageBoxDataSource.updateImageBoxDto(ImageBoxDto(imageBoxInfo.id, pageId, imageBoxInfo.boxData, url.toString())).collect() { result ->
                emit(result)
            }
        }
    }

    override suspend fun updateImageBoxInfoList(pageId: String, imageBoxInfoList: List<ImageBoxInfo>): Flow<Boolean> = flow {
        Log.d("function test","updateImageBoxInfoList")
        val imageBoxDtoList = mutableListOf<ImageBoxDto>()
        for (imageBoxInfo in imageBoxInfoList) {
            layerDataSource.saveImage(imageBoxInfo.image, imageBoxInfo.id).collect() { url ->
                imageBoxDtoList.add(ImageBoxDto(imageBoxInfo.id, pageId, imageBoxInfo.boxData, url.toString()))
            }
        }
        imageBoxDataSource.updateImageBoxDtoList(imageBoxDtoList).collect() { result ->
            emit(result)
        }
    }

    override suspend fun deleteImageBoxInfo(id: String): Flow<Boolean> = flow {
        imageBoxDataSource.deleteImageBoxDto(id).collect() { result ->
            emit(result)
        }
    }
}