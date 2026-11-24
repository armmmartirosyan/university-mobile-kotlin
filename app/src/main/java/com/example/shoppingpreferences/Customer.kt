package com.example.shoppingpreferences

data class Customer(
    // The real SQLite id of the row. It is not the ListView position:
    // after deletes the ids can be 2, 5, 8 while positions are 0, 1, 2.
    val id: Long,
    val name: String,
    val email: String
) {
    // ArrayAdapter shows each item using toString(), so the ListView row
    // shows only the customer's name instead of "Customer(id=..., name=...)".
    override fun toString(): String {
        return name
    }
}
