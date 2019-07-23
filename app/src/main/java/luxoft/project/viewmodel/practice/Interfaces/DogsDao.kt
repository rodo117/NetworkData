package luxoft.project.viewmodel.practice.Interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import luxoft.project.viewmodel.practice.DataObjects.Dogs

@Dao
interface DogsDao {
    @Query("SELECT * from dogs_table ORDER BY dog ASC")
    fun getAllDogs(): LiveData<List<Dogs>>

    @Insert
    suspend fun insert(dog:Dogs)

    @Query("Delete FROM dogs_table")
    fun deleteAll()

}