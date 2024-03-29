package com.lolo.io.onelist.core.data.reporitory

import android.net.Uri
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.lolo.io.onelist.core.data.file_access.FileAccess
import com.lolo.io.onelist.core.data.model.AllListsWithErrors
import com.lolo.io.onelist.core.data.model.ErrorLoadingList
import com.lolo.io.onelist.core.data.shared_preferences.SharedPreferencesHelper
import com.lolo.io.onelist.core.data.utils.toItemListEntity
import com.lolo.io.onelist.core.database.dao.ItemListDao
import com.lolo.io.onelist.core.database.util.toItemListModel
import com.lolo.io.onelist.core.database.util.toItemListModels
import com.lolo.io.onelist.core.model.ItemList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import updateOne
import java.io.FileNotFoundException
import kotlin.coroutines.coroutineContext


class OneListRepository(
    private val preferences: SharedPreferencesHelper,
    private val dao: ItemListDao,
    private val fileAccess: FileAccess
) {

    private val _allListsWithErrors =
        MutableStateFlow(AllListsWithErrors())

    suspend fun getAllLists(): Flow<AllListsWithErrors> {
        withContext(Dispatchers.IO) {
            val allListsFromDb = dao.getAll()
            val errors = mutableListOf<ErrorLoadingList>()
            val lists = if (
                preferences.preferUseFiles &&
                preferences.backupUri != null
            ) {
                allListsFromDb.map {
                    val list = it.toItemListModel()
                    try {
                        supervisorScope {
                            fileAccess.getListFromLocalFile(list)
                        }
                    } catch (e: SecurityException) {
                        errors += ErrorLoadingList.PermissionDeniedError
                        list
                    } catch (e: FileNotFoundException) {
                        errors += ErrorLoadingList.FileMissingError
                        list
                    } catch (e: IllegalArgumentException) {
                        errors += ErrorLoadingList.FileMissingError
                        print(e)
                        list
                    } catch (e: JsonSyntaxException) {
                        errors += ErrorLoadingList.FileCorruptedError
                        list
                    } catch (e: JsonIOException) {
                        errors += ErrorLoadingList.FileCorruptedError
                        list
                    } catch (e: Exception) {
                        errors += ErrorLoadingList.UnknownError
                        list
                    }
                }
            } else {
                allListsFromDb.toItemListModels()
            }

            _allListsWithErrors.value = AllListsWithErrors(
                lists = lists,
                errors = errors.distinct(),
            )
        }

        return _allListsWithErrors
    }

    suspend fun createList(itemList: ItemList) {
        itemList.position = _allListsWithErrors.value.lists.size
        _allListsWithErrors.value =
            AllListsWithErrors(_allListsWithErrors.value.lists + upsertList(itemList))
        preferences.selectedListIndex = _allListsWithErrors.value.lists.size - 1
    }

    suspend fun editList(itemList: ItemList) {
        upsertList(itemList).let {
            _allListsWithErrors.value = AllListsWithErrors(
                _allListsWithErrors.value.lists.updateOne(itemList) { it.id == itemList.id })
        }
    }

    private suspend fun upsertList(list: ItemList): ItemList {
        return withContext(Dispatchers.IO) {
            upsertInDao(list)
            if (preferences.backupUri != null) {
                fileAccess.saveListFile(
                    preferences.backupUri,
                    list,
                    onNewFileCreated = { list, uri ->
                        list.uri = uri
                        upsertInDao(list)
                        _allListsWithErrors.value = AllListsWithErrors(
                            _allListsWithErrors.value.lists.updateOne(list) { it.id == list.id })
                    })
            } else list
        }
    }


    private suspend fun upsertInDao(list: ItemList) {
        withContext(Dispatchers.IO) {
            dao.upsert(list.toItemListEntity()).takeIf { it > 0 }?.let {
                list.id = it
            }
        }
    }

    @Throws
    suspend fun deleteList(
        itemList: ItemList,
        deleteBackupFile: Boolean,
        onFileDeleted: () -> Unit
    ) {

        withContext(Dispatchers.IO) {
            dao.delete(itemList.toItemListEntity())
        }

        _allListsWithErrors.value =
            AllListsWithErrors(_allListsWithErrors.value.lists
                .filter { it.id != itemList.id })
                .also {
                    val position = _allListsWithErrors.value.lists.indexOf(itemList)
                    if (position < it.lists.size) {
                        selectList(position)
                    } else if (position > 0) {
                        selectList(position - 1)
                    }
                }

        if (deleteBackupFile) {
            fileAccess.deleteListBackupFile(itemList, onFileDeleted)
        }
    }

    suspend fun importList(uri: Uri): ItemList {
        val list = fileAccess.createListFromUri(uri,
            onListCreated = {
                upsertList(
                    it.copy(
                        id = 0L,
                        position = _allListsWithErrors.value.lists.size
                    )
                )
            })
        getAllLists()
        preferences.selectedListIndex = _allListsWithErrors.value.lists.size - 1
        return list
    }

    fun selectList(position: Int) {
        preferences.selectedListIndex =
            position
        preferences.selectedListIndex = position
    }

    suspend fun saveAllLists(lists: List<ItemList>) {
        _allListsWithErrors.value = AllListsWithErrors(lists)
        coroutineScope {
            // update async to improve list move performance.
            CoroutineScope(Dispatchers.IO).launch {
                lists.forEach {
                    upsertInDao(it)
                }
            }
        }

    }

    suspend fun syncAllLists() {
        preferences.backupUri?.let {
            _allListsWithErrors.value.lists.forEach {
                upsertList(it)
            }
        }
    }

    suspend fun setBackupUri(uri: Uri?, displayPath: String?) {
        if (uri != null) {
            preferences.backupUri = uri.toString()
            preferences.backupDisplayPath = displayPath
            syncAllLists()
        } else {
            preferences.backupUri = null
            preferences.backupDisplayPath = null
            fileAccess.revokeAllAccessFolders()
        }
    }
}