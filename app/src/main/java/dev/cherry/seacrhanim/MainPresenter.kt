package dev.cherry.seacrhanim

import dev.cherry.seacrhanim.repository.CitiesRepository

/**
 * @author DVLP_2
 * @since 13.02.2017.
 */
class MainPresenter(val mCityRepo: CitiesRepository) {

    fun requestCities(name: String, lang: String) {
        mCityRepo.getCities(name, lang)
    }
}