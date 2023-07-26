package com.utils

import com.badlogic.gdx.math.Matrix3
import com.badlogic.gdx.math.Vector2
import com.screens.game.board.Case
import com.screens.game.board.Pawn
import com.screens.game.board.Queue
import com.screens.game.board.Tile

/**
 * Objet pour stocker différentes valeurs.
 */
const val tileSize = 600f
const val offset = 40 * tileSize / 600
const val caseSize = (tileSize - 2 * offset) / 4
val origin = Vector2()
val portalList: Map<Color, MutableList<Case>> = Color.values().associateWith { mutableListOf() }
var isInPhaseB = false
var numberWeaponsRetrieved = 0
var numberPawnsOut = 0
val newBase = Matrix3(floatArrayOf(tileSize, -caseSize, 0f, caseSize, tileSize, 0f, 0f, 0f, 1f))
val newBaseInvert: Matrix3 = Matrix3(newBase).inv()
var queue: Queue? = null

val tileList = mutableListOf<Tile>()
val pawnList = mutableListOf<Pawn>()
