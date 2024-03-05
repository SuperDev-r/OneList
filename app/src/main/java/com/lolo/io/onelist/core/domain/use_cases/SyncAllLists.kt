package com.lolo.io.onelist.core.domain.use_cases

import com.lolo.io.onelist.core.data.reporitory.OneListRepository
import javax.inject.Inject

class SyncAllLists @Inject constructor(private val repository: OneListRepository) {

    suspend operator fun invoke() {
        repository.syncAllLists()
    }
}