package com.example.app_vinilos_g17.repositories

import android.app.Application
import com.example.app_vinilos_g17.models.Collector
import com.example.app_vinilos_g17.network.NetworkServiceAdapter
import com.example.app_vinilos_g17.network.CacheManager
import android.util.Log

class CollectorsRepository(val application: Application) {

    private val networkService = NetworkServiceAdapter.getInstance(application)
    private val cacheManager = CacheManager.getInstance(application.applicationContext)

    // Función para obtener la lista de coleccionistas
    suspend fun refreshData(): List<Collector> {
        val cachedCollectors = cacheManager.getCollectors()

        if (cachedCollectors.isNotEmpty()) {
            return cachedCollectors
        } else {
            Log.d("Cache decision", "Obteniendo coleccionistas por api.")
            val collectors = networkService.getCollectors()
            Log.d("Cache decision", "Almacenando información de coleccionistas en cache")
            cacheManager.addCollectors(collectors)

            return collectors
        }
    }

    suspend fun getCollectorDetail(collectorId: Int): Collector {
        val cachedCollector = cacheManager.getCollectorDetail(collectorId)

        return if (cachedCollector != null) {
            Log.d("Cache decision", "Se retorna información desde caché")
            cachedCollector
        } else {
            Log.d("Cache decision", "Se retorna información desde API")
            val collectorDetail = networkService.getCollectorDetail(collectorId)
            cacheManager.addCollectorDetail(collectorId, collectorDetail)
            collectorDetail
        }
    }

}
