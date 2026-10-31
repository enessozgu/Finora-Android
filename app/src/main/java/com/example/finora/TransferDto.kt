package com.example.finora.ui

import java.math.BigDecimal

data class TransferRequestDto(
    val iban: String,
    val recipientName: String,
    val amount: BigDecimal,
    val description: String?
)

data class TransferResponseDto(
    val status: String,
    val message: String,
    val transferId: String? = null,
    val recipientName: String? = null,
    val iban: String? = null,
    val amount: BigDecimal? = null,
    val description: String? = null,
    val createdAt: String? = null
)