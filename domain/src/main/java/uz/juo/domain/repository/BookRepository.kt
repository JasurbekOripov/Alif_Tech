package uz.juo.domain.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import uz.juo.domain.models.BookData

interface BookRepository {
    fun getBooks(): Flow<Response<BookData>>
}