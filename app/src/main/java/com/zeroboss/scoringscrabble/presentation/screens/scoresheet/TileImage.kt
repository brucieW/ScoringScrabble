package com.zeroboss.scoringscrabble.presentation.screens.scoresheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.zeroboss.scoringscrabble.R

@Composable
fun TileImage(
    compose: DrawScope,
    letter: Char,
    offset: IntOffset,
    size: Int
) {
    val image = when (letter) {
        'A' -> ImageBitmap.imageResource(R.drawable.letter_a)
        'B' -> ImageBitmap.imageResource(R.drawable.letter_b)
        'C' -> ImageBitmap.imageResource(R.drawable.letter_c)
        'D' -> ImageBitmap.imageResource(R.drawable.letter_d)
        'E' -> ImageBitmap.imageResource(R.drawable.letter_e)
        'F' -> ImageBitmap.imageResource(R.drawable.letter_f)
        'G' -> ImageBitmap.imageResource(R.drawable.letter_g)
        'H' -> ImageBitmap.imageResource(R.drawable.letter_h)
        'I' -> ImageBitmap.imageResource(R.drawable.letter_i)
        'J' -> ImageBitmap.imageResource(R.drawable.letter_j)
        'K' -> ImageBitmap.imageResource(R.drawable.letter_k)
        'L' -> ImageBitmap.imageResource(R.drawable.letter_l)
        'M' -> ImageBitmap.imageResource(R.drawable.letter_m)
        'N' -> ImageBitmap.imageResource(R.drawable.letter_n)
        'O' -> ImageBitmap.imageResource(R.drawable.letter_o)
        'P' -> ImageBitmap.imageResource(R.drawable.letter_p)
        'Q' -> ImageBitmap.imageResource(R.drawable.letter_q)
        'R' -> ImageBitmap.imageResource(R.drawable.letter_r)
        'S' -> ImageBitmap.imageResource(R.drawable.letter_s)
        'T' -> ImageBitmap.imageResource(R.drawable.letter_t)
        'U' -> ImageBitmap.imageResource(R.drawable.letter_u)
        'V' -> ImageBitmap.imageResource(R.drawable.letter_v)
        'W' -> ImageBitmap.imageResource(R.drawable.letter_w)
        'X' -> ImageBitmap.imageResource(R.drawable.letter_x)
        'Y' -> ImageBitmap.imageResource(R.drawable.letter_y)
        'Z' -> ImageBitmap.imageResource(R.drawable.letter_z)
        else -> null
    }

    compose.drawImage(
        image = image!!,
        dstOffset = offset,
        dstSize = IntSize(size, size)
    )
}

