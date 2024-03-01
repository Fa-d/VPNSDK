package com.faddy.wgtunlib.data.repository

import com.faddy.wgtunlib.data.model.TunnelConfig
import com.faddy.wgtunlib.util.TunnelConfigs
import kotlinx.coroutines.flow.Flow

interface TunnelConfigRepository {

    fun getTunnelConfigsFlow(): Flow<TunnelConfigs>

    suspend fun getAll(): TunnelConfigs

    suspend fun save(tunnelConfig: TunnelConfig)

    suspend fun delete(tunnelConfig: TunnelConfig)

    suspend fun count(): Int
}
