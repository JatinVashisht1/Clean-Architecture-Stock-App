package com.example.cleanarchitecturestockapppl.presentation.company_info.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cleanarchitecturestockapppl.domain.model.IntradayInfoModel
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun StockChart(
    modifier: Modifier = Modifier,
    infos: List<IntradayInfoModel> = emptyList(),
    graphColor: Color = Color.Green
) {
    val spacing = 100f
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }
    // infos here is is the key, so it will be recalculated whenever the infos changes
    val upperValue = remember(infos) {
        // let's understand logic of below code
        // we are finding max value of the list that can be null
        // if it is not null then we are taking the maxvalue of the list and increase it by 1
        // then we are rounding off it to int
        // so at the ending we are rounding off it to its floor value that's why we have incremented it by 1
        // so that it gets round off to its ceiling value
        (infos.maxOfOrNull { it.close }?.plus(1))?.roundToInt() ?: 0
    }

    // here we have not performed all the above goddamn operations because we want flor value in this case
    val lowerValue = remember(infos) {
        infos.minOfOrNull { it.close }?.toInt() ?: 0
    }

    val density = LocalDensity.current

    val textPaint = remember(density) {
        android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(modifier = modifier) {
        // for x axis
        val spacingPerHour = (size.width - spacing) / infos.size

        // below is the code demonstrating how we want to draw our x-axis
        (0 until infos.size - 1 step 2).forEach { i ->
            val info = infos[i]
            val hour = info.date.hour
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(),
                    // let's understand the below statement
                    // we have added i because initially i is 0 and then it gets incremented by 2
                    // and the x axis starts from left so we are putting numbers at their correct place
                    spacing + i + spacingPerHour,
                    size.height - 5, // 5 here is random number which means 5 px from bottom (y starts at top and ends at bottom)
                    textPaint,
                )
            }
        }
        val priceStep = (upperValue - lowerValue) / 5f
        (0..4).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(

                    // lowerValue would be first value of y axis
                    // starting with i = 0, so we start at the lower value
                    // so at next round priceStep will be multiplied to i and it will be our next value
                    // this process will continue
                    round(lowerValue + priceStep * i).toString(),
                    30f,
                    // this shows what from where we want to start and how much gap will be between each value of y axis
                    size.height - spacing - i * size.height,
                    textPaint
                )
            }
        }

        var lastX = 0f
        // Path will be used to draw lines of graph
        val strokePath = Path().apply {
            val height = size.height
            for (i in infos.indices) {
                val info = infos[i]
                // let's understand the below line of code
                // nextInfo is used to get what is the next point we want to draw our line from current point
                // we are using null check operator to check if the next point is present or not
                // so if current point is the last index then there would be no next point and to avoid index out of bound exception
                val nextInfo = infos.getOrNull(i + 1) ?: infos.last()
                // formula to map left and right point to a value between 0 and 1
                // left
                val leftRatio = (info.close - lowerValue) / (upperValue - lowerValue)
                val rightRatio = (nextInfo.close - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacingPerHour
                val y1 = height - spacing - (leftRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacingPerHour
                val y2 = height - spacing - (rightRatio * height).toFloat()
                if (i == 0) {
                    // below code snippet is the way to move to left point
                    // we are connecting one left and one right point
                    moveTo(x1, y1)
                }

                lastX = (x1 + x2)/2f

                // this is the mathematical calculation that make a smooth path
                quadraticBezierTo(
                    x1, y1, (x1 + x2) / 2, (y1 + y2) / 2,
                )

            }
        }

        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
            .asComposePath()
            .apply {
                lineTo(lastX, size.height - spacing)
                lineTo(spacing, size.height - spacing)
                close() //  close means we connect the current point with the starting point
            }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(transparentGraphColor, Color.Transparent),
                endY = size.height - spacing
            )
        )
        drawPath(
            path = strokePath,
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round // end of the stroke would be rounded
            )
        )
    }
}