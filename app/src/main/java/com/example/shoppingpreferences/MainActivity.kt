package com.example.shoppingpreferences

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    // lateinit: these are created in onCreate(), but used later in loadCustomers().
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: ArrayAdapter<Customer>

    // The adapter shows this list. It is only a copy of what is in SQLite and
    // is refilled from the database in loadCustomers().
    private val customers = mutableListOf<Customer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val discountCheck = findViewById<CheckBox>(R.id.discountCheck)
        val newProductCheck = findViewById<CheckBox>(R.id.newProductCheck)
        val deliveryToggle = findViewById<ToggleButton>(R.id.deliveryToggle)
        val cardRadio = findViewById<RadioButton>(R.id.cardRadio)
        val saveButton = findViewById<Button>(R.id.saveButton)

        discountCheck.isChecked = true
        newProductCheck.isChecked = true

        // checked = true -> textOn -> «ԱՌԱՔՈՒՄ»
        deliveryToggle.isChecked = true

        // Card is selected; the RadioGroup keeps the other two unselected.
        cardRadio.isChecked = true

        saveButton.visibility = View.VISIBLE
        saveButton.isEnabled = true

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val resultText = findViewById<TextView>(R.id.resultText)
        val helpButton = findViewById<Button>(R.id.helpButton)
        val listView = findViewById<ListView>(R.id.customersListView)

        // Creating the helper does not open or create the database yet;
        // that happens on the first readableDatabase / writableDatabase call.
        dbHelper = DatabaseHelper(this)

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            customers
        )
        listView.adapter = adapter

        // The list is loaded in onResume(), which always runs right after onCreate().

        saveButton.setOnClickListener {
            // Read the fields here, at click time, to get what is typed right now.
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                resultText.text = getString(R.string.fill_name_and_email)
                // Stops only this click handler, not the whole onCreate().
                return@setOnClickListener
            }

            val rowId = dbHelper.insertCustomer(name, email)

            if (rowId != -1L) {
                resultText.text = "${getString(R.string.buyer_label)} $name, " +
                    "${getString(R.string.email_label)} $email"
                nameInput.text.clear()
                emailInput.text.clear()
                loadCustomers()
            } else {
                resultText.text = getString(R.string.save_error)
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            // position is the row index in the list (0, 1, 2...),
            // and the same index in the customers list behind the adapter.
            val selectedCustomer = customers[position]

            val intent = Intent(this, DetailsActivity::class.java)
            // Pass the SQLite id (not position), so DetailsActivity deletes the right row.
            intent.putExtra(
                DetailsActivity.EXTRA_RECORD_ID,
                selectedCustomer.id
            )
            intent.putExtra(
                DetailsActivity.EXTRA_CUSTOMER_NAME,
                selectedCustomer.name
            )
            intent.putExtra(
                DetailsActivity.EXTRA_CUSTOMER_EMAIL,
                selectedCustomer.email
            )
            startActivity(intent)
        }

        helpButton.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }
    }

    // Runs on first open and also every time we come back from DetailsActivity,
    // so a customer deleted there disappears from the list.
    override fun onResume() {
        super.onResume()
        loadCustomers()
    }

    private fun loadCustomers() {
        // Without clear() every reload would add the same customers again.
        customers.clear()

        val cursor: Cursor = dbHelper.getAllCustomers()
        // use { } closes the cursor at the end, even if an exception happens.
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(
                    it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)
                )
                val name = it.getString(
                    it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)
                )
                val email = it.getString(
                    it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL)
                )
                customers.add(Customer(id, name, email))
            }
        }

        // The adapter does not query SQLite itself; we changed its list,
        // so we tell it to redraw the ListView.
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
