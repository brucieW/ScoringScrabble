package com.zeroboss.scoringscrabble.data.common

import com.zeroboss.scoringscrabble.data.entities.LetterAndPosition
import com.zeroboss.scoringscrabble.data.entities.Letters
import com.zeroboss.scoringscrabble.data.entities.convertPosition
import io.objectbox.converter.PropertyConverter

class LetterAndPositionConverter : PropertyConverter<LetterAndPosition?, String?> {
    override fun convertToEntityProperty(text: String?): LetterAndPosition? {
        return text?.let {
            val index = it.indexOf(',')
            val letter = Letters.get(it.first())
            val position = convertPosition(it.substring(1, index))
            val blank = if (index == it.length - 1) null else Letters.get(it.last())
            LetterAndPosition(letter, position, blank)
        }
    }

    override fun convertToDatabaseValue(lp: LetterAndPosition?): String {
        val sb = StringBuilder()
        sb.append(lp!!.letter.letter)
        sb.append('A' + lp.position.column)
        sb.append(lp.position.row)
        sb.append(',')

        if (lp.blankValue != null) {
            sb.append(lp.blankValue.letter)
        }

        return sb.toString()
    }
}
