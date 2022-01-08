package uz.juo.aliftech.utils

import uz.juo.domain.models.BookData

sealed class BookResource {
    object Loading : BookResource()
    data class Error(private val message:String) : BookResource()
    data class Success(val data:BookData?) : BookResource()

}