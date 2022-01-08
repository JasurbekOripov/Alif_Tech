package uz.juo.aliftech.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.juo.data.network.ApiService
import uz.juo.domain.models.Data

//class NewsSource :
//    PagingSource<Int, Data>() {
//    var count = 0
//
//    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
//        return state.anchorPosition
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
//        return try {
//            var pageNumber = params.key ?: 1
////            var list = ApiService.getBookData()
//            count = list.pagination.total.div(list.pagination.count)
//            if (count>=pageNumber){
//                if (pageNumber > 1) {
//                    LoadResult.Page(list.data, pageNumber - 1, pageNumber + 1)
//                } else {
//                    LoadResult.Page(list.data, null, pageNumber + 1)
//                }
//            }else{
//                LoadResult.Page(emptyList(), null, null)
//            }
//        } catch (e: Exception) {
//            LoadResult.Page(emptyList(), null, null)
//        }
//    }
//}