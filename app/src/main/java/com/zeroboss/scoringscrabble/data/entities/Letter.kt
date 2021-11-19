package com.zeroboss.scoringscrabble.data.entities

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.zeroboss.scoringscrabble.R

data class Letter (
    val letter: Char,
    val image: Int,
    val value: Int,
    val quantity: Int
)

object Letters {
    private val letters: List<Letter> = listOf<Letter>(
        Letter('A', R.drawable.letter_a, 3, 9),
        Letter('B', R.drawable.letter_b, 3, 2),
        Letter('C', R.drawable.letter_c, 3, 2),
        Letter('D', R.drawable.letter_d, 2, 4),
        Letter('E', R.drawable.letter_e, 1, 12),
        Letter('F', R.drawable.letter_f, 4, 2),
        Letter('G', R.drawable.letter_g, 2, 3),
        Letter('H', R.drawable.letter_h, 4, 2),
        Letter('I', R.drawable.letter_i, 1, 9),
        Letter('J', R.drawable.letter_j, 8, 1),
        Letter('K', R.drawable.letter_k, 5, 1),
        Letter('L', R.drawable.letter_l, 1, 4),
        Letter('M', R.drawable.letter_m, 3, 2),
        Letter('N', R.drawable.letter_n, 1, 6),
        Letter('O', R.drawable.letter_o, 1, 8),
        Letter('P', R.drawable.letter_p, 3, 2),
        Letter('Q', R.drawable.letter_q, 10, 1),
        Letter('R', R.drawable.letter_r, 1, 6),
        Letter('S', R.drawable.letter_s, 1, 4),
        Letter('T', R.drawable.letter_t, 1, 6),
        Letter('U', R.drawable.letter_u, 1, 4),
        Letter('V', R.drawable.letter_v, 4, 2),
        Letter('W', R.drawable.letter_w, 4, 2),
        Letter('X', R.drawable.letter_x, 8, 1),
        Letter('Y', R.drawable.letter_y, 4, 2),
        Letter('Z', R.drawable.letter_z, 10, 1),
        Letter('*', R.drawable.letter_blank1, 0, 2),
    )

    fun get(
        c: Char
    ) : Letter {
        return letters.first { letter ->  letter.letter == c }
    }
}


