package com.screens.game.board

import com.utils.Color
import java.lang.Exception

sealed interface CaseType {
    enum class BasicCase : CaseType {
        INACCESSIBLE, NONE, ENTRANCE, HOURGLASS, CRYSTAL_BALL, CAMERA;
    }

    sealed class ColoredCase(val color: Color) : CaseType {
        class Exit(color: Color) : ColoredCase(color)
        class Portal(color: Color) : ColoredCase(color)
        class Weapon(color: Color) : ColoredCase(color)
        class FinalExit(color: Color) : ColoredCase(color)

    }
}


// On a un table de 4x4 pour indiquer ce que contiennent les cases
// Puis un tableau de 3x4 pour indiquer les murs verticaux, et un 4x3 pour les murs horizontaux
fun getTileArray(number: Int): List<List<CaseType>> =
    when (number) {
        0 -> listOf(
            listOf(
                CaseType.BasicCase.HOURGLASS,
                CaseType.BasicCase.NONE,
                CaseType.ColoredCase.Exit(Color.ORANGE),
                CaseType.ColoredCase.Portal(Color.PURPLE)
            ),
            listOf(
                CaseType.ColoredCase.Exit(Color.PURPLE),
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.ColoredCase.Portal(Color.YELLOW)
            ),
            listOf(
                CaseType.ColoredCase.Portal(Color.ORANGE),
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.ColoredCase.Exit(Color.GREEN)
            ),
            listOf(
                CaseType.ColoredCase.Portal(Color.GREEN),
                CaseType.ColoredCase.Exit(Color.YELLOW),
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.INACCESSIBLE
            )
        )

        1 -> listOf(
            listOf(
                CaseType.BasicCase.HOURGLASS,
                CaseType.BasicCase.NONE,
                CaseType.ColoredCase.Exit(Color.GREEN),
                CaseType.BasicCase.NONE
            ),
            listOf(
                CaseType.ColoredCase.Exit(Color.PURPLE),
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE
            ),
            listOf(
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.ColoredCase.Exit(Color.YELLOW)
            ),
            listOf(
                CaseType.BasicCase.NONE,
                CaseType.ColoredCase.Exit(Color.ORANGE),
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.INACCESSIBLE
            )
        )

        2 -> listOf(
            listOf(
                CaseType.ColoredCase.FinalExit(Color.PURPLE),
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.INACCESSIBLE
            ),
            listOf(
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Portal(Color.PURPLE)
            ),
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.ENTRANCE
            ),
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Exit(Color.ORANGE),
                CaseType.BasicCase.NONE,
                CaseType.ColoredCase.Portal(Color.GREEN)
            )
        )

        3 -> listOf(
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.ENTRANCE,
                CaseType.BasicCase.INACCESSIBLE
            ),
            listOf(
                CaseType.ColoredCase.Exit(Color.YELLOW),
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.HOURGLASS
            ),
            listOf(
                CaseType.ColoredCase.Exit(Color.PURPLE),
                CaseType.ColoredCase.Portal(Color.GREEN),
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE
            ),
            listOf(
                CaseType.ColoredCase.Portal(Color.ORANGE),
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE
            )
        )

        4 -> listOf(
            listOf(
                CaseType.ColoredCase.Portal(Color.ORANGE),
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.INACCESSIBLE
            ),
            listOf(
                CaseType.BasicCase.ENTRANCE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.HOURGLASS,
                CaseType.BasicCase.INACCESSIBLE
            ),
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.ColoredCase.Exit(Color.PURPLE)
            ),
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Exit(Color.GREEN),
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Portal(Color.YELLOW)
            )
        )

        5 -> listOf(
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Portal(Color.PURPLE),
                CaseType.ColoredCase.Exit(Color.YELLOW),
                CaseType.BasicCase.NONE
            ),
            listOf(
                CaseType.BasicCase.ENTRANCE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE
            ),
            listOf(
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.HOURGLASS,
                CaseType.ColoredCase.Exit(Color.ORANGE)
            ),
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Exit(Color.GREEN),
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE
            )
        )

        6 -> listOf(
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Weapon(Color.YELLOW),
                CaseType.BasicCase.INACCESSIBLE
            ),
            listOf(
                CaseType.ColoredCase.Exit(Color.GREEN),
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.INACCESSIBLE
            ),
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.ENTRANCE
            ),
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Exit(Color.ORANGE),
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Portal(Color.PURPLE)
            )
        )

        7 -> listOf(
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Portal(Color.GREEN),
                CaseType.BasicCase.INACCESSIBLE
            ),
            listOf(
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE
            ),
            listOf(
                CaseType.ColoredCase.Weapon(Color.ORANGE),
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Exit(Color.PURPLE)
            ),
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.ENTRANCE,
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Portal(Color.YELLOW)
            )
        )

        8 -> listOf(
            listOf(
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE
            ),
            listOf(
                CaseType.ColoredCase.Exit(Color.ORANGE),
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Portal(Color.YELLOW),
                CaseType.BasicCase.NONE
            ),
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Exit(Color.PURPLE)
            ),
            listOf(
                CaseType.ColoredCase.Weapon(Color.GREEN),
                CaseType.BasicCase.ENTRANCE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE
            )
        )

        9 -> listOf(
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE
            ),
            listOf(
                CaseType.BasicCase.ENTRANCE,
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.NONE
            ),
            listOf(
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.BasicCase.INACCESSIBLE,
                CaseType.ColoredCase.Portal(Color.ORANGE),
                CaseType.BasicCase.NONE
            ),
            listOf(
                CaseType.ColoredCase.Weapon(Color.PURPLE),
                CaseType.ColoredCase.Exit(Color.YELLOW),
                CaseType.BasicCase.NONE,
                CaseType.BasicCase.NONE
            )
        )

        else -> throw Exception("This tile number wasn't acceptable: $number")
    }.reversed()

