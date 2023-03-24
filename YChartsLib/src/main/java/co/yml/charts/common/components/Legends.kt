package co.yml.charts.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.yml.charts.common.model.LegendLabel
import co.yml.charts.common.model.LegendsConfig
import kotlin.math.roundToInt

/**
 * Renders the list of legends in a grid format for given given grid column count
 * @param modifier: Defines the arrangements of ui compositions.
 * @param legendsConfig: Defines the configurations required for rendering legends in [LegendsConfig]
 */
@Composable
fun Legends(modifier: Modifier = Modifier, legendsConfig: LegendsConfig) {
    with(legendsConfig) {
        if (legendLabelList.isNotEmpty()) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = paddingHorizontal,
                        vertical = paddingVertical
                    ),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val legendsInColumn = legendLabelList.size/columnCount +
                        if (legendLabelList.size % columnCount > 0) 1 else 0
                var index = 0
                for (col in (1..columnCount)) {
                    Column(
                        modifier = Modifier.weight((1.0/columnCount).toFloat()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (i in (index until legendsInColumn * col)) {
                            if (i < legendLabelList.size) Legend(legendsConfig, legendLabelList[i])
                            index++
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Legend(config: LegendsConfig, legendLabel: LegendLabel) {
    Row(
        horizontalArrangement = config.legendsArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(config.colorBoxShape)
                .size(config.colorBoxSize)
                .background(legendLabel.color)
        )
        Spacer(modifier = Modifier.padding(config.spaceBWLabelAndColorBox))
        Text(
            text = legendLabel.name,
            style = config.textStyle,
            overflow = TextOverflow.Ellipsis,
            color = config.textColor
        )
    }
}

