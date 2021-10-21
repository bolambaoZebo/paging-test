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
import java.lang.Exception
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

    fun getSoccerNews() : Flow<Resource<List<SoccerNews>>> =
        networkBoundResource(
        query = {
            soccerDao.getSoccer()
        },
        fetch = {
            apiSoccer.getSoccerList()
        },
        saveFetchResult = { soccer ->
            db.withTransaction {
                soccerDao.deleteAllSoccerNews()
                soccerDao.savaAllSoccer(soccer.body()?.en!!)
            }
        }
    )

    fun getLanguageNow() : LiveData<LanguageData> {
        _lang.postValue(LanguageData("en"))
        return try {
            languageDao.getLanguage()
        }catch (e : Exception) {
            _lang
        }
    }

    fun saveLanguage(lang: LanguageData){
        CoroutineScope(Dispatchers.IO).launch {
            languageDao.saveLanguage(lang)
        }
    }

}