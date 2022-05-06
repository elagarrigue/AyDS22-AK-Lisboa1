package ayds.lisboa.songinfo.moredetails

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm.sqldb.*
import java.util.ArrayList

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "dictionary.db"

class DataBase(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN
    )

    override fun onCreate(dataBase: SQLiteDatabase) {
        dataBase.execSQL(createArtistTableQuery)
    }

    override fun onUpgrade(dataBase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun saveArtist(artist: String?, info: String?) {
        writableDatabase?.insert(ARTIST_TABLE, null, createMapValues(artist, info))
    }

    private fun createMapValues(artist: String?, info: String?): ContentValues {
        val values = ContentValues().apply {
            put(ARTIST_COLUMN, artist)
            put(INFO_COLUMN, info)
            put(SOURCE_COLUMN, 1)
        }
        return values
    }

    fun getInfo(artist: String?): String? {
        val cursor = cursorDefinition(artist)
        return cursorToFirstInfoMapper(cursor)
    }

    private fun cursorDefinition(artist: String?): Cursor {
        return readableDatabase.query(
            ARTIST_TABLE,
            projection,
            SELECTION,
            arrayOf(artist),
            null,
            null,
            SORT_ORDER
        )
    }

    private fun cursorToFirstInfoMapper(cursor: Cursor): String? {
        val items: MutableList<String> = ArrayList()
        while (cursor.moveToNext()) {
            val info = cursor.getString(
                cursor.getColumnIndexOrThrow(INFO_COLUMN)
            )
            items.add(info)
        }
        cursor.close()
        return items.firstOrNull()
    }
}