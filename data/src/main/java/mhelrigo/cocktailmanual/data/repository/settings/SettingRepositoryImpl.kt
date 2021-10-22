package mhelrigo.cocktailmanual.data.repository.settings

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mhelrigo.cocktailmanual.domain.entity.SettingEntity
import mhelrigo.cocktailmanual.domain.repository.SettingRepository
import javax.inject.Inject
import javax.inject.Singleton

const val SETTINGS_NIGHT_MODE = "SETTINGS_NIGHT_MODE"

@Singleton
class SettingRepositoryImpl @Inject constructor(val sharedPreferences: SharedPreferences) :
    SettingRepository {

    val defaultNightMode = true

    override suspend fun getSettings(): Flow<SettingEntity> = flow {
        val v0: SettingEntity =
            SettingEntity(sharedPreferences.getBoolean(SETTINGS_NIGHT_MODE, defaultNightMode))
        emit(v0)
    }

    override suspend fun modifySettings(p0: SettingEntity) {
        sharedPreferences.edit().putBoolean(SETTINGS_NIGHT_MODE, p0.isNightMode).apply()
    }
}