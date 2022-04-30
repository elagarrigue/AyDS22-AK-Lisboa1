package ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm.sqldb

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm.LastFMLocalStorage

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "dictionary.db"

internal class LastFMLocalStorageImpl(
    context: Context,
    private val cursorToLastFMArtistBiographyMapper: CursorToLastFMArtistBiographyMapper
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), LastFMLocalStorage {

    private val projection = arrayOf(
        ID_COLUMN,
        ARTIST_COLUMN,
        INFO_COLUMN
    )

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createArtistTableQuery)
    }

    override fun onUpgrade(dataBase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    override fun saveArtist(artist: String?, info: String?) {
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

    override fun getInfo(artist: String?): String? {
        val cursor = cursorDefinition(artist)
        return cursorToLastFMArtistBiographyMapper.map(cursor)
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

}