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
            val isBlank = index != it.length - 1
            LetterAndPosition(letter, position, isBlank)
        }
    }

    override fun convertToDatabaseValue(lp: LetterAndPosition?): String {
        val sb = StringBuilder()
        sb.append(lp!!.letter.character)
        sb.append('A' + lp.position.column)
        sb.append(lp.position.row)
        sb.append(',')
        sb.append(if (lp.isBlank) 1 else 0)

        return sb.toString()
    }
}
