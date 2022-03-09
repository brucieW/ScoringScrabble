package com.zeroboss.scoringscrabble.data.entities

import com.zeroboss.scoringscrabble.R

/**
 * This contains information regarding a particular letter used for a scrabble tile.
 */
data class Letter (
    val character: Char,
    val image: Int,
    val value: Int,
    val quantity: Int
)

object Letters {
    private val letters = mapOf(
        'A' to Letter('A', R.drawable.letter_a, 3, 9),
        'B' to Letter('B', R.drawable.letter_b, 3, 2),
        'C' to Letter('C', R.drawable.letter_c, 3, 2),
        'D' to Letter('D', R.drawable.letter_d, 2, 4),
        'E' to Letter('E', R.drawable.letter_e, 1, 12),
        'F' to Letter('F', R.drawable.letter_f, 4, 2),
        'G' to Letter('G', R.drawable.letter_g, 2, 3),
        'H' to Letter('H', R.drawable.letter_h, 4, 2),
        'I' to Letter('I', R.drawable.letter_i, 1, 9),
        'J' to Letter('J', R.drawable.letter_j, 8, 1),
        'K' to Letter('K', R.drawable.letter_k, 5, 1),
        'L' to Letter('L', R.drawable.letter_l, 1, 4),
        'M' to Letter('M', R.drawable.letter_m, 3, 2),
        'N' to Letter('N', R.drawable.letter_n, 1, 6),
        'O' to Letter('O', R.drawable.letter_o, 1, 8),
        'P' to Letter('P', R.drawable.letter_p, 3, 2),
        'Q' to Letter('Q', R.drawable.letter_q, 10, 1),
        'R' to Letter('R', R.drawable.letter_r, 1, 6),
        'S' to Letter('S', R.drawable.letter_s, 1, 4),
        'T' to Letter('T', R.drawable.letter_t, 1, 6),
        'U' to Letter('U', R.drawable.letter_u, 1, 4),
        'V' to Letter('V', R.drawable.letter_v, 4, 2),
        'W' to Letter('W', R.drawable.letter_w, 4, 2),
        'X' to Letter('X', R.drawable.letter_x, 8, 1),
        'Y' to Letter('Y', R.drawable.letter_y, 4, 2),
        'Z' to Letter('Z', R.drawable.letter_z, 10, 1),
        '[' to Letter('[', R.drawable.letter_blank1, 0, 2),
    )

    fun get(
        c: Char
    ) : Letter {
        return letters.getValue(c)
    }
}


