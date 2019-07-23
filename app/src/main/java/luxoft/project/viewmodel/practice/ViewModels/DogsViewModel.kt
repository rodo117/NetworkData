package luxoft.project.viewmodel.practice.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import luxoft.project.viewmodel.practice.DataObjects.Dogs
import luxoft.project.viewmodel.practice.Room.DogsRepository
import luxoft.project.viewmodel.practice.Room.DogsRoomDataBase


class DogsViewModel(applica: Application) : AndroidViewModel(applica) {

    private val repository:DogsRepository

    val allWords: LiveData<List<Dogs>>

    init{
        val dogsDao = DogsRoomDataBase.getDataBaseInstance(applica, viewModelScope).dogsDao()
        repository = DogsRepository(dogsDao)
        allWords = repository.allDogs
    }


    fun insert(dog: Dogs) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(dog)
    }

}