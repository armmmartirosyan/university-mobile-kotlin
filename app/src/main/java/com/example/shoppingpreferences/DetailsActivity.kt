package com.example.shoppingpreferences

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    companion object {
        // MainActivity puts the values with these same keys, so both
        // screens use one shared constant instead of retyping the string.
        const val EXTRA_CUSTOMER_NAME =
            "com.example.shoppingpreferences.CUSTOMER_NAME"
        const val EXTRA_CUSTOMER_EMAIL =
            "com.example.shoppingpreferences.CUSTOMER_EMAIL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val nameText = findViewById<TextView>(R.id.detailsNameText)
        val emailText = findViewById<TextView>(R.id.detailsEmailText)

        // intent is the Intent that opened this screen.
        val name = intent.getStringExtra(EXTRA_CUSTOMER_NAME)
        val email = intent.getStringExtra(EXTRA_CUSTOMER_EMAIL)

        nameText.text = "${getString(R.string.details_name_label)} $name"
        emailText.text = "${getString(R.string.details_email_label)} $email"
    }
}
