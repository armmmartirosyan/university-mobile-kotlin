package com.example.shoppingpreferences

data class Customer(
    val name: String,
    val email: String
) {
    // ArrayAdapter shows each item using toString(), so the ListView row
    // shows only the customer's name instead of "Customer(name=..., email=...)".
    override fun toString(): String {
        return name
    }
}
