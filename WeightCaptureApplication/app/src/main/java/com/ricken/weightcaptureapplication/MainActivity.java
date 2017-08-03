package com.ricken.weightcaptureapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricken.weightcaptureapplication.database.DataBase;
import com.ricken.weightcaptureapplication.database.object.Scale;
import com.ricken.weightcaptureapplication.database.object.Weight;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Weight> weights;
    private WeightListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weights = new ArrayList<>();
        adapter = new WeightListAdapter(this, weights);
        ListView listView = (ListView) findViewById(R.id.main_list_weights);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        reloadScales();
        reloadWeight();
    }

    public static String getReadableTime(long millis){
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }
    public static String getReadableWeight(double weight){
        return String.format(Locale.getDefault(), "%.1f", weight);
    }
    public void reloadWeight(){
        reloadLastWeight();
        reloadWeightList();
    }
    private void reloadWeightList(){
        weights.clear();
        weights.addAll(DataBase.getInstance(this).getWeightsDescending());
        adapter.notifyDataSetChanged();
    }
    private void reloadLastWeight(){
        TextView tv_value = (TextView) findViewById(R.id.main_last_weight_value);
        TextView tv_time = (TextView) findViewById(R.id.main_last_weight_time);
        Weight weight = DataBase.getInstance(this).getLastWeight();
        if(weight != null){
            tv_value.setText(getReadableWeight(weight.getValue()));
            tv_time.setText(getReadableTime(weight.getTime()));
        } else {
            tv_value.setText("");
            tv_time.setText("");
        }
    }
    private void reloadScales(){
        LinearLayout layout_buttons = (LinearLayout) findViewById(R.id.main_layout_button);
        ArrayList<Scale> scales = DataBase.getInstance(this).getScales();
        for(final Scale scale : scales){
            Button button = new Button(this);
            button.setText(scale.getName());
            layout_buttons.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText input = (EditText) findViewById(R.id.main_input);
                    String text = input.getText().toString();
                    double value = text.isEmpty() ? 0 : Double.parseDouble(text);
                    if(value > 0){
                        long millis = System.currentTimeMillis();
                        Weight weight = new Weight();
                        weight.setScale(scale.getId());
                        weight.setTime(millis);
                        weight.setValue(value);
                        DataBase.getInstance(getApplicationContext()).addWeight(weight);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), R.string.added_element, Toast.LENGTH_LONG).show();
                            }
                        });
                        reloadWeight();
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_main_scales:
                Intent intent = new Intent(this, ScaleActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
