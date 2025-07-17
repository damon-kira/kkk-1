

package com.kira.ui.core.storage.database.dao.base

import androidx.room.*

interface BaseDao<in T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg obj: T)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(obj: T)

    @Delete
    fun delete(obj: T)
}