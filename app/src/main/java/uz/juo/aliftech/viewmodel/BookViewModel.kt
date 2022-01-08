package uz.juo.aliftech.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.juo.aliftech.utils.BookResource
import uz.juo.data.room.AppDataBase
import uz.juo.domain.interactor.BookInteractor
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookInteractor: BookInteractor,
    val appDataBase: AppDataBase
) : ViewModel() {
    fun getBooks(): StateFlow<BookResource> {
        val stateFlow = MutableStateFlow<BookResource>(BookResource.Loading)
        viewModelScope.launch {
            bookInteractor.getBooks().collect {
                if (it.isSuccess) {
                    stateFlow.emit(BookResource.Success(it.getOrNull()))
                } else {
                    stateFlow.emit(BookResource.Error(it.exceptionOrNull()?.message ?: "Error"))
                }
            }
        }
        return stateFlow
    }

    var dao = appDataBase.dao()
}