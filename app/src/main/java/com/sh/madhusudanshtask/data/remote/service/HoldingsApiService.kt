package com.sh.madhusudanshtask.data.remote.service

import com.sh.madhusudanshtask.data.remote.model.ApiResponse
import com.sh.madhusudanshtask.data.remote.model.UserHoldingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.headers

interface HoldingsApiService {
    suspend fun fetchHoldings(): ApiResponse<UserHoldingResponse>
}

class HoldingsApiServiceImpl(
    private val client: HttpClient, private val baseUrl: String
) : HoldingsApiService {

    override suspend fun fetchHoldings(): ApiResponse<UserHoldingResponse> {
        return client.get("https://35dee773a9ec441e9f38d5fc249406ce.api.mockbin.io") {
            headers {
                append(
                    "Cache-Control", "public, max-age=60, must-revalidate"
                )
            }
        }.body()
    }
}