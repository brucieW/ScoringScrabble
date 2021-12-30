package com.zeroboss.scoringscrabble.ui.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

enum class Direction(
    val icon: ImageVector,
    val description: String
) {
    Up(Icons.Rounded.KeyboardArrowUp, "up"),
    Down(Icons.Rounded.KeyboardArrowDown, "down"),
    Left(Icons.Rounded.KeyboardArrowLeft, "left"),
    Right(Icons.Rounded.KeyboardArrowRight, "right")
}

fun animatedStateValue(
    state: MutableState<Int>,
    offset: Float,
    halvedNumbersSizePx: Float
): Int = state.value - (offset / halvedNumbersSizePx).toInt()

@Composable
fun NumberPicker(
    state: MutableState<Int>,
    modifier: Modifier = Modifier,
    firstDirection: Direction = Direction.Left,
    range: IntRange? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    onStateChanged: (Int) -> Unit = {},
) {
    val numbersSize = 32.dp
    val halvedNumbersSize = numbersSize / 2
    val halvedNumbersSizePx = with(LocalDensity.current) { halvedNumbersSize.toPx() }

    val animatedOffset = remember { Animatable(0f) }.apply {
        if (range != null) {
            val offsetRange = remember(state.value, range) {
                val value = state.value
                val first = -(range.last - value) * halvedNumbersSizePx
                val last = -(range.first - value) * halvedNumbersSizePx
                first..last
            }
            updateBounds(offsetRange.start, offsetRange.endInclusive)
        }
    }

    val coercedAnimatedOffset = animatedOffset.value % halvedNumbersSizePx
    val animatedStateValue = animatedStateValue(state, animatedOffset.value, halvedNumbersSizePx)

    if (firstDirection == Direction.Left) {
        Row(
            modifier = getDragModifier(
                state,
                modifier,
                animatedOffset,
                halvedNumbersSizePx,
                onStateChanged
            ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = {
                PickerContent(
                    state,
                    range,
                    firstDirection,
                    coercedAnimatedOffset,
                    textStyle,
                    animatedStateValue,
                    halvedNumbersSize,
                    halvedNumbersSizePx,
                    onStateChanged,
                )
            }
        )
    } else {
        Column(
            modifier = getDragModifier(
                state,
                modifier,
                animatedOffset,
                halvedNumbersSizePx,
                onStateChanged
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                PickerContent(
                    state,
                    range,
                    firstDirection,
                    coercedAnimatedOffset,
                    textStyle,
                    animatedStateValue,
                    halvedNumbersSize,
                    halvedNumbersSizePx,
                    onStateChanged
                )
            }
        )
    }
}

@Composable
fun PickerContent(
    state: MutableState<Int>,
    range: IntRange? = null,
    firstDirection: Direction,
    coercedAnimatedOffset: Float,
    textStyle: TextStyle,
    animatedStateValue: Int,
    halvedNumbersSize: Dp,
    halvedNumbersSizePx: Float,
    onStateChanged: (Int) -> Unit,
) {
    RangeButton(
        state,
        range!!,
        firstDirection,
        textStyle.color,
        onStateChanged
    )

    Box(
        modifier = Modifier
            .offset { IntOffset(x = 0, y = coercedAnimatedOffset.roundToInt()) }
    ) {
        val baseLabelModifier = Modifier.align(Alignment.Center)
        ProvideTextStyle(textStyle) {
            Label(
                text = (animatedStateValue - 1).toString(),
                modifier = baseLabelModifier
                    .offset(y = -halvedNumbersSize)
                    .alpha(coercedAnimatedOffset / halvedNumbersSizePx)
            )
            Label(
                text = animatedStateValue.toString(),
                modifier = baseLabelModifier
                    .alpha(1 - abs(coercedAnimatedOffset) / halvedNumbersSizePx)
            )
            Label(
                text = (animatedStateValue + 1).toString(),
                modifier = baseLabelModifier
                    .offset(y = halvedNumbersSize)
                    .alpha(-coercedAnimatedOffset / halvedNumbersSizePx)
            )
        }
    }

    RangeButton(
        state,
        range,
        if (firstDirection == Direction.Left) Direction.Right else Direction.Down,
        textStyle.color,
        onStateChanged
    )

}

@Composable
fun getDragModifier(
    state: MutableState<Int>,
    modifier: Modifier,
    animatedOffset: Animatable<Float, AnimationVector1D>,
    halvedNumbersSizePx: Float,
    onStateChanged: (Int) -> Unit
) : Modifier {
    val coroutineScope = rememberCoroutineScope()

    return modifier
        .wrapContentSize()
        .draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { deltaY ->
                coroutineScope.launch {
                    animatedOffset.snapTo(animatedOffset.value + deltaY)
                }
            },
            onDragStopped = { velocity ->
                coroutineScope.launch {
                    val endValue = animatedOffset.fling(
                        initialVelocity = velocity,
                        animationSpec = exponentialDecay(frictionMultiplier = 20f),
                        adjustTarget = { target ->
                            val coercedTarget = target % halvedNumbersSizePx
                            val coercedAnchors = listOf(
                                -halvedNumbersSizePx,
                                0f,
                                halvedNumbersSizePx
                            )
                            val coercedPoint =
                                coercedAnchors.minByOrNull { abs(it - coercedTarget) }!!
                            val base =
                                halvedNumbersSizePx * (target / halvedNumbersSizePx).toInt()
                            coercedPoint + base
                        }
                    ).endState.value

                    state.value = animatedStateValue(state, endValue, halvedNumbersSizePx)
                    onStateChanged(state.value)
                    animatedOffset.snapTo(0f)
                }
            }
        )
}

