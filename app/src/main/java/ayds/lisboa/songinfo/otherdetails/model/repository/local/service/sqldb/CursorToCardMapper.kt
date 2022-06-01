package ayds.lisboa.songinfo.otherdetails.model.repository.local.service.sqldb

import android.database.Cursor
import ayds.lisboa.songinfo.otherdetails.model.entities.ServiceCard
import java.sql.SQLException

interface CursorToCardMapper{
    fun map(cursor: Cursor): ServiceCard?
}

internal class CursorToCardMapperImpl : CursorToCardMapper {

    override fun map(cursor: Cursor): ServiceCard? =
        try {
            with(cursor) {
                if (moveToNext()) {
                    ServiceCard(
                        term = getString(getColumnIndexOrThrow(ARTIST_COLUMN)),
                        description = getString(getColumnIndexOrThrow(DESCRIPTION_COLUMN)),
                        infoUrl = getString(getColumnIndexOrThrow(INFO_URL_COLUMN)),
                        source = getString(getColumnIndexOrThrow(SOURCE_COLUMN)),
                        sourceLogoUrl = getString(getColumnIndexOrThrow(SOURCE_LOGO_URL_COLUMN))
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