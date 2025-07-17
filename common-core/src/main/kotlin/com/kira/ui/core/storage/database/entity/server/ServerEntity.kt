

package com.kira.ui.core.storage.database.entity.server

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kira.ui.core.storage.database.utils.Tables

@Entity(tableName = Tables.SERVERS)
data class ServerEntity(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    val uuid: String,
    @ColumnInfo(name = "scheme")
    val scheme: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "port")
    val port: Int,
    @ColumnInfo(name = "initial_dir")
    val initialDir: String,
    @ColumnInfo(name = "auth_method")
    val authMethod: Int,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "password")
    val password: String?,
    @ColumnInfo(name = "private_key")
    val privateKey: String?,
    @ColumnInfo(name = "passphrase")
    val passphrase: String?,
)