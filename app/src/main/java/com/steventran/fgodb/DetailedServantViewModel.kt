package com.steventran.fgodb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailedServantViewModel: ViewModel() {
    var detailedServantLiveData: LiveData<DetailedServant> = MutableLiveData<DetailedServant>()

    fun getServant(servantId: Int) {
        detailedServantLiveData = AtlasFetcher().fetchDetailedServant(servantId)
    }
}