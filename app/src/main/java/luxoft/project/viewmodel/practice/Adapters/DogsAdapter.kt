package luxoft.project.viewmodel.practice.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_dog.view.*
import luxoft.project.viewmodel.R

class DogsAdapter (var images: List<String>) : RecyclerView.Adapter<DogsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = images[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            layoutInflater.inflate(
                R.layout.item_dog,
                parent,
                false
            )
        )
    }

    fun dataChanged(images: List<String>){
        this.images = images
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(image: String) {
            itemView.ivDog.fromUrl(image)
        }

        private fun ImageView.fromUrl(url:String){
            Picasso.get().load(url).into(this)
        }
    }


}