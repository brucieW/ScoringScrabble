package com.zeroboss.scoringscrabble.data.common

import com.zeroboss.scoringscrabble.data.entities.Letter
import com.zeroboss.scoringscrabble.data.entities.Letters
import com.zeroboss.scoringscrabble.data.entities.Position
import com.zeroboss.scoringscrabble.data.entities.convertPosition
import io.objectbox.converter.PropertyConverter

class PositionConverter : PropertyConverter<Position?, String?> {
    override fun convertToEntityProperty(text: String?): Position? {
        return text?.let {
            convertPosition(it)
        }
    }

    override fun convertToDatabaseValue(position: Position?): String? {
        return position?.let {
            val sb = StringBuilder()
            sb.append('A' + it.column)
            sb.append(it.row)
            sb.toString()
        }
    }
}
