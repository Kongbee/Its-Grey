package com.tntt.layer.repository

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.tntt.imagebox.datasource.RemoteImageBoxDataSource
import com.tntt.layer.datasource.RemoteLayerDataSource
import com.tntt.layer.model.LayerDto
import com.tntt.model.LayerInfo
import com.tntt.repo.LayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class LayerRepositoryImpl @Inject constructor(
    private val layerDataSource: RemoteLayerDataSource,
    private val imageBoxDataSource: RemoteImageBoxDataSource
): LayerRepository {

    override suspend fun createLayerInfoList(imageBoxId: String, layerInfoList: List<LayerInfo>): Flow<List<LayerInfo>> = flow {
        val layerDtoList = mutableListOf<LayerDto>()
        for (layerInfo in layerInfoList) {
            layerDataSource.saveImage(layerInfo.bitmap, layerInfo.url).collect() { uri ->
                layerDtoList.add(LayerDto(layerInfo.id, imageBoxId, layerInfo.order, uri.toString()))
            }
        }
        layerDataSource.createLayerDtoList(layerDtoList).collect() { layerDtoList ->
            emit(layerInfoList)
        }
    }

    override suspend fun getLayerInfoList(imageBoxId: String): Flow<List<LayerInfo>> = flow {
        val layerInfoList = mutableListOf<LayerInfo>()
        layerDataSource.getLayerDtoList(imageBoxId).collect() { layerDtoList ->
            for (layerDto in layerDtoList) {
                layerDataSource.getImage(layerDto.url).collect() { bitmap ->
                    layerInfoList.add(LayerInfo(layerDto.id, layerDto.order, bitmap, layerDto.url))
                }
            }
            emit(layerInfoList)
        }
    }

    override suspend fun updateLayerInfoList(imageBoxId: String, layerInfoList: List<LayerInfo>): Flow<Boolean> = flow {
        val layerDtoList = mutableListOf<LayerDto>()

        for (layerInfo in layerInfoList) {
            layerDataSource.saveImage(layerInfo.bitmap, layerInfo.url).collect() { url ->
                layerDtoList.add(LayerDto(layerInfo.id, imageBoxId, layerInfo.order, url.toString()))
            }
        }
        layerDataSource.updateLayerDtoList(layerDtoList).collect() { result ->
            emit(result)
        }
    }

    override suspend fun deleteLayerInfoList(imageBoxId: String): Flow<Boolean> = flow {
        layerDataSource.deleteLayerDtoList(imageBoxId).collect() { result ->
            emit(result)
        }
    }

    override suspend fun getSketchBitmap(bitmap: Bitmap): Flow<Bitmap> = flow {
        layerDataSource.getSketchBitmap(bitmap).collect() { bitmap ->
            emit(bitmap)
        }
    }

    override suspend fun saveImage(bitmap: Bitmap, url: String): Flow<Uri?> = flow {
        layerDataSource.saveImage(bitmap, url).collect() { url ->
            emit(url)
        }
    }

    override suspend fun getImage(uri: String): Flow<Bitmap> = flow {
        layerDataSource.getImage(uri).collect() { bitmap ->
            emit(bitmap)
        }
    }
}