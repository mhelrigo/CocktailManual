package mhelrigo.cocktailmanual.domain.usecase.settings

import kotlinx.coroutines.flow.Flow
import mhelrigo.cocktailmanual.domain.entity.SettingEntity
import mhelrigo.cocktailmanual.domain.repository.SettingRepository
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSettings @Inject constructor(val settingRepository: SettingRepository) :
    UseCase<Flow<SettingEntity>, GetSettings.Params>() {
    override suspend fun buildExecutable(params: Params): Flow<SettingEntity> {
        return settingRepository.getSettings()
    }

    class Params {}
}