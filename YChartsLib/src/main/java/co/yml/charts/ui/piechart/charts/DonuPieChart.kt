package co.yml.charts.ui.piechart.charts

import android.graphics.Paint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import co.yml.charts.ui.piechart.utils.convertTouchEventPointToAngle
import co.yml.charts.ui.piechart.utils.proportion
import co.yml.charts.ui.piechart.utils.sweepAngles
import co.yml.charts.common.components.accessibility.SliceInfo
import co.yml.charts.common.extensions.collectIsTalkbackEnabledAsState
import co.yml.charts.common.model.PlotType
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


/**
 * Compose function for Drawing Donut chart
 * @param modifier : All modifier related property
 * @param pieChartData: data list for the pie chart
 * @param pieChartConfig: configuration for the pie chart
 * @param onSliceClick(pieChartData.Slice)->Unit: The event that captures the click
 */
@Composable
fun DonutPieChart(
    modifier: Modifier,
    pieChartData: PieChartData,
    pieChartConfig: PieChartConfig,
    onSliceClick: (PieChartData.Slice) -> Unit = {}
) {
    // Sum of all the values
    val sumOfValues = pieChartData.totalLength

    // Calculate each proportion value
    val proportions = pieChartData.slices.proportion(sumOfValues)

    // Convert each proportions to angle
    val sweepAngles = proportions.sweepAngles()

    val progressSize = mutableListOf<Float>()
    progressSize.add(sweepAngles.first())
    for (x in 1 until sweepAngles.size) {
        progressSize.add(sweepAngles[x] + progressSize[x - 1])
    }

    var activePie by rememberSaveable {
        mutableStateOf(-1)
    }
    LaunchedEffect(key1 = proportions) {
        activePie = -1
    }
//    val accessibilitySheetState =
//        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val isTalkBackEnabled by LocalContext.current.collectIsTalkbackEnabledAsState()
//    if (accessibilitySheetState.isVisible && isTalkBackEnabled
//        && pieChartConfig.accessibilityConfig.shouldHandleBackWhenTalkBackPopUpShown
//    ) {
//        BackHandler {
//            scope.launch {
//                accessibilitySheetState.hide()
//            }
//        }
//    }
    Surface(
        modifier = modifier
    ) {
        BoxWithConstraints(
            modifier = modifier
                .aspectRatio(1f)
                .semantics {
                    contentDescription = pieChartConfig.accessibilityConfig.chartDescription
                }
                .clickable {
                    if (isTalkBackEnabled) {
                        scope.launch {
//                            accessibilitySheetState.animateTo(
//                                ModalBottomSheetValue.Expanded
//                            )
                           // accessibilitySheetState.show()
                        }
                    }
                }) {

            val sideSize = Integer.min(constraints.maxWidth, constraints.maxHeight)
            val padding = (sideSize * pieChartConfig.chartPadding) / 100f
            val size = Size(sideSize.toFloat() - padding, sideSize.toFloat() - padding)

            val pathPortion = remember {
                Animatable(initialValue = 0f)
            }

            if (pieChartConfig.isAnimationEnable) {
                LaunchedEffect(key1 = Unit) {
                    pathPortion.animateTo(
                        1f, animationSpec = tween(pieChartConfig.animationDuration)
                    )
                }
            }

            Canvas(
                modifier = Modifier
                    .width(sideSize.dp)
                    .height(sideSize.dp)
                    .pointerInput(progressSize) {

                        detectTapGestures {
                            val clickedAngle = convertTouchEventPointToAngle(
                                sideSize.toFloat(),
                                sideSize.toFloat(),
                                it.x,
                                it.y
                            )
                            progressSize.forEachIndexed { index, item ->
                                if (clickedAngle <= item) {
                                    if (activePie != index)
                                        activePie = index
                                    onSliceClick(pieChartData.slices[index])
                                    return@detectTapGestures
                                }
                            }
                        }
                    }

            ) {

                var sAngle = pieChartConfig.startAngle

                sweepAngles.forEachIndexed { index, arcProgress ->
                    drawPie(
                        color = pieChartData.slices[index].color,
                        startAngle = sAngle,
                        arcProgress = if (pieChartConfig.isAnimationEnable)
                            arcProgress * pathPortion.value else arcProgress,
                        size = size,
                        padding = padding,
                        isDonut = pieChartData.plotType == PlotType.Donut,
                        strokeWidth = pieChartConfig.strokeWidth,
                        activeAddWidth = pieChartConfig.activeAddWidth,
                        sliceGap = pieChartConfig.sliceGap,
                        isActive = activePie == index,
                        pieChartConfig = pieChartConfig
                    )
                    sAngle += arcProgress
                }

                if (activePie != -1 && pieChartConfig.percentVisible)
                    drawContext.canvas.nativeCanvas.apply {
                        val fontSize = pieChartConfig.percentageFontSize.toPx()
                        drawText(
                            "${proportions[activePie].roundToInt()}%",
                            (sideSize / 2) + fontSize / 4, (sideSize / 2) - fontSize / 4,
                            Paint().apply {
                                color = pieChartConfig.percentColor.toArgb()
                                textSize = fontSize
                                textAlign = Paint.Align.CENTER
                                typeface = pieChartConfig.percentageTypeface

                            }
                        )
                        val labelFontSize = pieChartConfig.labelFontSize.toPx()
                        drawText(
                            pieChartData.slices[activePie].label,
                            (sideSize / 2) + labelFontSize / 4, (sideSize / 2) + labelFontSize,
                            Paint().apply {
                                color = pieChartConfig.percentColor.toArgb()
                                textSize = labelFontSize
                                textAlign = Paint.Align.CENTER
                                typeface = pieChartConfig.percentageTypeface

                            }
                        )
                    }
            }
        }
        if (isTalkBackEnabled) {
            with(pieChartConfig) {
//                AccessibilityBottomSheetDialog(
//                    modifier = Modifier.fillMaxSize(),
//                    backgroundColor = Color.White,
//                    content = {
//                        LazyColumn {
//                            items(pieChartData.slices.size) { index ->
//                                SliceInfo(
//                                    pieChartData.slices[index],
//                                    proportions[index].roundToInt()
//                                )
//                            }
//                        }
//                    },
//                    popUpTopRightButtonTitle = accessibilityConfig.popUpTopRightButtonTitle,
//                    popUpTopRightButtonDescription = accessibilityConfig.popUpTopRightButtonDescription,
//                    sheetState = accessibilitySheetState
//                )
            }
        }
    }
}
