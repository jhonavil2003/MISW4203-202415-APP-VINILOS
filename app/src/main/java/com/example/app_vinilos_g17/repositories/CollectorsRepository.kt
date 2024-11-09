package com.example.app_vinilos_g17.repositories

import android.app.Application
import com.example.app_vinilos_g17.models.Collector
import com.example.app_vinilos_g17.network.NetworkServiceAdapter

class CollectorsRepository (val application: Application){
    suspend fun refreshData(): List<Collector> {
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        return NetworkServiceAdapter.getInstance(application).getCollectors()
    }

}
