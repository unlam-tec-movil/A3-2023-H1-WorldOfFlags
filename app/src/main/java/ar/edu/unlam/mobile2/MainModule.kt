package ar.edu.unlam.mobile2

import ar.edu.unlam.mobile2.data.CountriesRepo
import ar.edu.unlam.mobile2.data.CountriesRestRepo
import ar.edu.unlam.mobile2.data.KittiesRepo
import ar.edu.unlam.mobile2.data.KittiesRestRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainModule {

    @Binds
    abstract fun bindKittiesRepo(kittiesRestRepo: KittiesRestRepo): KittiesRepo

    @Binds
    abstract fun bindCountriesRepo(countriesRestRepo: CountriesRestRepo): CountriesRepo
}
