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
val tileSize = 600f
val offset = 40 * tileSize / 600
val caseSize = (tileSize - 2 * offset) / 4
val origin = Vector2()
val portalList: Map<Color, MutableList<Case>> = Color.values().associateWith { mutableListOf() }
var lastExploredCase: Case? = null
var isInPhaseB = false
var numberWeaponsRetrieved = 0
var numberPawnsOut = 0
var newBase = Matrix3(floatArrayOf(tileSize, -caseSize, 0f, caseSize, tileSize, 0f, 0f, 0f, 1f))
var newBaseInvert: Matrix3 = Matrix3(newBase).inv()
var queue: Queue? = null

val tileList = mutableListOf<Tile>()
val pawnList = mutableListOf<Pawn>()
