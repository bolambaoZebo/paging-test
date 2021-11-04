package com.example.secondhiltapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.example.secondhiltapp.api.network.ApiSoccer
import com.example.secondhiltapp.api.network.SafeCallRequest
import com.example.secondhiltapp.db.AppRoom
import com.example.secondhiltapp.db.entity.IsClickable
import com.example.secondhiltapp.db.entity.LanguageData
import com.example.secondhiltapp.db.entity.SoccerNews
import com.example.secondhiltapp.utils.Resource
import com.example.secondhiltapp.utils.networkBoundResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepository @Inject constructor(
    private val apiSoccer: ApiSoccer,
    private val db: AppRoom
) : SafeCallRequest() {

    private val soccerDao = db.getSoccerDao()
    private val languageDao = db.getLanguageDao()

    private var _lang = MutableLiveData<LanguageData>()

    fun getSoccerNews(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<SoccerNews>>> =
        networkBoundResource(
            query = {
                soccerDao.getSoccer()
            },
            fetch = {
                val response = apiSoccer.getSoccerList()
                response
            },
            saveFetchResult = { soccer ->
                db.withTransaction {
                    soccerDao.deleteAllSoccerNews()
                    soccerDao.savaAllSoccer(soccer.body()?.en!!)
                }
            },
            shouldFetch = { cachedSoccerNews ->
                if (forceRefresh) {
                    true
                } else {
                    val sortedSoccer = cachedSoccerNews.sortedBy { soccer ->
                        soccer.uid
                    }
                    val oldestTimestamp = sortedSoccer.firstOrNull()?.uid
                    val needsRefresh = oldestTimestamp == null ||
                            oldestTimestamp < System.currentTimeMillis() -
                            TimeUnit.MINUTES.toMillis(60)
                    needsRefresh
                }
            },
            onFetchSuccess = onFetchSuccess,
            onFetchFailed = { t ->
                if (t !is HttpException && t !is IOException) {
                    throw t
                }
                onFetchFailed(t)
            }
        )

    fun getLanguageNow(): LiveData<LanguageData> {
        _lang.postValue(LanguageData("en"))
        return try {
            languageDao.getLanguage()
        } catch (e: Exception) {
            _lang
        }
    }

    fun saveLanguage(lang: LanguageData) {
        CoroutineScope(Dispatchers.IO).launch {
            languageDao.saveLanguage(lang)
        }
    }

    suspend fun deleteNonBookmarkedArticlesOlderThan(timestampInMillis: Long) {
       soccerDao.deleteAllSoccerNews()// newsArticleDao.deleteNonBookmarkedArticlesOlderThan(timestampInMillis)
    }

}