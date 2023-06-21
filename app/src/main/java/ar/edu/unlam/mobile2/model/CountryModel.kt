package ar.edu.unlam.mobile2.model

import java.io.Serializable

data class CountryModel (
    var name: TranslationsOptionsModel,
    var capital: List<String>,
    var flags : FlagsModel,
    var translations: TranslationsModel,
    var region: String,
    var subregion: String,
    var latlng: Array<Double>,
    ) : Serializable {
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as CountryModel
        
        if (name != other.name) return false
        if (capital != other.capital) return false
        if (flags != other.flags) return false
        if (translations != other.translations) return false
        if (region != other.region) return false
        if (subregion != other.subregion) return false
        if (!latlng.contentEquals(other.latlng)) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + capital.hashCode()
        result = 31 * result + flags.hashCode()
        result = 31 * result + translations.hashCode()
        result = 31 * result + region.hashCode()
        result = 31 * result + subregion.hashCode()
        result = 31 * result + latlng.contentHashCode()
        return result
    }
}