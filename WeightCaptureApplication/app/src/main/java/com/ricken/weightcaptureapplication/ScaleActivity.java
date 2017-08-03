package com.ricken.weightcaptureapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ricken.weightcaptureapplication.database.DataBase;
import com.ricken.weightcaptureapplication.database.object.Scale;

import java.util.ArrayList;

public class ScaleActivity extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnClickListener{
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale);

        findViewById(R.id.scale_button_new).setOnClickListener(this);


    }

    @Override
    protected void onResume(){
        super.onResume();

        LinearLayout layout_buttons = (LinearLayout) findViewById(R.id.scale_layout_buttons);
        layout_buttons.removeAllViews();
        ArrayList<Scale> scales = DataBase.getInstance(this).getScales();
        for(final Scale scale : scales){
            View view = View.inflate(this, R.layout.scaleview, null);
            Button button = view.findViewById(R.id.scale_view_delete);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBase.getInstance(getApplicationContext()).removeScale(scale.getId());
                    finish();
                    startActivity(getIntent());
                }
            });
            TextView label = view.findViewById(R.id.scale_view_text);
            label.setText(scale.getName());
        }
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.scale_button_new:{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.new_scale);
                input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton(R.string.ok, this);
                builder.setNegativeButton(R.string.cancel, this);

                builder.show();
                break;
            }
            default: break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int i) {
        switch(i){
            case DialogInterface.BUTTON_POSITIVE:{
                String text = input.getText().toString();
                if(!text.isEmpty()){
                    Scale scale = new Scale(text);
                    DataBase.getInstance(getApplicationContext()).addScale(scale);
                }
                break;
            }
            default:{
                dialog.cancel();
            }
        }
    }
}
