package com.example.dicodingevent.data.retrofit

import com.example.dicodingevent.data.response.DetailResponse
import com.example.dicodingevent.data.response.EventResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("events?active=1")
    suspend fun getUpcomingEvents(): Response<EventResponse>

    @GET("events?active=0")
    suspend fun getCompletedEvents(): Response<EventResponse>

    @GET("events/{id}")
    suspend fun getEventDetails(@Path("id") id: Int): Response<DetailResponse>
}
