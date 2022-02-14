package com.zeroboss.scoringscrabble.ui.screens.scoresheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zeroboss.scoringscrabble.data.entities.LetterAndPosition
import com.zeroboss.scoringscrabble.domain.model.WordInfoItem
import com.zeroboss.scoringscrabble.presentation.WordInfoViewModel
import com.zeroboss.scoringscrabble.ui.theme.errorText
import com.zeroboss.scoringscrabble.ui.theme.smallerText

enum class PulseState {
    None,
    GrowStart,
    GrowEnd
}

enum class MoveTileState(val letter: LetterAndPosition = LetterAndPosition()) {
    None(),
    Start(),
    End()
}

@Composable
fun Dictionary(
    width: Dp,
    wordInfoViewModel: WordInfoViewModel
) {
    val activeWords = remember { wordInfoViewModel.wordList }
    val selectedWords = remember { wordInfoViewModel.selectedWords }

    val errorMessage by wordInfoViewModel.errorText

    val state by wordInfoViewModel.state

    if (activeWords.size > 0) {
        Card(
            modifier = Modifier
                .width(width)
                .padding(top = 10.dp, end = 10.dp),
            elevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    activeWords.forEachIndexed { index, word ->
                        Box(
                            modifier = Modifier
                                .padding(top = 10.dp, start = 10.dp)
                                .clickable {
                                    if (selectedWords[index]) {
                                        wordInfoViewModel.clearSearch()
                                        selectedWords[index] = false
                                    } else {
                                        selectedWords.forEachIndexed { index, _ ->
                                            selectedWords[index] = false
                                        }

                                        selectedWords[index] = true
                                        wordInfoViewModel.onSearch(activeWords[index])
                                    }
                                }
                                .border(
                                    BorderStroke(
                                        if (selectedWords[index]) 3.dp else 1.dp,
                                        Color.Black
                                    )
                                ),
                        ) {
                            Text(
                                text = word,
                                style = smallerText,
                                modifier = Modifier.padding(10.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 20.dp, start = 50.dp)
                    )
                }

                state.wordInfoItems.forEachIndexed { i, wordInfo ->
                    if (i > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    WordInfoItem(wordInfo)
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        style = errorText
                    )
                }
            }
        }
    }
}
