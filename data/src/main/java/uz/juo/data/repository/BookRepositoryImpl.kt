package uz.juo.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import uz.juo.data.network.ApiService
import uz.juo.domain.models.BookData
import uz.juo.domain.repository.BookRepository
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(private val apiService: ApiService) : BookRepository {
    override fun getBooks(): Flow<Response<BookData>> {
        return flow { emit(apiService.getBookData()) }
    }

}