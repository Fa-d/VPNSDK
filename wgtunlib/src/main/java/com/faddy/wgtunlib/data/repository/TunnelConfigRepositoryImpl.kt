package com.faddy.wgtunlib.data.repository

import com.faddy.wgtunlib.data.TunnelConfigDao
import com.faddy.wgtunlib.data.model.TunnelConfig
import com.faddy.wgtunlib.util.TunnelConfigs
import kotlinx.coroutines.flow.Flow

class TunnelConfigRepositoryImpl(private val tunnelConfigDao: TunnelConfigDao) :
    TunnelConfigRepository {
    override fun getTunnelConfigsFlow(): Flow<TunnelConfigs> {
        return tunnelConfigDao.getAllFlow()
    }

    override suspend fun getAll(): TunnelConfigs {
        return tunnelConfigDao.getAll()
    }

    override suspend fun save(tunnelConfig: TunnelConfig) {
        tunnelConfigDao.save(tunnelConfig)
    }

    override suspend fun delete(tunnelConfig: TunnelConfig) {
        tunnelConfigDao.delete(tunnelConfig)
    }

    override suspend fun count(): Int {
        return tunnelConfigDao.count().toInt()
    }
}
