package com.sh.madhusudanshtask.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class HoldingDto(
    val symbol: String, val quantity: Int, val ltp: Double, val avgPrice: Double, val close: Double
)