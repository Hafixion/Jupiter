package com.neptune.jupiter.api.utils.time

import kotlin.time.*

fun now(): Long = System.currentTimeMillis()
fun nowNano(): Long = System.nanoTime()

@ExperimentalTime
val Long.ticks: Duration get() = toDouble().ticks

@ExperimentalTime
val Int.ticks: Duration get() = toDouble().ticks

@ExperimentalTime
val Double.ticks: Duration get() = Duration.milliseconds(tickToMilliseconds(this))

@ExperimentalTime
val Duration.inTicks: Double get() = millisecondsToTick(toDouble(DurationUnit.MILLISECONDS))

@ExperimentalTime
fun Duration.toLongTicks(): Long = inTicks.toLong()

private fun tickToMilliseconds(value: Double): Double = value * 50.0
private fun millisecondsToTick(value: Double): Double = value / 50.0