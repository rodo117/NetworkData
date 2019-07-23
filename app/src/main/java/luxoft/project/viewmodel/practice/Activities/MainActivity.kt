package luxoft.project.viewmodel.practice.Activities

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import luxoft.project.viewmodel.R
import luxoft.project.viewmodel.practice.Interfaces.APIService
import luxoft.project.viewmodel.practice.Adapters.DogsAdapter
import luxoft.project.viewmodel.practice.DataObjects.Dogs
import luxoft.project.viewmodel.practice.DataObjects.DogsResponse
import luxoft.project.viewmodel.practice.ViewModels.DogsViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener {

    var imagesPuppies:List<String> = ArrayList<String> ()
    lateinit var dogsAdapter: DogsAdapter
    private lateinit var model: DogsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchBreed.setOnQueryTextListener(this)
        dogsAdapter = DogsAdapter(imagesPuppies)
        rvDogs.setHasFixedSize(true)
        rvDogs.layoutManager = LinearLayoutManager(this)
        rvDogs.adapter = dogsAdapter


        model = ViewModelProviders.of(this).get(DogsViewModel(application)::class.java)
        model.allWords.observe(this, Observer { dogs->let { println(dogs) } })

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchByName(query?.toLowerCase())
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query: String?) {

        GlobalScope.launch {
            try {

                val call = suspend {  getRetrofit().create(APIService::class.java).getCharacterByName("$query/images").execute()}
                val puppies = call.invoke().body()
                withContext(Dispatchers.Main) {
                    if (puppies?.status == "success") {
                        initCharacter(puppies)
                        addDogsToDatabase(puppies)
                    } else {
                        showErrorDialog()
                    }
                    hideKeyBoard()
                }

            } catch (e: Exception) {
                print("The Exception $e")
            }


        }
    }

    private fun addDogsToDatabase(puppies: DogsResponse){
        puppies.images.forEach{dogReceived->
            val dog = Dogs(dogReceived)
            model.insert(dog)
        }

    }


    private fun initCharacter(puppies: DogsResponse) {
        dogsAdapter.dataChanged(puppies.images)
    }

    private fun showErrorDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Error")
        builder.setMessage("Ha ocurrido un error, inténtelo de nuevo.")
        builder.setPositiveButton("Yes"){dialog, which ->
        }.show()
    }

    private fun hideKeyBoard(){
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(viewRoot.windowToken, 0)
    }


}
