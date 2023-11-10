package com.example.pokedex

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.pokedex.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPokemonbyId(1)

        initUI()
    }

    private fun getPokemonbyId(id: Int){
        //Hilo secundario / Corrutinas
        CoroutineScope(Dispatchers.IO).launch {
            val pokemon = getRetrofit().create(ApiService::class.java).getPokemon(id)
            if (pokemon.body() != null){
                runOnUiThread{ //Hilo principal
                    crearUI(pokemon.body()!!)
                }
            }
        }
    }

    private fun initUI(){
        var numero = 1;
        binding.btnSigPokemon.setOnClickListener {

            numero++

            getPokemonbyId(numero)
          //  Log.i("julio",numeroAleatorio.toString())
        }

        binding.btnAntPokemon.setOnClickListener{

            numero--

            if (numero < 1){
                getPokemonbyId(1)
            }else{
                getPokemonbyId(numero)
            }

        }
    }

    fun getRandomNumber(): Int {
        val random = Random
        return random.nextInt(1, 152)
    }

    private fun crearUI(pokemon: PokemonResponse){
        binding.tvPokemonName.text = pokemon.name
        Picasso.get().load(pokemon.sprites.front_default).into(binding.ivPokemon)

        /*
        for (typeResponse in pokemon.types) {
            Log.i("julio", typeResponse.type.name)
        }*/
        binding.addTipo.removeAllViews()

        for (tipoResponse in pokemon.types) {

            val tipo = tipoResponse.type.name
            val colorId = tipoColorMap[tipo]

           // Log.i("julio", colorId.toString())
          //  Log.i("julio",tipo)

            if (colorId != null) {
                val cardView = createCardView(tipo, colorId)
                // Elimina todos los CardView anteriores
                binding.addTipo.addView(cardView) // Agrega el nuevo CardView
            }
        }

        //Obtiene los stats de cada pokemon
        for (stats in pokemon.stats){
            val statss = stats.stat.name
            val numero = stats.base_stat

          //  Log.i("julioStat", statss +" "+ numero.toString())

            when (statss) {
                "hp" -> updateHeight(binding.viewHp, numero.toString())
                "attack" -> updateHeight(binding.viewAttack, numero.toString())
                "defense" -> updateHeight(binding.viewDefence, numero.toString())
                "special-attack" -> updateHeight(binding.viewSpecialAttack, numero.toString())
                "special-defense" -> updateHeight(binding.viewSpecialDefense, numero.toString())
                "speed" -> updateHeight(binding.viewSpeed, numero.toString())
                else -> {
                    // Tratar casos desconocidos o no manejados
                }
            }
        }

    }

    /*
    private fun prepararStat(powerstats: StatsResponse) {

        updateHeight(binding.viewCombat, powerstats.base_stat)
        updateHeight(binding.viewDurability, powerstats.durability)
        updateHeight(binding.viewIntelligence, powerstats.intelligence)
        updateHeight(binding.viewPower, powerstats.power)
        updateHeight(binding.viewSpeed, powerstats.speed)
        updateHeight(binding.viewStrength, powerstats.strength)
    }*/

    private fun updateHeight(view: View, stat: String){
        var params = view.layoutParams
        params.height = pxToDp(stat.toFloat())
        view.layoutParams = params
    }

    ///Pasar de pixeles a DP
    private fun pxToDp(px: Float): Int{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.displayMetrics).roundToInt() //Redondea a Int
    }


    //Color por tipo de Pokemon
    val tipoColorMap = mapOf(
        "normal" to R.color.normal,
        "fighting" to R.color.lucha,
        "flying" to R.color.volador,
        "poison" to R.color.veneno,
        "ground" to R.color.tierra,
        "rock" to R.color.roca,
        "steel" to R.color.acero,
        "fire" to R.color.fuego,
        "ghost" to R.color.fantasma,
        "grass" to R.color.planta,
        "electric" to R.color.electrico,
        "psychic" to R.color.psiquico,
        "ice" to R.color.hielo,
        "dragon" to R.color.dragon,
        "fairy" to R.color.hada,
        "shadow" to R.color.siniestro,
        "bug" to R.color.bicho,
        "water" to R.color.agua
    )

    private fun createCardView(tipo: String, colorId: Int): CardView {
        val cardView = CardView(this)
        val layoutParams = LinearLayout.LayoutParams(
            resources.getDimension(R.dimen.card_width).toInt(),
            resources.getDimension(R.dimen.card_height).toInt()
        )
        layoutParams.setMargins(0, 0, resources.getDimension(R.dimen.card_margin).toInt(), 0)
        cardView.layoutParams = layoutParams

        cardView.cardElevation = resources.getDimension(R.dimen.card_elevation)
        cardView.radius = resources.getDimension(R.dimen.card_corner_radius)

        val textView = TextView(this)
        textView.text = tipo
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
        //textView.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        textView.setTypeface(textView.typeface, Typeface.BOLD)

        textView.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL // Centra el TextView en el CardView


        // Asigna el colorId o un color predeterminado si el tipo no está en el mapa
        val colorIdToUse = tipoColorMap.getOrDefault(tipo, R.color.default_color)
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, colorIdToUse))
        cardView.addView(textView)

        return cardView
    }


    private fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://pokeapi.co")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}