package com.example.finora.ui

import retrofit2.Call
import retrofit2.http.GET

interface DashboardApi {

    @GET("api/v1/dashboard")
    fun getDashboard(): Call<DashboardDto>
}