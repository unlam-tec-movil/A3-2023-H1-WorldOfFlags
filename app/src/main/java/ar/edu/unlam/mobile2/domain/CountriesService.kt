package ar.edu.unlam.mobile2.domain

import ar.edu.unlam.mobile2.data.CountriesRepo
import ar.edu.unlam.mobile2.model.CountryModel
import javax.inject.Inject

class CountriesService @Inject constructor(var repo: CountriesRepo) {

    suspend fun getCountry(): List<CountryModel>? {
        return repo.getCountry()
    }

}