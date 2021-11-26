package com.zeroboss.scoringscrabble.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

val navigationTitle = TextStyle(
    fontFamily = FontFamily.Default,
    fontSize = 10.sp,
    color = Color.White
)

val warningTitle = TextStyle(
    fontFamily = FontFamily.Default,
    fontSize = 26.sp,
    color = Color.Red
)

val dialogTitle = TextStyle(
    fontFamily = FontFamily.Default,
    fontSize = 22.sp,
    fontWeight = FontWeight.Bold,
    color = Blue800
)

val navigationTitle2 = TextStyle(
    fontFamily = FontFamily.Default,
    fontSize = 22.sp,
    color = Color.White
)

val erasedTextStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontSize = 24.sp,
    textDecoration = TextDecoration.LineThrough,
    color = Color.Black
)

val textInputStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold,
    color = Blue800
)

val normalText = TextStyle(
    fontFamily = FontFamily.Default,
    fontSize = 18.sp,
    fontWeight = FontWeight.SemiBold,
    color = Blue800
)

val errorText = TextStyle(
    fontFamily = FontFamily.Default,
    fontSize = 14.sp,
    fontWeight = FontWeight.SemiBold,
    color = Color.Red
)

val smallerText = TextStyle(
    fontFamily = FontFamily.Default,
    fontSize = 14.sp,
    fontWeight = FontWeight.SemiBold,
    color = Blue800
)

val textTitleStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold,
    color = Blue800
)

val typography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 96.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 117.sp,
        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 60.sp,
        fontWeight = FontWeight.Light,
        lineHeight = 73.sp,
        letterSpacing = (-0.5).sp,
        color = Color.Black
    ),
    h3 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 48.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 59.sp,
        color = Blue800
    ),
    h4 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 30.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 37.sp,
        color = Blue800
    ),
    h5 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 29.sp,
        color = Blue800
    ),
    h6 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp,
        color = Blue800
    ),
    subtitle1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.15.sp,
        color = Blue800
    ),
    subtitle2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp,
        color = Blue800
    ),
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp,
        letterSpacing = 0.15.sp
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        color = Color.Black
    ),
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.25.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
    ),
    overline = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 16.sp,
        letterSpacing = 1.sp
    )
)
