package com.zeroboss.scoringscrabble.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zeroboss.scoringscrabble.ui.theme.*

@Composable
fun ScoringButton(
    modifier: Modifier = Modifier,
    text: String = "",
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    smallButtons: Boolean = false
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick!!,
        shape = RoundedCornerShape(if (smallButtons) 10.dp else 20.dp),
        elevation = ButtonDefaults.elevation(10.dp, 0.dp, 0.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,
            disabledBackgroundColor = Color.Gray
        )
    ) {
        val horizontalPaddding = if (smallButtons) 8.dp else 16.dp
        val verticalPadding = if (smallButtons) 4.dp else 8.dp

        Box(
            modifier = Modifier
                .background(if (enabled) buttonBrush else disabledBrush)
                .padding(horizontal = horizontalPaddding, vertical = verticalPadding)
        ) {
            Text(
                text = text,
                color = if (enabled) textColorLight else textColorDark,
                style = if (smallButtons) normalText else typography.button
            )
        }
    }
}

@Composable
fun MultipleButtonBar(
    buttonData: List<ButtonData>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        buttonData.forEach { data ->
            ScoringButton(
                modifier = Modifier.padding(start = 20.dp),
                text = data.text,
                enabled = data.enabled,
                onClick = data.onClicked
            )
        }
    }
}

data class ButtonData(
    val text: String,
    val enabled: Boolean = true,
    val onClicked: () -> Unit
)

fun getTwoButtons(
    firstButtonText: String = "Save",
    secondButtonText: String = "Cancel",
    firstButtonEnabled: Boolean = false,
    secondButtonEnabled: Boolean = true,
    onFirstButtonClicked: () -> Unit,
    onSecondButtonClicked: () -> Unit
): List<ButtonData> {
    return listOf(
        ButtonData(
            firstButtonText,
            onClicked = onFirstButtonClicked,
            enabled = firstButtonEnabled
        ),

        ButtonData(
            secondButtonText,
            onClicked = onSecondButtonClicked,
            enabled = secondButtonEnabled )
    )
}

fun getYesNoButtons(
    yesButtonEnabled: Boolean = true,
    onYesClicked: () -> Unit,
    onNoClicked: () -> Unit
) : List<ButtonData>  {
    return getTwoButtons(
        firstButtonText = "Yes",
        secondButtonText = " No ",
        firstButtonEnabled = yesButtonEnabled,
        onFirstButtonClicked = onYesClicked,
        onSecondButtonClicked = onNoClicked
    )
}

@Preview
@Composable
fun ShowButton() {
    ScoringButton(
        text = "This is a test",
        onClick = { }
    )
}

@Preview
@Composable
fun ShowSaveCancel() {
    MultipleButtonBar(
        getTwoButtons(
            onFirstButtonClicked = {},
            onSecondButtonClicked = {}
        )
    )
}


@Preview
@Composable
fun ShowYesNo() {
    MultipleButtonBar(
        getYesNoButtons(
            onYesClicked = {},
            onNoClicked = {}
        )
    )
}