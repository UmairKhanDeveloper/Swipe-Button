package com.example.swipebutton.draggablecontrol

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swipebutton.confirmationstate.ConfirmationState
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConfirmationButton(
    modifier: Modifier = Modifier,
    text: String = "Swipe to Confirm",
    confirmedText: String = "Confirmed"
) {
    val width = 320.dp
    val dragSize = 56.dp

    val swipeableState = rememberSwipeableState(
        initialValue = ConfirmationState.DEFAULT
    )

    val sizePx = with(LocalDensity.current) {
        (width - dragSize).toPx()
    }

    val anchors = mapOf(
        0f to ConfirmationState.DEFAULT,
        sizePx to ConfirmationState.CONFIRMED
    )

    val offset = swipeableState.offset.value.coerceIn(0f, sizePx)
    val progress = (offset / sizePx)

    val isConfirmed = swipeableState.currentValue == ConfirmationState.CONFIRMED

    val backgroundColor by animateColorAsState(
        if (isConfirmed) Color(0xFF2E7D32)
        else Color(0xFF4CAF50)
    )

    Box(
        modifier = modifier
            .width(width)
            .height(dragSize)
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal
            )
            .padding(4.dp)
    ) {

        Text(
            text = if (isConfirmed) confirmedText else text,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .alpha(if (isConfirmed) 1f else 1f - progress)
        )

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(offset.roundToInt(), 0)
                }
                .size(dragSize)
                .shadow(6.dp, CircleShape)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(targetState = isConfirmed) { confirmed ->
                if (confirmed) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}