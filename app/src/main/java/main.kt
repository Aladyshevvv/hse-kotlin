package com.example.expensetracker

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Transaction(val amount: Int, val type: String, val category: String, val dateTime: LocalDateTime)

class TransactionHistory {
    private val transactions = mutableListOf<Transaction>()
    private val categories = mutableListOf("Food", "Transport", "Entertainment", "Other")

    fun addTransaction(transaction: Transaction) {
        transactions.add(transaction)
    }

    fun undoLastTransaction() {
        if (transactions.isNotEmpty()) {
            transactions.removeAt(transactions.size - 1)
            println("Last transaction removed.")
        } else {
            println("No transactions to undo.")
        }
    }

    fun showHistory(category: String? = null) {
        val filteredTransactions = if (category != null) {
            transactions.filter { it.category == category }
        } else {
            transactions
        }

        if (filteredTransactions.isEmpty()) {
            println("No transactions found.")
            return
        }

        filteredTransactions.forEach {
            println("Amount: ${it.amount}, Type: ${it.type}, Category: ${it.category}, Date: ${it.dateTime.format(DateTimeFormatter.ISO_DATE)}")
        }
    }

    fun getCurrentBalance(): Int {
        return transactions.sumOf { if (it.type == "Income") it.amount else -it.amount }
    }

    fun addCategory(category: String) {
        if (!categories.contains(category)) {
            categories.add(category)
            println("Category $category added.")
        } else {
            println("Category already exists.")
        }
    }

    fun listCategories() {
        println("Available categories:")
        categories.forEach { println(it) }
    }

    fun isValidCategory(category: String): Boolean {
        return categories.contains(category)
    }
}

fun main() {
    val history = TransactionHistory()
    var running = true

    while (running) {
        println("Balance: ${history.getCurrentBalance()}")
        println(
            """
            Menu:
            1 - Add expense
            2 - Add income
            3 - Undo last transaction
            4 - Show transaction history
            5 - Add custom category
            0 - Exit
            """.trimIndent()
        )
        when (val input = readLine()) {
            "0" -> running = false
            "1", "2" -> handleTransaction(input == "1", history)
            "3" -> history.undoLastTransaction()
            "4" -> history.showHistory()
            "5" -> addCategory(history)
            else -> println("Unknown command")
        }
        println("*** *** ***\n")
    }
}

fun handleTransaction(isExpense: Boolean, history: TransactionHistory) {
    println("Enter amount:")
    val amount = readLine()?.toIntOrNull()
    if (amount == null || amount <= 0) {
        println("Invalid amount.")
        return
    }

    history.listCategories()
    println("Select a category (type the category name):")
    val category = readLine()
    if (category == null || !history.isValidCategory(category)) {
        println("Invalid category.")
        return
    }

    val type = if (isExpense) "Expense" else "Income"
    history.addTransaction(Transaction(amount, type, category, LocalDateTime.now()))
    println("$type of $amount added to category $category.")
}

fun addCategory(history: TransactionHistory) {
    println("Enter new category name:")
    val category = readLine()
    if (category.isNullOrBlank()) {
        println("Invalid category name.")
        return
    }
    history.addCategory(category)
}
