package com.knight.kotlin.module_mine.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.module_mine.api.HistoryRecordService
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2022/5/17 18:02
 * Description:HistoryRecordsRepo
 */
class HistoryRecordsRepo @Inject constructor() : BaseRepository() {

    @Inject
    lateinit var mHistoryRecordService: HistoryRecordService
}