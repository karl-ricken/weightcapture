package com.ricken.weightcaptureapplication.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ricken.weightcaptureapplication.IdElement
import com.ricken.weightcaptureapplication.database.`object`.Scale
import com.ricken.weightcaptureapplication.database.`object`.Weight
import com.ricken.weightcaptureapplication.database.table.TScale
import com.ricken.weightcaptureapplication.database.table.TWeight
import com.ricken.weightcaptureapplication.database.table.TableId
import java.util.*

class DataBase private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val tScale: TScale = TScale()
    private val tWeight: TWeight = TWeight()
    override fun onCreate(db: SQLiteDatabase) {
        var select = "CREATE TABLE " + tScale.name + "(" + tScale.id + " INTEGER PRIMARY KEY, " + tScale.label + " TEXT)"
        db.execSQL(select)
        select = ("CREATE TABLE " + tWeight.name
                + "(" + tWeight.id + " INTEGER PRIMARY KEY, "
                + tWeight.scale + " INTEGER, "
                + tWeight.time + " INTEGER, "
                + tWeight.value + " REAL)")
        db.execSQL(select)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + tScale.name)
        db.execSQL("DROP TABLE IF EXISTS " + tWeight.name)
        onCreate(db)
    }

    private fun addIdElement(element: IdElement, table: TableId, contentValues: ContentValues) {
        val db = writableDatabase
        val id = db.insert(table.name, null, contentValues)
        if (id > 0) {
            element.id = id.toInt()
        }
    }

    fun addScale(scale: Scale) {
        val contentValues = ContentValues()
        contentValues.put(tScale.label, scale.name)
        addIdElement(scale, tScale, contentValues)
    }

    private fun createScale(cursor: Cursor): Scale {
        val result = Scale()
        result.id = cursor.getInt(cursor.getColumnIndex(tWeight.id))
        result.name = cursor.getString(cursor.getColumnIndex(tScale.label))
        return result
    }

    fun getScales(): ArrayList<Scale> {
        val results = ArrayList<Scale>()
        val db = writableDatabase
        val query = "SELECT * FROM " + tScale.name
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                results.add(createScale(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return results
    }

    fun removeScale(id: Int) = this.writableDatabase.delete(tScale.name, tScale.id + "=?", arrayOf("" + id))

    fun addWeight(weight: Weight) {
        val contentValues = ContentValues()
        contentValues.put(tWeight.scale, weight.scale)
        contentValues.put(tWeight.time, weight.time)
        contentValues.put(tWeight.value, weight.value)
        addIdElement(weight, tWeight, contentValues)
    }

    private fun createWeight(cursor: Cursor): Weight {
        val result = Weight()
        result.id = cursor.getInt(cursor.getColumnIndex(tWeight.id))
        result.time = cursor.getLong(cursor.getColumnIndex(tWeight.time))
        result.value = cursor.getDouble(cursor.getColumnIndex(tWeight.value))
        return result
    }

    fun getLastWeight(): Weight? {
        var result: Weight? = null
        val db = writableDatabase
        val query = "SELECT * FROM " + tWeight.name + " ORDER BY " + tWeight.time + " DESC LIMIT 1"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            result = createWeight(cursor)
        }
        cursor.close()
        return result
    }

    private fun getWeights(select: String): ArrayList<Weight> {
        val results: ArrayList<Weight> = ArrayList()
        val db = writableDatabase
        val cursor = db.rawQuery(select, null)
        if (cursor.moveToFirst()) {
            do {
                results.add(createWeight(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return results
    }

    fun getWeightsDescending(): ArrayList<Weight> = getWeights("SELECT * FROM " + tWeight.name + " ORDER BY " + tWeight.time + " DESC")

    fun removeWeight(id: Int) = this.writableDatabase.delete(tWeight.name, tScale.id + "=?", arrayOf("" + id))

    companion object {
        private var instance: DataBase? = null
        fun getInstance(context: Context): DataBase {
            if (instance == null) {
                instance = DataBase(context)
            }
            return instance!!
        }

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "weight_capture"
    }

}