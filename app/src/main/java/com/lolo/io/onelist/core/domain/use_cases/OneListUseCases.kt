package com.lolo.io.onelist.core.domain.use_cases

import javax.inject.Inject

data class OneListUseCases @Inject constructor(
    val createList: CreateList,
    val importList: ImportList,
    val moveList: MoveList,
    val editList: EditList,
    val getAllLists: GetAllLists,
    val setBackupUri: SetBackupUri,
    val removeList: RemoveList,
    val handleFirstLaunch: HandleFirstLaunch,
    val syncAllLists: SyncAllLists,
    val showWhatsNew: ShowWhatsNew,
)