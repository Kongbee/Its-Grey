package com.tntt.book.repository

import android.util.Log
import com.tntt.book.datasource.RemoteBookDataSource
import com.tntt.book.model.BookDto
import com.tntt.model.BookType
import com.tntt.model.SortType
import com.tntt.model.BookInfo
import com.tntt.repo.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookDataSource: RemoteBookDataSource
) : BookRepository {

    override suspend fun getBookInfo(bookId: String): Flow<BookInfo> = flow {
        Log.d("function test", "getBookInfo(${bookId})")
        bookDataSource.getBookDto(bookId).collect() { bookDto ->
            val id = bookDto.id
            val title = bookDto.title
            val saveDate = bookDto.saveDate
            emit(BookInfo(id, title, saveDate))
        }
    }

    override suspend fun getBookInfoList(userId: String, sortType: SortType, startIndex: Long, bookType: BookType): Flow<List<BookInfo>> = flow {
        Log.d("function test", "getBookInfoList(${userId}, ${sortType}, ${startIndex}, ${bookType})")
        bookDataSource.getBookDtoList(userId, sortType, startIndex, bookType).collect() { bookDtoList ->
            Log.d("function test", "bookDtoList = ${bookDtoList}")
            val bookList = mutableListOf<BookInfo>()
            for (bookDto in bookDtoList){
                bookList.add(BookInfo(bookDto.id, bookDto.title, bookDto.saveDate))
            }
            Log.d("function test", "emit(${bookList})")
            emit(bookList)
        }
    }

    override suspend fun createBookInfo(userId: String): Flow<String> = flow {
        Log.d("function test", "createBookInfo(${userId})")
        bookDataSource.createBookDto(userId).collect() { bookDtoId ->
            emit(bookDtoId)
        }
    }

    override suspend fun updateBookInfo(bookInfo: BookInfo, userId: String, bookType: BookType): Flow<Boolean> = flow {
        Log.d("function test=======================", "updateBookInfo(${bookInfo}, ${userId}, ${bookType})")
        bookDataSource.updateBookDto(BookDto(bookInfo.id, userId, bookInfo.title, bookType, bookInfo.saveDate)).collect() { result ->
            emit(result)
        }
    }

    override suspend fun deleteBookInfo(bookIdList: List<String>): Flow<Boolean> = flow {
        Log.d("function test=======================", "deleteBookInfo(${bookIdList})")
        bookDataSource.deleteBookDto(bookIdList).collect() { result ->
            emit(result)
        }
    }
}