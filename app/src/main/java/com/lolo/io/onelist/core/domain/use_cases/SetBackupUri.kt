package com.lolo.io.onelist.core.domain.use_cases

import android.net.Uri
import com.lolo.io.onelist.core.data.reporitory.OneListRepository
import javax.inject.Inject

class SetBackupUri @Inject constructor(private val repository: OneListRepository) {

    suspend operator fun invoke(uri: Uri?, displayPath: String? = null) {
        repository.setBackupUri(uri, displayPath)
    }
}