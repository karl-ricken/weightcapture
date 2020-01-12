package com.ricken.weightcaptureapplication

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ricken.weightcaptureapplication.database.DataBase
import com.ricken.weightcaptureapplication.database.`object`.Scale

class ScaleActivity : AppCompatActivity(), View.OnClickListener, DialogInterface.OnClickListener {
    private var input: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)
        findViewById<View>(R.id.scale_button_new).setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        val layoutButtons = findViewById<View>(R.id.scale_layout_buttons) as LinearLayout
        layoutButtons.removeAllViews()
        val scales = DataBase.getInstance(this).getScales()
        for (scale in scales) {
            val view = View.inflate(this, R.layout.scaleview, null)
            val button = view.findViewById<Button>(R.id.scale_view_delete)
            button.setOnClickListener {
                DataBase.getInstance(applicationContext).removeScale(scale.id)
                finish()
                startActivity(intent)
            }
            val label = view.findViewById<TextView>(R.id.scale_view_text)
            label.text = scale.name
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.scale_button_new -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.new_scale)
                input = EditText(this)
                input!!.inputType = InputType.TYPE_CLASS_TEXT
                builder.setView(input)
                builder.setPositiveButton(R.string.ok, this)
                builder.setNegativeButton(R.string.cancel, this)
                builder.show()
            }
        }
    }

    override fun onClick(dialog: DialogInterface, i: Int) {
        when (i) {
            DialogInterface.BUTTON_POSITIVE -> {
                val text = input?.text.toString()
                if (text.isNotEmpty()) {
                    val scale = Scale(text)
                    DataBase.getInstance(applicationContext).addScale(scale)
                }
            }
            else -> dialog.cancel()
        }
    }
}