package com.ricken.weightcaptureapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ricken.weightcaptureapplication.database.DataBase;
import com.ricken.weightcaptureapplication.database.object.Weight;

import java.util.List;

public class WeightListAdapter extends ArrayAdapter<Weight>{
    private final MainActivity mainActivity;
    public WeightListAdapter(MainActivity mainActivity, List<Weight> items){
        super(mainActivity, R.layout.item_weight, items);
        this.mainActivity = mainActivity;
    }
    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        ViewHolderWeight holder;
        View view = convertView;
        if(view == null){
            holder = new ViewHolderWeight();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.item_weight, parent, false);

            holder.time = view.findViewById(R.id.item_weight_time);
            holder.weight = view.findViewById(R.id.item_weight_value);
            holder.btn_delete = view.findViewById(R.id.item_weight_delete);

            // save the holder in the tag-attribute for change texts faster
            view.setTag(holder);
        } else {
            // the view was created before with an holder as tag-attribute
            holder = (ViewHolderWeight) view.getTag();
        }
        final Weight element = getItem(position);
        if(element!=null){ // only if an element exists at the position update texts
            final String textTime = MainActivity.getReadableTime(element.getTime());
            final String textWeight = MainActivity.getReadableWeight(element.getValue());
            holder.time.setText(textTime);
            holder.weight.setText(textWeight);
            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.delete_weight);
                    String message = textTime + " - " + textWeight;
                    builder.setMessage(message);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            DataBase.getInstance(getContext()).removeWeight(element.getId());
                            mainActivity.reloadWeight();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });
        }
        return view;
    }
}
