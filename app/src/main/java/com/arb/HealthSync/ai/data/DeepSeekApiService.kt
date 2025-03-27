package com.arb.HealthSync.ai.data

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Response

interface DeepSeekApiService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer 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"
    )
    @POST("chat/completions")
    suspend fun getChatResponse(@Body request: ChatRequest): Response<ChatResponse>
}
