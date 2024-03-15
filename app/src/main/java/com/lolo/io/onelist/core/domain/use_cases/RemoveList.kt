package com.lolo.io.onelist.core.domain.use_cases

import com.lolo.io.onelist.core.data.reporitory.OneListRepository
import com.lolo.io.onelist.core.model.ItemList
import javax.inject.Inject
import kotlin.jvm.Throws

class RemoveList @Inject constructor(private val repository: OneListRepository) {

    @Throws
    suspend operator fun invoke(itemList: ItemList,
                                deleteBackupFile: Boolean,
                                onFieDeleted: () -> Unit) {
        repository.deleteList(itemList, deleteBackupFile, onFieDeleted)
    }
}