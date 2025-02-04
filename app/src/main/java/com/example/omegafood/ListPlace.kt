package com.example.omegafood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException
import java.util.ArrayList
import org.json.JSONArray
import org.json.JSONException
import androidx.recyclerview.widget.DividerItemDecoration

class ListPlace : AppCompatActivity() {


    private lateinit var mFoodPlaces: ArrayList<FoodPlace>
    private lateinit var mAdapter: PlaceAdapter
    private lateinit var recycler: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.rvFoodPlace)
        setupRecyclerView()
        generatePlaceFood()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(
                    this,
                    SettingsActivity::class.java
                )
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }



    private fun setupRecyclerView() {
        mFoodPlaces = arrayListOf()
        recycler.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        mAdapter = PlaceAdapter(mFoodPlaces, this) { foodPlace ->
            contactOnClick(foodPlace)
        }

        recycler.adapter = mAdapter
    }

    private fun contactOnClick(foodPlace: FoodPlace) {
        Log.d(TAG, "Click on: $foodPlace")
        foodPlace?.let {
            navigateToDetail(it)
        }
    }

    private fun navigateToDetail(foodPlace: FoodPlace) {
        val intent = Intent(this, DetailPlace::class.java)
        startActivity(intent)
    }

    private fun generatePlaceFood() {
        val placesString = readPlaceJsonFile()
        try {
            val placesJson = JSONArray(placesString)
            for (i in 0 until placesJson.length()) {
                val placeJson = placesJson.getJSONObject(i)
                val foodPlace = FoodPlace(
                    placeJson.getString("place_name"),
                    placeJson.getString("adress"),
                    placeJson.getString("email"),
                    placeJson.getString("imageUrl")
                )
                Log.d(TAG, "generatePlaceFood: $foodPlace")
                mFoodPlaces.add(foodPlace)
            }

            mAdapter.notifyDataSetChanged()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun readPlaceJsonFile(): String? {
        var placesString: String? = null
        try {
            val inputStream = assets.open("mock_placefood.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            placesString = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return placesString
    }


    companion object {
        private val TAG = ListPlace::class.java.simpleName
    }
}

