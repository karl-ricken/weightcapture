package com.ricken.weightcaptureapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.ricken.weightcaptureapplication.database.`object`.Weight
import com.ricken.weightcaptureapplication.database.DataBase
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var weights: ArrayList<Weight?>
    private lateinit var adapter: WeightListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weights = ArrayList()
        adapter = WeightListAdapter(this, weights)
        val listView = findViewById<View>(R.id.main_list_weights) as ListView
        listView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        reloadScales()
        reloadWeight()
    }

    fun reloadWeight() {
        reloadLastWeight()
        reloadWeightList()
    }

    private fun reloadWeightList() {
        weights.clear()
        weights.addAll(DataBase.getInstance(this).getWeightsDescending())
        adapter.notifyDataSetChanged()
    }

    private fun reloadLastWeight() {
        val tvValue = findViewById<View>(R.id.main_last_weight_value) as TextView
        val tvTime = findViewById<View>(R.id.main_last_weight_time) as TextView
        val weight: Weight? = DataBase.getInstance(this).getLastWeight()
        tvValue.text = if (weight != null) getReadableWeight(weight.value) else ""
        tvTime.text = if (weight != null) getReadableTime(weight.time) else ""
    }

    private fun reloadScales() {
        val layoutButtons = findViewById<View>(R.id.main_layout_button) as LinearLayout
        val scales = DataBase.getInstance(this).getScales()
        for (scale in scales) {
            val button = Button(this)
            button.text = scale.name
            layoutButtons.addView(button)
            button.setOnClickListener {
                val input = findViewById<View>(R.id.main_input) as EditText
                val text = input.text.toString()
                val value: Double = if (text.isEmpty()) .0 else text.toDouble()
                if (value > 0) {
                    val millis = System.currentTimeMillis()
                    val weight = Weight()
                    weight.scale = scale.id
                    weight.time = millis
                    weight.value = value
                    DataBase.getInstance(applicationContext).addWeight(weight)
                    Handler(Looper.getMainLooper()).post { Toast.makeText(applicationContext, R.string.added_element, Toast.LENGTH_LONG).show() }
                    reloadWeight()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu) //your file name
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_main_scales -> {
                val intent = Intent(this, ScaleActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun getReadableTime(millis: Long): String {
            val date = Date(millis)
            val sdf = SimpleDateFormat("dd.MM HH:mm:ss", Locale.getDefault())
            return sdf.format(date)
        }

        fun getReadableWeight(weight: Double): String = String.format(Locale.getDefault(), "%.1f", weight)
    }
}