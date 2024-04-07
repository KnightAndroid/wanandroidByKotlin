package com.knight.kotlin.library_common.provider

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri



/**
 * Author:Knight
 * Time:2021/12/16 14:12
 * Description:AppContentProvider
 */
class AppContentProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        var context: Context = context ?: return false
        context = context.getApplicationContext()
        return if (context is Application) {
            ApplicationProvider.init(context)
            true
        } else {
            false
        }

    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
       return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}