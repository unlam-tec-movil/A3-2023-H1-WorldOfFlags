package ar.edu.unlam.mobile2.data

import ar.edu.unlam.mobile2.model.CountryModel
import retrofit2.Retrofit
import javax.inject.Inject
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory

class CountriesRestRepo @Inject constructor(builder: Builder) : CountriesRepo{

    var retrofit: Retrofit = builder
        .baseUrl("https://restcountries.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override suspend fun getCountry(): List<CountryModel>? {
        var countryApi = retrofit.create(CountriesAPI::class.java)

        var call = countryApi.getCountries()
        val country = call.body()

        if (call.isSuccessful) {
            return country
        }
        return emptyList()
    }
    
    override suspend fun getCountryByName(name: String): List<CountryModel>? {
        val countryApi = retrofit.create(CountriesAPI::class.java)
        val call = countryApi.getCountryByName(name)
        val country = call.body()
        if (call.isSuccessful){
            return country
        }
        return null
    }
}
