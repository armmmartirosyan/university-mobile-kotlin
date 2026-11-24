package com.example.shoppingpreferences

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    companion object {
        // MainActivity puts the values with these same keys, so both
        // screens use one shared constant instead of retyping the string.
        const val EXTRA_RECORD_ID = "record_id"
        const val EXTRA_CUSTOMER_NAME =
            "com.example.shoppingpreferences.CUSTOMER_NAME"
        const val EXTRA_CUSTOMER_EMAIL =
            "com.example.shoppingpreferences.CUSTOMER_EMAIL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        dbHelper = DatabaseHelper(this)

        val nameText = findViewById<TextView>(R.id.detailsNameText)
        val emailText = findViewById<TextView>(R.id.detailsEmailText)
        val deleteButton = findViewById<Button>(R.id.deleteButton)

        // intent is the Intent that opened this screen.
        // -1L is returned if no id was passed; real SQLite ids start from 1.
        val recordId = intent.getLongExtra(EXTRA_RECORD_ID, -1L)
        val name = intent.getStringExtra(EXTRA_CUSTOMER_NAME)
        val email = intent.getStringExtra(EXTRA_CUSTOMER_EMAIL)

        nameText.text = "${getString(R.string.details_name_label)} $name"
        emailText.text = "${getString(R.string.details_email_label)} $email"

        deleteButton.setOnClickListener {
            if (recordId != -1L) {
                dbHelper.deleteCustomer(recordId)
                Toast.makeText(
                    this,
                    getString(R.string.customer_deleted),
                    Toast.LENGTH_SHORT
                ).show()
            }
            // Close this screen and go back to MainActivity;
            // its onResume() reloads the list from SQLite.
            finish()
        }
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
