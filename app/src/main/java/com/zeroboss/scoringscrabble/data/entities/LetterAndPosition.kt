package com.zeroboss.scoringscrabble.data.entities

data class LetterAndPosition(
    val letter: Letter,
    val position: Position,
    val blankValue: Letter? = null
)

data class Position(
    val column: Int = 0,
    val row: Int = 0
) {
    companion object {
        val maxRows = 15
        val maxColumns = 15
    }

    fun isValid() : Boolean {
        return column > 0 && row > 0
    }
}

fun convertPosition(
    position: String
) : Position {
    var newPosition = Position()

    if (position.length == 2 || position.length == 3) {
        val column = position[0] - 'A' + 1

        if (column > 0 && column <= Position.maxColumns) {
            val row = position.substring(1).toInt()

            if (row > 0 && row <= Position.maxRows) {
                newPosition = Position(column, row)
            }
        }
    }

    return newPosition
}

enum class Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST
}

fun getNewPosition(
    position: Position,
    direction: Direction,
    tileCount: Int
): Position {
    var newPosition = Position()

    when (direction) {
        Direction.EAST -> {
            val newColumn = position.column + tileCount

            if (newColumn < 16) {
                newPosition = Position(newColumn, position.row)
            }
        }

        Direction.WEST -> {
            val newColumn = position.column - tileCount

            if (newColumn > 1) {
                newPosition = Position(newColumn, position.row)
            }
        }

        Direction.NORTH -> {
            val newRow = position.row - tileCount

            if (newRow > 0) {
                newPosition = Position(position.column, newRow)
            }
        }

        Direction.SOUTH -> {
            val newRow = position.row + tileCount

            if (newRow < 16) {
                newPosition = Position(position.column, newRow)
            }
        }
    }

    return newPosition
}
