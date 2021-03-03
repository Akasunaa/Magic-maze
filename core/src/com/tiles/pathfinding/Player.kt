package com.tiles.pathfinding
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
class Player (
        val north: Boolean, val south: Boolean,
        val east: Boolean, val west: Boolean,
        val shortcutTaker: Boolean, val escalatorTaker: Boolean) {

    fun rotate(i: Int):Player =
            if (i == 0) this
            else if (i == 1) Player(south,north,east,west, shortcutTaker, escalatorTaker)
            else Player(south,north,east,west, shortcutTaker, escalatorTaker).rotate(((i-1)%4 + 4)%4)
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
}