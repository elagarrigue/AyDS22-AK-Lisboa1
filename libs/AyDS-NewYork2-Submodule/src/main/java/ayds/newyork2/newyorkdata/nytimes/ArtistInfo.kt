package ayds.newyork2.newyorkdata.nytimes

interface ArtistInfo {
    val artistName: String
    val artistInfo: String
    val artistUrl: String
}

data class NYTimesArtistInfo(
    override val artistName: String,
    override val artistInfo: String,
    override val artistUrl: String,
) : ArtistInfo {
    val source_logo_url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"
}

object EmptyArtistInfo : ArtistInfo {
    override val artistName: String = ""
    override val artistInfo: String = ""
    override val artistUrl: String = ""
}