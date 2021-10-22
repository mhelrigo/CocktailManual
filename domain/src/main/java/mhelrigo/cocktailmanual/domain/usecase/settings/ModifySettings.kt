package mhelrigo.cocktailmanual.domain.usecase.settings

import mhelrigo.cocktailmanual.domain.entity.SettingEntity
import mhelrigo.cocktailmanual.domain.repository.SettingRepository
import mhelrigo.cocktailmanual.domain.usecase.base.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ModifySettings @Inject constructor(val settingRepository: SettingRepository) :
    UseCase<Unit, ModifySettings.Params>() {

    override suspend fun buildExecutable(params: Params) {
        return settingRepository.modifySettings(params.settingEntity)
    }

    class Params(val settingEntity: SettingEntity)
}