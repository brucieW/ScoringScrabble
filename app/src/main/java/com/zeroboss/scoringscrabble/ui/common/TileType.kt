package com.zeroboss.scoringscrabble.ui.common

import androidx.compose.ui.graphics.Color

enum class TileType(
    val info: TileInfo
) {
    TripleWord(
        TileInfo(
            color = Color(0xFFFFA500),
            text = "TW"
        )
    ),

    DoubleWord(
        TileInfo(
            color = Color(0xFFE75480),
            text = "DW"
        )
    ),

    TripleLetter(
        TileInfo(
            color = Color(0xFF1E2F97),
            text = "TL"
        )
    ),

    DoubleLetter(
        TileInfo(
            color = Color(0xFF1AA7EC),
            text = "DL"
        )
    ),

    Starting(
        TileInfo(
            color = Color(0xFFE75480)
        )
    ),

    Normal(
        TileInfo(
            color = Color(0xFFF6E4AD)
        )
    )
}

data class TileInfo(
    val color: Color = Color(0xFFfcedc5),
    val text: String = ""
)

object TileSettings {
    fun getTileInfo(
        position: String
    ): TileType {
        if (tripleWordTiles.find { it == position } != null) {
            return TileType.TripleWord
        }

        if (doubleWordTiles.find { it == position } != null) {
            return TileType.DoubleWord
        }

        if (tripleLetterTiles.find { it == position } != null) {
            return TileType.TripleLetter
        }

        if (doubleLetterTiles.find { it == position } != null) {
            return TileType.DoubleLetter
        }

        if (position == startingTile) {
            return TileType.Starting
        }

        return TileType.Normal
    }

    private val tripleWordTiles = listOf(
        "A1", "H1", "O1", "A8", "O8", "A15", "H15", "O15"
    )

    private val doubleWordTiles = listOf(
        "B2", "C3", "D4", "E5", "K5", "L4", "M3", "N2",
        "B14", "C13", "D12", "E11", "K11", "L12", "M13", "N14"
    )

    private val tripleLetterTiles = listOf(
        "F2", "J2", "B6", "F6", "J6", "N6",
        "B10", "F10", "J10", "N10", "F14", "J14"
    )

    private val doubleLetterTiles = listOf(
        "D1", "L1", "G3", "I3", "A4", "H4", "O4",
        "C7", "G7", "I7", "M7", "D8", "L8",
        "C9", "G9", "I9", "M9",
        "A12", "H12", "O12", "G13", "I13", "D15", "L15"
    )

    private const val startingTile = "H8"
}


