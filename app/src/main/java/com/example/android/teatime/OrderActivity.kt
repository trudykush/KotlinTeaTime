/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.example.android.teatime

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.android.teatime.OrderActivity
import kotlinx.android.synthetic.main.activity_order.*
import java.text.NumberFormat

class OrderActivity : AppCompatActivity() {
    private var mQuantity = 0
    private var mTotalPrice = 0
    private var mMilkType: String? = null
    private var mSugarType: String? = null
    private var mTeaName = ""
    private var mSize: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        val menuToolbar = order_toolbar as Toolbar
        setSupportActionBar(menuToolbar)
        supportActionBar!!.title = getString(R.string.order_title)

        // Set header name and image depending on which item was clicked in the gridView
        val intent = intent
        mTeaName = intent.getStringExtra(MenuActivity.EXTRA_TEA_NAME)
        val teaNameTextView = tea_name_text_view as TextView
        teaNameTextView.text = mTeaName

        // Set cost default to $0.00
        val costTextView = findViewById<View>(
                R.id.cost_text_view) as TextView
        costTextView.text = getString(R.string.initial_cost)
        setupSizeSpinner()
        setupMilkSpinner()
        setupSugarSpinner()
    }

    /**
     * Sets up the dropdown spinner for user to select tea mSize
     */
    private fun setupSizeSpinner() {
        val mSizeSpinner = tea_size_spinner as Spinner

        // Create an ArrayAdapter using the string array and a default mSizeSpinner layout
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.tea_size_array, android.R.layout.simple_spinner_item)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the mSizeSpinner
        mSizeSpinner.adapter = adapter

        // Set the integer mSelected to the constant values
        mSizeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> mSize = getString(R.string.tea_size_small)
                    1 -> mSize = getString(R.string.tea_size_medium)
                    2 -> mSize = getString(R.string.tea_size_large)
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            override fun onNothingSelected(parent: AdapterView<*>?) {
                mSize = getString(R.string.tea_size_small)
            }
        }
    }

    /**
     * Sets up the dropdown spinner for user to select milk type
     */
    private fun setupMilkSpinner() {
        val mSizeSpinner = milk_spinner as Spinner

        // Create an ArrayAdapter using the string array and a default mSizeSpinner layout
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.milk_array, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the mSizeSpinner
        mSizeSpinner.adapter = adapter

        // Set the integer mSelected to the constant values
        mSizeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> mMilkType = getString(R.string.milk_type_none)
                    1 -> mMilkType = getString(R.string.milk_type_nonfat)
                    2 -> mMilkType = getString(R.string.milk_type_1_percent)
                    3 -> mMilkType = getString(R.string.milk_type_2_percent)
                    4 -> mMilkType = getString(R.string.milk_type_whole)
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            override fun onNothingSelected(parent: AdapterView<*>?) {
                mMilkType = getString(R.string.milk_type_none)
            }
        }
    }

    /**
     * Setup the dropdown spinner for user to select amount of sugar
     */
    private fun setupSugarSpinner() {
        val mSizeSpinner = sugar_spinner as Spinner

        // Create an ArrayAdapter using the string array and a default mSizeSpinner layout
        val adapter = ArrayAdapter.createFromResource(this,
                R.array.sugar_array, android.R.layout.simple_spinner_item)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the mSizeSpinner
        mSizeSpinner.adapter = adapter

        // Set the integer mSelected to the constant values
        mSizeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> mSugarType = getString(R.string.sweet_type_0)
                    1 -> mSugarType = getString(R.string.sweet_type_25)
                    2 -> mSugarType = getString(R.string.sweet_type_50)
                    3 -> mSugarType = getString(R.string.sweet_type_75)
                    4 -> mSugarType = getString(R.string.sweet_type_100)
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            override fun onNothingSelected(parent: AdapterView<*>?) {
                mSugarType = getString(R.string.sweet_type_100)
            }
        }
    }

    /**
     * Increments the quantity and recalculates the price
     */
    fun increment(view: View?) {
        mQuantity = mQuantity + 1
        displayQuantity(mQuantity)
        mTotalPrice = calculatePrice()
        displayCost(mTotalPrice)
    }

    /**
     * Decrements the quantity and recalculates the price
     */
    fun decrement(view: View?) {
        if (mQuantity > 0) {
            mQuantity = mQuantity - 1
            displayQuantity(mQuantity)
            mTotalPrice = calculatePrice()
            displayCost(mTotalPrice)
        }
    }

    /**
     * Calculates the TotalPrice of the order.
     *
     * @return total mTotalPrice
     */
    private fun calculatePrice(): Int {

        // Calculate the total order mTotalPrice by multiplying by the mQuantity
        when (mSize) {
            TEA_SIZE_SMALL -> mTotalPrice = mQuantity * SMALL_PRICE
            TEA_SIZE_MEDIUM -> mTotalPrice = mQuantity * MEDIUM_PRICE
            TEA_SIZE_LARGE -> mTotalPrice = mQuantity * LARGE_PRICE
        }
        return mTotalPrice
    }

    /**
     * Displays the given mQuantity value on the screen then
     * calculates and displays the cost
     */
    private fun displayQuantity(numberOfTeas: Int) {
        val quantityTextView = findViewById<View>(R.id.quantity_text_view) as TextView
        quantityTextView.text = numberOfTeas.toString()
    }

    private fun displayCost(totalPrice: Int) {
        val costTextView = findViewById<View>(
                R.id.cost_text_view) as TextView
        val convertPrice = NumberFormat.getCurrencyInstance().format(totalPrice.toLong())
        costTextView.text = convertPrice
    }

    /**
     * This method is called when the "Brew Tea" button is clicked
     * and a new intent opens the the [OrderSummaryActivity]
     */
    fun brewTea(view: View?) {
        // Create a new intent to open the {@link OrderSummaryActivity}
        val intent = Intent(this@OrderActivity, OrderSummaryActivity::class.java)
        intent.putExtra(EXTRA_TOTAL_PRICE, mTotalPrice)
        intent.putExtra(EXTRA_TEA_NAME, mTeaName)
        intent.putExtra(EXTRA_SIZE, mSize)
        intent.putExtra(EXTRA_MILK_TYPE, mMilkType)
        intent.putExtra(EXTRA_SUGAR_TYPE, mSugarType)
        intent.putExtra(EXTRA_QUANTITY, mQuantity)
        startActivity(intent)
    }

    companion object {
        private const val SMALL_PRICE = 5
        private const val MEDIUM_PRICE = 6
        private const val LARGE_PRICE = 7
        private const val TEA_SIZE_SMALL = "Small ($5/cup)"
        private const val TEA_SIZE_MEDIUM = "Medium ($6/cup)"
        private const val TEA_SIZE_LARGE = "Large ($7/cup)"
        const val EXTRA_TOTAL_PRICE = "com.example.android.teatime.EXTRA_TOTAL_PRICE"
        const val EXTRA_TEA_NAME = "com.example.android.teatime.EXTRA_TEA_NAME"
        const val EXTRA_SIZE = "com.example.android.teatime.EXTRA_SIZE"
        const val EXTRA_MILK_TYPE = "com.example.android.teatime.EXTRA_MILK_TYPE"
        const val EXTRA_SUGAR_TYPE = "com.example.android.teatime.EXTRA_SUGAR_TYPE"
        const val EXTRA_QUANTITY = "com.example.android.teatime.EXTRA_QUANTITY"
    }
}