package com.zeroboss.scoringscrabble.data.common

import com.zeroboss.scoringscrabble.data.entities.Letter
import com.zeroboss.scoringscrabble.data.entities.Letters
import io.objectbox.converter.PropertyConverter

class LetterConverter : PropertyConverter<Letter?, String?> {
    override fun convertToEntityProperty(text: String?): Letter? {
        return text?.let {
            Letters.get(it.first())
        }
    }

    override fun convertToDatabaseValue(letter: Letter?): String? {
        return letter?.let {
            'A' + it.letter.toString()
        }
    }
}
