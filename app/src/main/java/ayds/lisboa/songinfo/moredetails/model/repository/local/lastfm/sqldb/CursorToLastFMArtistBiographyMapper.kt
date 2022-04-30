package ayds.lisboa.songinfo.moredetails.model.repository.local.lastfm.sqldb

import ayds.lisboa.songinfo.moredetails.model.entities.LastFMArtistBiography
import android.database.Cursor
import java.util.ArrayList

interface CursorToLastFMArtistBiographyMapper{

    fun map(cursor: Cursor): String?
}

internal class CursorToLastFMArtistBiographyMapperImpl: CursorToLastFMArtistBiographyMapper {

    override fun map(cursor: Cursor): String? {
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