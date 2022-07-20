package fr.elamari.exo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokeAdapter(var dataSet: MutableList<MainActivity.Pokemon>) :
    RecyclerView.Adapter<PokeAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokemonImage: ImageView = view.findViewById(R.id.pokemonImage)
        val pokemonName: TextView = view.findViewById(R.id.pokemonName)
        val pokemonID: TextView = view.findViewById(R.id.pokemonID)
        val button_data: Button = view.findViewById(R.id.button_data)

        init {
            button_data.setOnClickListener {
                Log.d("Adapter", "you have clicked this pika button :)")
            }
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.pokemonName.text = dataSet[position].info.name
        viewHolder.pokemonID.text = dataSet[position].info.id.toString()
        Glide.with(viewHolder.pokemonImage)
            .load("https://raw.githubusercontent.com/PokeApi/sprites/master/sprites/pokemon/${position+1}.png")
            .into(viewHolder.pokemonImage)
    }


    override fun getItemCount() = dataSet.size
}