package com.tiles.pathfinding

import com.utils.Functions.modulo
import kotlinx.serialization.Serializable

@Serializable
class Player(
        val north: Boolean, val south: Boolean,
        val east: Boolean, val west: Boolean,
        val shortcutTaker: Boolean, val escalatorTaker: Boolean) {

    fun rotate(i: Int): Player =
            when (i) {
                0 -> this
                else -> Player(south, north, west, east, shortcutTaker, escalatorTaker).rotate(modulo(i - 1, 4))
            }

    /*
    WHAT
    THE
    FUCK
    THIS SHOULD NOT WORK LIKE THIS
    LET IT BE CLEAR: THIS RIGHT HERE IS WITCHCRAFT
    EVIL WITCHCRAFT
    DO NOT TOUCH THIS
    DO NOT REPRODUCE
    THIS CODE IS EVIL
     */

    /*
    Edit: This isn't witchcraft anymore
    At least I don't think it is ?
    It's just a classic inversion
    Thanks Mr. Simatic for the tip of using actual named constants
     */
}