package fr.elamari.exo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    data class  TypeInfo (
        val name: String,
        val url: String,
    )

    data class  PokemonType (
        val type: TypeInfo
    )

    data class  PokemonSprites (
        val front_default: String,
    )

    data class PokemonInfo (
        val id: Int,
        val name: String,
        val sprites: PokemonSprites,
        val types: List<PokemonType>,
    )

    data class Pokemon (
        val name: String,
        val url: String,
        var info: PokemonInfo,
    )

    data class PokemonList (
        val results: List<Pokemon>
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pokemonRV : RecyclerView = findViewById(R.id.pokemonRecyclerView)

        loadPokemonList(151) { pokemonList ->
            val pokeAdapter = PokeAdapter(pokemonList.results as MutableList<Pokemon>)

            pokemonRV.adapter = pokeAdapter
            pokemonRV.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun loadPokemonList (limit: Int, cb: (PokemonList) -> Unit) {

        val httpAsync = "https://pokeapi.co/api/v2/pokemon?limit=${limit}&offset=0"
            .httpGet()
            .responseString { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                    }
                    is Result.Success -> {
                        val data = result.get()
                        val parsedJson = Gson().fromJson<PokemonList>(data, PokemonList::class.java)
                        for (i in 0 until limit) {
                            loadPokemonProfile(parsedJson.results[i].url) { pokemon ->
                                parsedJson.results[i].info = pokemon
                                if (parsedJson.results[i].info.id == limit) {
                                    cb(parsedJson)
                                }
                            }
                        }
                    }
                }
            }
        httpAsync.join()
    }

    private fun loadPokemonProfile (url: String, cb: (PokemonInfo) -> Unit) {

        val httpAsync = url
            .httpGet()
            .responseString { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val fail = result.getException()
                        println(fail)
                    }
                    is Result.Success -> {
                        val data = result.get()
                        val pokemon = Gson().fromJson<PokemonInfo>(data, PokemonInfo::class.java)
                        cb(pokemon)
                    }
                }
            }
        httpAsync.join()
    }
}