fun getTileArrayWallVertical(number: Int): List<BooleanArray> = when (number) {
    0 -> listOf(
        booleanArrayOf(false, false, false),
        booleanArrayOf(false, false, false),
        booleanArrayOf(false, false, true),
        booleanArrayOf(false, false, true)
    )

    1 -> listOf(
        booleanArrayOf(true, false, false),
        booleanArrayOf(true, false, true),
        booleanArrayOf(false, false, true),
        booleanArrayOf(false, false, true)
    )

    2 -> listOf(
        booleanArrayOf(false, true, false),
        booleanArrayOf(true, false, true),
        booleanArrayOf(true, true, false),
        booleanArrayOf(true, false, false)
    )

    3 -> listOf(
        booleanArrayOf(true, false, true),
        booleanArrayOf(false, false, false),
        booleanArrayOf(false, false, true),
        booleanArrayOf(true, true, false)
    )

    4 -> listOf(
        booleanArrayOf(true, true, false),
        booleanArrayOf(false, false, true),
        booleanArrayOf(true, false, false),
        booleanArrayOf(true, true, true)
    )

    5 -> listOf(
        booleanArrayOf(true, true, false),
        booleanArrayOf(true, false, false),
        booleanArrayOf(false, false, true),
        booleanArrayOf(true, false, false)
    )

    6 -> listOf(
        booleanArrayOf(false, true, true),
        booleanArrayOf(false, false, true),
        booleanArrayOf(true, true, false),
        booleanArrayOf(true, true, true)
    )

    7 -> listOf(
        booleanArrayOf(false, true, true),
        booleanArrayOf(false, false, false),
        booleanArrayOf(true, false, true),
        booleanArrayOf(true, true, true)
    )

    8 -> listOf(
        booleanArrayOf(false, false, false),
        booleanArrayOf(true, true, true),
        booleanArrayOf(false, false, true),
        booleanArrayOf(false, false, false)
    )

    9 -> listOf(
        booleanArrayOf(true, false, false),
        booleanArrayOf(false, true, true),
        booleanArrayOf(true, true, true),
        booleanArrayOf(false, false, false)
    )

    else -> throw Exception("This tile number wasn't acceptable: $number")
}.reversed()

fun getTileArrayWallHorizontal(number: Int): List<BooleanArray> = when (number) {
    0 -> listOf(
        booleanArrayOf(true, false, false, true),
        booleanArrayOf(true, false, false, true),
        booleanArrayOf(true, false, false, true)
    )

    0 -> listOf(
        booleanArrayOf(false, false, true, false),
        booleanArrayOf(false, false, false, false),
        booleanArrayOf(false, true, true, true)
    )

    2 -> listOf(
        booleanArrayOf(false, true, false, true),
        booleanArrayOf(true, true, true, false),
        booleanArrayOf(false, true, false, true)
    )

    3 -> listOf(
        booleanArrayOf(true, false, true, true),
        booleanArrayOf(true, true, false, true),
        booleanArrayOf(true, false, false, false)
    )

    4 -> listOf(
        booleanArrayOf(true, false, true, false),
        booleanArrayOf(true, false, true, true),
        booleanArrayOf(false, false, true, false)
    )

    5 -> listOf(
        booleanArrayOf(true, false, true, false),
        booleanArrayOf(false, false, true, true),
        booleanArrayOf(true, false, true, false)
    )

    6 -> listOf(
        booleanArrayOf(true, true, false, true),
        booleanArrayOf(true, false, false, true),
        booleanArrayOf(false, false, true, false)
    )

    7 -> listOf(
        booleanArrayOf(true, true, false, true),
        booleanArrayOf(false, true, true, false),
        booleanArrayOf(true, false, false, false)
    )

    8 -> listOf(
        booleanArrayOf(false, true, false, false),
        booleanArrayOf(true, false, true, false),
        booleanArrayOf(true, true, true, false)
    )

    9 -> listOf(
        booleanArrayOf(true, false, true, false),
        booleanArrayOf(true, true, true, false),
        booleanArrayOf(true, true, false, false)
    )

    else -> throw Exception("This tile number wasn't acceptable: $number")
}.reversed()