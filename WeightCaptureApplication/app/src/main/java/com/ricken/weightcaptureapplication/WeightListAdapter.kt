package com.ricken.weightcaptureapplication

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ricken.weightcaptureapplication.database.`object`.Weight
import com.ricken.weightcaptureapplication.database.DataBase

class WeightListAdapter(private val mainActivity: MainActivity, items: List<Weight?>) : ArrayAdapter<Weight?>(mainActivity, R.layout.item_weight, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolderWeight
        lateinit var view: View
        if (convertView == null) {
            holder = ViewHolderWeight()
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.item_weight, parent, false)
            holder.time = view.findViewById(R.id.item_weight_time)
            holder.weight = view.findViewById(R.id.item_weight_value)
            holder.btnDelete = view.findViewById(R.id.item_weight_delete)
            // save the holder in the tag-attribute for change texts faster
            view.tag = holder
        } else { // the view was created before with an holder as tag-attribute
            view = convertView
            holder = view.tag as ViewHolderWeight
        }
        val element: Weight? = getItem(position)
        if (element != null) { // only if an element exists at the position update texts
            val textTime = MainActivity.getReadableTime(element.time)
            val textWeight = MainActivity.getReadableWeight(element.value)
            holder.time!!.text = textTime
            holder.weight!!.text = textWeight
            holder.btnDelete!!.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(R.string.delete_weight)
                val message = "$textTime - $textWeight"
                builder.setMessage(message)
                builder.setPositiveButton(R.string.ok) { _, _ ->
                    DataBase.getInstance(context).removeWeight(element.id)
                    mainActivity.reloadWeight()
                }
                builder.setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                builder.show()
            }
        }
        return view
    }

}