@Composable
fun RangeButton(
    state: MutableState<Int>,
    range: IntRange,
    direction: Direction,
    tint: Color,
    onStateChanged: (Int) -> Unit
) {
    IconButton(
        modifier = Modifier.size(24.dp),
        onClick = {
            if (direction == Direction.Left || direction == Direction.Up) {
                state.value = if (state.value == range.first) range.last else state.value - 1
            } else {
                state.value = if (state.value == range.last) range.first else state.value + 1
            }
            onStateChanged(state.value)
        }
    ) {
        Icon(
            direction.icon,
            contentDescription = direction.description,
            tint = tint
        )
    }
}

@Composable
private fun Label(text: String, modifier: Modifier) {
    Text(
        text = text,
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onLongPress = {
                // Empty to disable text selection
            })
        }
    )
}

private suspend fun Animatable<Float, AnimationVector1D>.fling(
    initialVelocity: Float,
    animationSpec: DecayAnimationSpec<Float>,
    adjustTarget: ((Float) -> Float)?,
    block: (Animatable<Float, AnimationVector1D>.() -> Unit)? = null,
): AnimationResult<Float, AnimationVector1D> {
    val targetValue = animationSpec.calculateTargetValue(value, initialVelocity)
    val adjustedTarget = adjustTarget?.invoke(targetValue)

    return if (adjustedTarget != null) {
        animateTo(
            targetValue = adjustedTarget,
            initialVelocity = initialVelocity,
            block = block
        )
    } else {
        animateDecay(
            initialVelocity = initialVelocity,
            animationSpec = animationSpec,
            block = block,
        )
    }
}

@Preview
@Composable
fun PreviewLeftRightPicker() {
    Box(modifier = Modifier.fillMaxWidth()) {
        NumberPicker(
            state = remember { mutableStateOf(9) },
            range = 0..10,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
@Preview
@Composable
fun PreviewUpDownPicker() {
    Box(modifier = Modifier.fillMaxWidth()) {
        NumberPicker(
            state = remember { mutableStateOf(9) },
            firstDirection = Direction.Up,
            range = 0..10,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}