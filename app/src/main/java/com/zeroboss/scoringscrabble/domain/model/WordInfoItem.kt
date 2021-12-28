package com.zeroboss.scoringscrabble.domain.model

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroboss.scoringscrabble.ui.theme.*

@Composable
fun WordInfoItem(
    wordInfo: WordInfo
) {
    Text(
        text = wordInfo.word,
        style = wordInfoTitleStyle
    )

    Text(
        text = wordInfo.phonetic,
        style = wordInfoLightStyle
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = wordInfo.origin,
        style = wordInfoStyle
    )

    wordInfo.meanings.forEach { meaning ->
        Text(
            text = meaning.partOfSpeech,
            style = wordInfoBoldStyle
        )

        meaning.definitions.forEachIndexed { index, definition ->
            Text(
                text = "${index + 1}. ${definition.definition}",
                style = wordInfoStyle
            )

            Spacer(modifier = Modifier.height(8.dp))

            definition.example?.let { example ->
                Text(
                    text = "Example: $example",
                    style = wordInfoStyle
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
