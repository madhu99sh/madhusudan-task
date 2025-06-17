package com.sh.madhusudanshtask.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val data: T
)

@Serializable
data class UserHoldingResponse(
    val userHolding: List<HoldingDto>
)
