package co.yml.ycharts.app.ui.compositions

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.yml.ycharts.app.R
import co.yml.ycharts.app.ui.theme.YChartsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarWithBackButton(title: String, onBackPressed: () -> Unit) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(elevation = 6.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = YChartsTheme.colors.button,
        ),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.width(5.dp))
                    IconButton(
                        onClick = { onBackPressed() },
                        modifier = Modifier
                            .size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_arrow),
                            contentDescription = "Back",
                            tint = Color.Unspecified
                        )
                    }
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    color = YChartsTheme.colors.text,
                    textAlign = TextAlign.Center,
                    style = YChartsTheme.typography.header
                )
            }
        },
        actions = {

        }
    )
}
