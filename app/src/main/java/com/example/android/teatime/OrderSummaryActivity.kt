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
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_order_summary.*
import java.text.NumberFormat

class OrderSummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)
        val menuToolbar = order_summary_toolbar as Toolbar
        setSupportActionBar(menuToolbar)
        supportActionBar!!.title = getString(R.string.order_summary_title)
        val intent = intent
        val teaName = intent.getStringExtra(OrderActivity.EXTRA_TEA_NAME)
        val price = intent.getIntExtra(OrderActivity.EXTRA_TOTAL_PRICE, 0)
        val size = intent.getStringExtra(OrderActivity.EXTRA_SIZE)
        val milkType = intent.getStringExtra(OrderActivity.EXTRA_MILK_TYPE)
        val sugarType = intent.getStringExtra(OrderActivity.EXTRA_SUGAR_TYPE)
        val quantity = intent.getIntExtra(OrderActivity.EXTRA_QUANTITY, 0)
        displayOrderSummary(teaName, price, size, milkType, sugarType, quantity)
    }

    /**
     * Create summary of the order.
     *
     * @param teaName   type of tea
     * @param quantity  quantity ordered
     * @param price     price of the order
     * @param milkType  type of milk to add
     * @param sugarType amount of sugar to add
     */
    private fun displayOrderSummary(teaName: String, price: Int, size: String, milkType: String,
                                    sugarType: String, quantity: Int) {

        // Set tea name in order summary
        val teaNameTextView = summary_tea_name as TextView
        teaNameTextView.text = teaName

        // Set quantity in order summary
        val quantityTextView = summary_quantity as TextView
        quantityTextView.text = quantity.toString()

        // Set tea size in order summary
        val sizeTextView = summary_tea_size as TextView
        sizeTextView.text = size

        // Set milk type in order summary
        val milkTextView = summary_milk_type as TextView
        milkTextView.text = milkType

        // Set sugar amount in order summary
        val sugarTextView = summary_sugar_amount as TextView
        sugarTextView.text = sugarType

        // Set total price in order summary
        val priceTextView = summary_total_price as TextView
        val convertPrice = NumberFormat.getCurrencyInstance().format(price.toLong())
        priceTextView.text = convertPrice
    }

    /**
     * This method is called when the Send Email button is clicked and sends a copy of the order
     * summary to the inputted email address.
     */
    fun sendEmail(view: View?) {
        val emailMessage = getString(R.string.email_message)

        // Use an intent to launch an email app.
        // Send the order summary in the email body.
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.order_summary_email_subject))
        intent.putExtra(Intent.EXTRA_TEXT, emailMessage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}