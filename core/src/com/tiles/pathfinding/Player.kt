package com.tiles.pathfinding
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
class Player (
        val north: Boolean, val south: Boolean,
        val east: Boolean, val west: Boolean,
        val shortcutTaker: Boolean, val escalatorTaker: Boolean) {

    fun copyEscalator()= Player(north, south, east, west, shortcutTaker, false)

    fun copyShortcut()= Player(north, south, east, west, false, escalatorTaker)

    // Deprecated, j'ai réparé le pathfinding.
}