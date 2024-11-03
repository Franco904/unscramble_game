package com.example.unscramble_game.core.data.utils

import android.content.Context
import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase

private const val DATABASE_VERSION_ASSET_PATH = "database/version_"
private const val SQL_FILE_TYPE = ".sql"

//fun Context.getMigrationFromAssetFolder(
//    startVersion: Int = 0,
//    endVersion: Int,
//) = object : Migration(startVersion, endVersion) {
//    override fun migrate(db: SupportSQLiteDatabase) {
//        migrate(db, endVersion = endVersion)
//    }
//}

fun Context.migrate(
    db: SupportSQLiteDatabase,
    endVersion: Int,
) {
    val endVersionFolder = "$DATABASE_VERSION_ASSET_PATH$endVersion"

    try {
        val sqlFiles = assets.list(endVersionFolder) ?: emptyArray()

        sqlFiles.filter { it.endsWith(SQL_FILE_TYPE) }.forEach { fileName ->
            val sqlFile = assets.open("$endVersionFolder/$fileName")
            val sqlStatements = sqlFile.bufferedReader().use { reader -> reader.readText() }

            sqlStatements.split(";").forEach { statement ->
                val trimmedStatement = statement.trim()

                if (trimmedStatement.isNotEmpty()) {
                    Log.d("migrate", "Executing SQL statements inside $endVersionFolder/$fileName")
                    db.execSQL(trimmedStatement)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        throw RuntimeException("Error during migration from assets folder: $endVersionFolder", e)
    }
}
