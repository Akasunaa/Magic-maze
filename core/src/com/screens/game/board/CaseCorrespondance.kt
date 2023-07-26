package com.screens.game.board

import com.utils.Color
import java.lang.Exception

sealed interface CaseType {
    enum class BasicCase : CaseType {
        INACCESSIBLE, NONE, ENTRANCE, HOURGLASS, CRYSTAL_BALL, CAMERA;
    }

    sealed class ColoredCase(val color: Color) : CaseType {
        class Exit(color: Color) : ColoredCase(color);
        class Portal(color: Color) : ColoredCase(color);
        class Weapon(color: Color) : ColoredCase(color);
        class FinalExit(color: Color) : ColoredCase(color);

    }
}

/* Et maintenant, on construit allégrement la tuile
    Je vais maintenant expliquer comment on utilise le système de tableau
    On a un tableau 4x4 pour les cases, et pour les murs...
    Un tableau de 3x4 pour ceux verticaux
    Un tableau de 4x3 pour ceux horizontaux
    Pour le mur: 1 = un mur, 0 = pas de mur
*/
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

fun getTileArrayWallVertical(number: Int): List<List<Int>> = when (number) {
    0 -> listOf(listOf(0, 0, 0), listOf(0, 0, 0), listOf(0, 0, 1), listOf(0, 0, 1))
    1 -> listOf(listOf(1, 0, 0), listOf(1, 0, 1), listOf(0, 0, 1), listOf(0, 0, 1))
    2 -> listOf(listOf(0, 1, 0), listOf(1, 0, 1), listOf(1, 1, 0), listOf(1, 0, 0))
    3 -> listOf(listOf(1, 0, 1), listOf(0, 0, 0), listOf(0, 0, 1), listOf(1, 1, 0))
    4 -> listOf(listOf(1, 1, 0), listOf(0, 0, 1), listOf(1, 0, 0), listOf(1, 1, 1))
    5 -> listOf(listOf(1, 1, 0), listOf(1, 0, 0), listOf(0, 0, 1), listOf(1, 0, 0))
    6 -> listOf(listOf(0, 1, 1), listOf(0, 0, 1), listOf(1, 1, 0), listOf(1, 1, 1))
    7 -> listOf(listOf(0, 1, 1), listOf(0, 0, 0), listOf(1, 0, 1), listOf(1, 1, 1))
    8 -> listOf(listOf(0, 0, 0), listOf(1, 1, 1), listOf(0, 0, 1), listOf(0, 0, 0))
    9 -> listOf(listOf(1, 0, 0), listOf(0, 1, 1), listOf(1, 1, 1), listOf(0, 0, 0))
    else -> throw Exception("This tile number wasn't acceptable: $number")
}.reversed()

fun getTileArrayWallHorizontal(number: Int): List<List<Int>> = when (number) {
    0 -> listOf(listOf(1, 0, 0, 1), listOf(1, 0, 0, 1), listOf(1, 0, 0, 1))
    1 -> listOf(listOf(0, 0, 1, 0), listOf(0, 0, 0, 0), listOf(0, 1, 1, 1))
    2 -> listOf(listOf(0, 1, 0, 1), listOf(1, 1, 1, 0), listOf(0, 1, 0, 1))
    3 -> listOf(listOf(1, 0, 1, 1), listOf(1, 1, 0, 1), listOf(1, 0, 0, 0))
    4 -> listOf(listOf(1, 0, 1, 0), listOf(1, 0, 1, 1), listOf(0, 0, 1, 0))
    5 -> listOf(listOf(1, 0, 1, 0), listOf(0, 0, 1, 1), listOf(1, 0, 1, 0))
    6 -> listOf(listOf(1, 1, 0, 1), listOf(1, 0, 0, 1), listOf(0, 0, 1, 0))
    7 -> listOf(listOf(1, 1, 0, 1), listOf(0, 1, 1, 0), listOf(1, 0, 0, 0))
    8 -> listOf(listOf(0, 1, 0, 0), listOf(1, 0, 1, 0), listOf(1, 1, 1, 0))
    9 -> listOf(listOf(1, 0, 1, 0), listOf(1, 1, 1, 0), listOf(1, 1, 0, 0))
    else -> throw Exception("This tile number wasn't acceptable: $number")
}.reversed()