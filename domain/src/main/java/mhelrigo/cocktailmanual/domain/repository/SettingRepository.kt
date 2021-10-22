package mhelrigo.cocktailmanual.domain.repository

import kotlinx.coroutines.flow.Flow
import mhelrigo.cocktailmanual.domain.entity.SettingEntity

interface SettingRepository {
    suspend fun getSettings() : Flow<SettingEntity>
    suspend fun modifySettings(p0: SettingEntity)
}