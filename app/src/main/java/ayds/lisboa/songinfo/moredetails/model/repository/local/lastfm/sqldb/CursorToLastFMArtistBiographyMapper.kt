package ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm.sqldb

import ayds.lisboa.songinfo.moredetails.model.entities.LastFMArtistBiography
import android.database.Cursor
import ayds.lisboa.songinfo.moredetails.model.entities.ArtistBiography
import java.sql.SQLException

interface CursorToLastFMArtistBiographyMapper{

    fun map(cursor: Cursor): LastFMArtistBiography?
}

internal class CursorToLastFMArtistBiographyMapperImpl : CursorToLastFMArtistBiographyMapper {

    override fun map(cursor: Cursor): LastFMArtistBiography? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    LastFMArtistBiography(
                        id = getString(getColumnIndexOrThrow(ID_COLUMN)),
                        artist = getString(getColumnIndexOrThrow(ARTIST_COLUMN)),
                        biography = getString(getColumnIndexOrThrow(INFO_COLUMN)),
                        url = getString(getColumnIndexOrThrow(SOURCE_COLUMN)),
                    )
                } else {
                    null
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }
        /*
       val items: MutableList<String> = ArrayList()
       while (cursor.moveToNext()) {
           val info = cursor.getString(
               cursor.getColumnIndexOrThrow(INFO_COLUMN)
           )
           items.add(info)
       }
       cursor.close()
       return items.firstOrNull()
        */