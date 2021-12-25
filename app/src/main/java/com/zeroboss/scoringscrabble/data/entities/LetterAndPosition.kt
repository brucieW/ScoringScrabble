package com.zeroboss.scoringscrabble.data.entities

import com.zeroboss.scoringscrabble.data.common.LetterConverter
import com.zeroboss.scoringscrabble.data.common.PositionConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class LetterAndPosition(
    @Convert(converter = LetterConverter::class, dbType = String::class)
    val letter: Letter = Letters.get('A'),

    @Convert(converter = PositionConverter::class, dbType = String::class)
    val position: Position = Position(0, 0),

    @Convert(converter = LetterConverter::class, dbType = String::class)
    val blankValue: Letter? = null,

    @Id
    var id: Long = 0
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
