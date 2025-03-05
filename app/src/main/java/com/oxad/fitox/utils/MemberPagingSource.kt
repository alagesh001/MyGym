package com.oxad.fitox.utils

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.oxad.fitox.model.Member
import com.oxad.fitox.retrofit.ApiService
import retrofit2.HttpException
import java.io.IOException

class MemberPagingSource(private val apiService: ApiService,private val gymId: Int) : PagingSource<Int, Member>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Member> {
        var page = params.key ?: 1 // Start from page 1

        return try {
            Log.e("chckeca A ","$gymId $page")

            val response = apiService.getmymembers(gymId,page)
            val members = response.result
Log.e("chckeca B ",""+Gson().toJson(members))
            LoadResult.Page(
                data = members,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < response.totalPages) page + 1 else null
            )
        } catch (e: IOException) {
            LoadResult.Error(e) // Network error
        } catch (e: HttpException) {
            LoadResult.Error(e) // API error
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Member>): Int? {
        return state.anchorPosition
    }
}
