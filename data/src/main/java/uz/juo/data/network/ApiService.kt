package uz.juo.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uz.juo.domain.models.BookData

interface ApiService {

    @GET("upcomingGuides/")
    suspend fun getBookData(@Query("limit") limit: Int = 3): Response<BookData>
}