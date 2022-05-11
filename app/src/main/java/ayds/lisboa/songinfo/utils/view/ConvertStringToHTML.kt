package ayds.lisboa.songinfo.utils.view

interface ConvertStringToHTML {

    fun convertTextToHtml(artistBio: String,artistName: String): String
}

internal class ConvertStringToHTMLImpl() : ConvertStringToHTML {


    override fun convertTextToHtml(artistBio: String,artistName: String):String {
        return  textToHtml(replaceLineBreakToText(artistBio), artistName)
    }

    private fun replaceLineBreakToText(artistBio: String): String{
        return artistBio.replace("\\n", "\n")
    }

    private fun textToHtml(text: String, term: String): String {
        return  StringBuilder().apply {
            append("<html><div width=400>")
            append("<font face=\"arial\">")
            append(artistBiographyTextWithBold(text, term))
            append("</font></div></html>")
        }.toString()
    }

    private fun artistBiographyTextWithBold(text: String, term: String): String {
        return text.apply {
            replace("'", " ")
            replace("\n", "<br>")
            replace("(?i)" + term.toRegex(), "<b>" + term.uppercase() + "</b>")
        }
    }
}