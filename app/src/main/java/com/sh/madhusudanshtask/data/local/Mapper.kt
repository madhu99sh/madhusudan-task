package com.sh.madhusudanshtask.data.local

import com.sh.madhusudanshtask.data.local.model.HoldingEntity
import com.sh.madhusudanshtask.data.remote.model.HoldingDto
import com.sh.madhusudanshtask.domain.model.Holding


fun HoldingDto.toEntity(): HoldingEntity {
    return HoldingEntity(
        symbol = symbol,
        quantity = quantity,
        ltp = ltp,
        avgPrice = avgPrice,
        close = close
    )
}

fun HoldingDto.toModel(): Holding {
    return Holding(
        symbol = symbol,
        quantity = quantity,
        ltp = ltp,
        avgPrice = avgPrice,
        close = close
    )
}

fun HoldingEntity.toModel(): Holding {
    return Holding(
        symbol = symbol,
        quantity = quantity,
        ltp = ltp,
        avgPrice = avgPrice,
        close = close
    )
}