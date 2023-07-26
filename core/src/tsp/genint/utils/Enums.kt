package tsp.genint.utils

enum class Color {
    GREEN, PURPLE, YELLOW, ORANGE, NONE;
}

enum class Directions {
    SOUTH, EAST, NORTH, WEST;

    operator fun plus(other: Int): Int = Directions.values().indexOf(this) + other
}