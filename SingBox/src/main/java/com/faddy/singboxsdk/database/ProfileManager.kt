package com.faddy.singboxsdk.database

import androidx.room.Room
import com.faddy.singboxsdk.constant.Path
import com.faddy.singboxsdk.di.UsedServices
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Suppress("RedundantSuspendModifier")
object ProfileManager {

    private val callbacks = mutableListOf<() -> Unit>()
    private lateinit var services: UsedServices
    fun init(passedServices: UsedServices) {
        services = passedServices
    }

    fun registerCallback(callback: () -> Unit) {
        callbacks.add(callback)
    }

    fun unregisterCallback(callback: () -> Unit) {
        callbacks.remove(callback)
    }

    private val instance by lazy {
        services.application.getDatabasePath(Path.PROFILES_DATABASE_PATH).parentFile?.mkdirs()
        Room.databaseBuilder(
            services.application, ProfileDatabase::class.java, Path.PROFILES_DATABASE_PATH
        ).fallbackToDestructiveMigration().enableMultiInstanceInvalidation()
            .setQueryExecutor { GlobalScope.launch { it.run() } }.build()
    }

    suspend fun nextOrder(): Long {
        return instance.profileDao().nextOrder() ?: 0
    }

    suspend fun nextFileID(): Long {
        return instance.profileDao().nextFileID() ?: 1
    }


    suspend fun get(id: Long): Profile? {
        return instance.profileDao().get(id)
    }

    suspend fun create(profile: Profile): Profile {
        profile.id = instance.profileDao().insert(profile)
        for (callback in callbacks.toList()) {
            callback()
        }
        return profile
    }

    suspend fun update(profile: Profile): Int {
        try {
            return instance.profileDao().update(profile)
        } finally {
            for (callback in callbacks.toList()) {
                callback()
            }
        }
    }

    suspend fun update(profiles: List<Profile>): Int {
        try {
            return instance.profileDao().update(profiles)
        } finally {
            for (callback in callbacks.toList()) {
                callback()
            }
        }
    }

    suspend fun delete(profile: Profile): Int {
        try {
            return instance.profileDao().delete(profile)
        } finally {
            for (callback in callbacks.toList()) {
                callback()
            }
        }
    }

    suspend fun delete(profiles: List<Profile>): Int {
        try {
            return instance.profileDao().delete(profiles)
        } finally {
            for (callback in callbacks.toList()) {
                callback()
            }
        }
    }

    suspend fun list(): List<Profile> {
        return instance.profileDao().list()
    }

}