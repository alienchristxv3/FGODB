package com.steventran.fgodb

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class ServantListViewModel: ViewModel() {

    val servantLiveData: LiveData<List<Servant>> = AtlasFetcher().fetchServants()

}