package edu.washington.lmburu.tipcalculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.text.InputFilter
import android.view.View
import android.widget.AdapterView


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//      define prefix "$"
        amountBox.setText("$")
        Selection.setSelection(amountBox.text, amountBox.text.toString().length)

        amountBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //set prefix "$"
                if (!s.toString().startsWith("$")) {
                    amountBox.setText("$")
                    Selection.setSelection(amountBox.text, amountBox.text.toString().length)
                }

//              retrieve and validate user input. If incorrect format is entered alert user. Set conditions to enable
//              Tip button
                val amountVal = amountBox.text.toString()
                var decimalCount = 0
                if (amountVal.contains(".")) decimalCount = amountVal.split(".").last().length
                amountBox.error = if (decimalCount > 2) "acceptable currency format: $00.00" else null
                when {
                    amountVal == "$" -> tipButton.isClickable = false
                    amountVal.contains(".") && decimalCount == 0 -> tipButton.isClickable = false
                    amountVal.contains(".") && decimalCount == 1 -> tipButton.isClickable = false
                    decimalCount > 2 -> tipButton.isClickable = false
                    else -> tipButton.isClickable = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, x: Int, y: Int, z: Int) {}

            override fun onTextChanged(s: CharSequence?, x: Int, y: Int, z: Int) {}
        })

//       create and populate spinner
        ArrayAdapter.createFromResource(
            this@MainActivity,
            R.array.tipOptions,
            android.R.layout.simple_spinner_item
        )
            .also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter}

//        retrieve selection from spinner
        var item = 0
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                item = parent?.getItemAtPosition(position).toString().split("%").first().toInt()
            }
        }

//        Take the following actions once TIP button is clicked.
        tipButton.setOnClickListener {
            val pennies = calculatePennies(amountBox.text.toString())
            var percent: Double =  item  / 100.0
            val tipAmount = String.format("%.2f", (pennies * percent) / 100).toDouble()
            Toast.makeText(this@MainActivity, "$$tipAmount", Toast.LENGTH_LONG).show()
        }
    }

    //    function converts user input to currency in pennies.
    private fun calculatePennies(s: String): Int {
        var dollar = 0
        var cent = 0
        if(s.contains(".")) {
            try {
                dollar = s.split("$", ".").get(1).toInt()
                cent = s.split("$", ".").last().toInt()
            } catch (nfe: NumberFormatException) {
                // not a valid int. No dollar amount provided.
                if (s.split("$", ".").get(1).isNullOrBlank()) {
                    cent = s.split("$", ".").last().toInt()
                } else if (s.split("$", ".").last().isNullOrBlank()) {
                    cent = 0
                }
            }
        } else{
            try {
                dollar = s.split("$").last().toInt()
            } catch (nfe: java.lang.NumberFormatException   ){
                //no valid dollar amount
            }
        }
        return dollar * 100 + cent
    }
}





