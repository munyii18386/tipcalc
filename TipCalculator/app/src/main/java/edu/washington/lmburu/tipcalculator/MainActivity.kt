package edu.washington.lmburu.tipcalculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.*


class MainActivity : AppCompatActivity() {
    private val TAG  = "MainActivity"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            amountBox.setText("$")
            Selection.setSelection(amountBox.text, amountBox.text.toString().length)


        amountBox.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(!s.toString().startsWith("$")){
                    amountBox.setText("$");
                    Selection.setSelection(amountBox.text, amountBox.text.toString().length)

                }
                val amountVal = amountBox.text.toString()

                var decimalCount = 0
                if(amountVal.contains(".")) decimalCount = decimalCounter(amountVal)

                amountBox.error = if(decimalCount > 2) "acceptable currency format: $00.00" else null

                Log.i(TAG, "amount entered is:  ${amountVal}")
                Log.i(TAG, "number of decimal is $decimalCount")

                when{
                    amountVal == ""  -> button_id.isClickable = false
                    amountVal == "$" -> button_id.isClickable = false
                    amountVal.contains(".") && decimalCount == 0 -> button_id.isClickable = false
                    decimalCount > 2  -> button_id.isClickable = false
                    else -> button_id.isClickable = true
                }

//                var result = calculateTip(amountVal)
                val num = amountVal.split("$", ".")
                val a = num[1].toIntOrNull()
                val b = num[2].toIntOrNull()
                var total = 0




                Log.i(TAG, "sliced value ")




            }

            override fun beforeTextChanged(s: CharSequence?, x: Int, y: Int, z: Int) {}

            override fun onTextChanged(s: CharSequence?, x: Int, y: Int, z: Int) {}
        })



    }

    fun decimalCounter(s: String): Int{
        return s.split(".").last().length
    }

//    fun calculateTip(s: String){
//        val num = s.split("$", ".")
//
//       var total =  num[2]
//        Log.i(TAG, "amount in pennies $total")
//
//
//    }








}


