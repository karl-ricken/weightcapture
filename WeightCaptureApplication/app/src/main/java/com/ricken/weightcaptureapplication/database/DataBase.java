package com.ricken.weightcaptureapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ricken.weightcaptureapplication.IdElement;
import com.ricken.weightcaptureapplication.database.object.Scale;
import com.ricken.weightcaptureapplication.database.object.Weight;
import com.ricken.weightcaptureapplication.database.table.TScale;
import com.ricken.weightcaptureapplication.database.table.TWeight;
import com.ricken.weightcaptureapplication.database.table.TableId;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper{
    private static DataBase instance;
    public static DataBase getInstance(Context context){
        if(instance == null){
            instance = new DataBase(context);
        }
        return instance;
    }
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "weight_capture";

    private final TScale tScale;
    private final TWeight tWeight;
    private DataBase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        tScale = new TScale();
        tWeight = new TWeight();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String select = "CREATE TABLE " + tScale.getName() + "(" + tScale.id + " INTEGER PRIMARY KEY, " + tScale.label + " TEXT)";
        db.execSQL(select);
        select = "CREATE TABLE " + tWeight.getName()
                + "(" + tWeight.id + " INTEGER PRIMARY KEY, "
                + tWeight.scale + " INTEGER, "
                + tWeight.time + " INTEGER, "
                + tWeight.value + " REAL)";
        db.execSQL(select);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tScale.getName());
        db.execSQL("DROP TABLE IF EXISTS " + tWeight.getName());

        onCreate(db);
    }

    private void addIdElement(IdElement element, TableId table, ContentValues contentValues){
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(table.getName(), null, contentValues);
        if(id > 0){
            element.setId((int) id);
        }
    }

    public void addScale(Scale scale){
        ContentValues contentValues = new ContentValues();
        contentValues.put(tScale.label, scale.getName());
        addIdElement(scale, tScale, contentValues);
    }

    private Scale createScale(Cursor cursor){
        Scale result = new Scale();
        result.setId(cursor.getInt(cursor.getColumnIndex(tWeight.id)));
        result.setName(cursor.getString(cursor.getColumnIndex(tScale.label)));
        return result;
    }
    public Scale getScale(int id){
        Scale result = null;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + tScale.getName() + " WHERE " + tScale.id + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{ "" + id });
        if(cursor.moveToFirst()){
            result = createScale(cursor);
        }
        cursor.close();
        return result;
    }
    public ArrayList<Scale> getScales(){
        ArrayList<Scale> results = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + tScale.getName();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                results.add(createScale(cursor));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return results;
    }
    public void removeScale(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tScale.getName(), tScale.id + "=?", new String[]{ "" + id });
    }

    public void addWeight(Weight weight){
        ContentValues contentValues = new ContentValues();
        contentValues.put(tWeight.scale, weight.getScale());
        contentValues.put(tWeight.time, weight.getTime());
        contentValues.put(tWeight.value, weight.getValue());
        addIdElement(weight, tWeight, contentValues);
    }
    private Weight createWeight(Cursor cursor){
        Weight result = new Weight();
        result.setId(cursor.getInt(cursor.getColumnIndex(tWeight.id)));
        result.setTime(cursor.getLong(cursor.getColumnIndex(tWeight.time)));
        result.setValue(cursor.getDouble(cursor.getColumnIndex(tWeight.value)));
        return result;
    }
    public Weight getWeight(int id){
        Weight result = null;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + tWeight.getName() + " WHERE " + tWeight.id + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{ "" + id });
        if(cursor.moveToFirst()){
            result = createWeight(cursor);
        }
        cursor.close();
        return result;
    }
    public Weight getLastWeight(){
        Weight result = null;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + tWeight.getName() + " ORDER BY " + tWeight.time + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            result = createWeight(cursor);
        }
        cursor.close();
        return result;
    }
    private ArrayList<Weight> getWeights(String select){
        ArrayList<Weight> results = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        if(cursor.moveToFirst()){
            do{
                results.add(createWeight(cursor));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return results;
    }
    public ArrayList<Weight> getWeights(){
        return getWeights("SELECT * FROM " + tWeight.getName());
    }
    public ArrayList<Weight> getWeightsDescending(){
        return getWeights("SELECT * FROM " + tWeight.getName() + " ORDER BY " + tWeight.time + " DESC");
    }
    public void removeWeight(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tWeight.getName(), tScale.id + "=?", new String[]{ "" + id });
    }
}
