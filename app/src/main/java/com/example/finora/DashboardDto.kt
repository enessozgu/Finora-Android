package com.example.finora.ui

data class DashboardDto(
    val balance: Double,
    val currency: String,
    val accountName: String,
    val transactions: List<TransactionDto>
)

data class TransactionDto(
    val id: String,
    val title: String,
    val description: String,
    val amount: Double,
    val type: String
)