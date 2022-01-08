package uz.juo.domain.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import uz.juo.domain.models.BookData
import uz.juo.domain.repository.BookRepository
import javax.inject.Inject

class BookInteractor @Inject constructor(private val bookRepository: BookRepository) {
    fun getBooks(): Flow<Result<BookData>> {
        return bookRepository.getBooks().map {
            if (it.isSuccessful) {
                Result.success(it.body() ?: BookData())
            } else {
                Result.success(BookData())
            }
        }.catch { emit(Result.failure(it)) }
    }
}