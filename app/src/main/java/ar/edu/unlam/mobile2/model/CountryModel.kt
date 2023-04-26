package ar.edu.unlam.mobile2.model

data class CountryModel (
    val capital: Array<String>,
    val flags : FlagsModel,
    val translations: TranslationsModel,
    val region: String,
    val subregion: String
    ) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CountryModel

        if (!capital.contentEquals(other.capital)) return false
        if (flags != other.flags) return false
        if (translations != other.translations) return false
        if (region != other.region) return false
        if (subregion != other.subregion) return false

        return true
    }

    override fun hashCode(): Int {
        var result = capital.contentHashCode()
        result = 31 * result + flags.hashCode()
        result = 31 * result + translations.hashCode()
        result = 31 * result + region.hashCode()
        result = 31 * result + subregion.hashCode()
        return result
    }
}