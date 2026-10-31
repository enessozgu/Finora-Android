package com.example.finora.ui

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DashboardApi {

    // Dashboard verilerini getir
    @GET("api/v1/dashboard")
    fun getDashboard(): Call<DashboardDto>

    // Transfer isteği gönder
    @POST("api/v1/transfers")
    fun createTransfer(
        @Body request: TransferRequestDto
    ): Call<TransferResponseDto>
}