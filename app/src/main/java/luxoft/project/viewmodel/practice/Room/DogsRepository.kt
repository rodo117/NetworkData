package luxoft.project.viewmodel.practice.Room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import luxoft.project.viewmodel.practice.DataObjects.Dogs
import luxoft.project.viewmodel.practice.Interfaces.DogsDao

class DogsRepository(private val dogsDao:DogsDao) {

    var allDogs: LiveData<List<Dogs>> = dogsDao.getAllDogs()


    @WorkerThread
    suspend fun insert(dog: Dogs) {
        dogsDao.insert(dog)
    }

}