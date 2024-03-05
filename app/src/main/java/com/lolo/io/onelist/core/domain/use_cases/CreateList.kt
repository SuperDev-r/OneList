package com.lolo.io.onelist.core.domain.use_cases

import com.lolo.io.onelist.core.data.reporitory.OneListRepository
import com.lolo.io.onelist.core.model.ItemList
import javax.inject.Inject

class CreateList @Inject constructor(private val repository: OneListRepository) {

    suspend operator fun invoke(itemList: ItemList) {
        return repository.createList(itemList)
    }
}