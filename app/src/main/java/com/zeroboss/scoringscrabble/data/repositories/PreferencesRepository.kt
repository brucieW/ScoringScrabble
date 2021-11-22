package com.zeroboss.scoringscrabble.data.repositories

interface PreferencesRepository {
    fun getPreferences(): Preferences
    suspend fun savePreferences(preferencesData: Preferences)
}

class PreferencesRepositoryImpl constructor(
//    private val boxStore: BoxStore
) : PreferencesRepository {
    private val preferenceBox = boxStore.boxFor(Preferences::class)

    override fun getPreferences(): Preferences {
        if (preferenceBox.isEmpty) {
            preferenceBox.put(Preferences())
        }

        return preferenceBox.get(1)
    }

    override suspend fun savePreferences(preferencesData: Preferences) {
        val boxData = preferenceBox.get(1)
//        boxData.nonBiddingPointsType = preferencesData.nonBiddingPointsType
//        boxData.isTenTricksBonus = preferencesData.isTenTricksBonus

        preferenceBox.put(boxData)
    }
}