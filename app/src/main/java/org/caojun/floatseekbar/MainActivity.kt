package org.caojun.floatseekbar

import android.os.Bundle
import android.view.Gravity
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.caojun.floatseekbar.ui.theme.FloatSeekBarTheme
import org.caojun.library.floatseekbar.FloatRange
import org.caojun.library.floatseekbar.LabeledSeekBarCompose

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FloatSeekBarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var temperature by remember { mutableFloatStateOf(36.5f) }
    var volume by remember { mutableFloatStateOf(75f) }
    var brightness by remember { mutableFloatStateOf(0.5f) }
    var percentage by remember { mutableFloatStateOf(0.25f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Float SeekBar 示例 (View版本)",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // 示例1: 温度调节
        Column {
            Text(
                text = "温度调节TOP: 35.0 -> 42.9",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Red
            )
            LabeledSeekBarCompose(
                value = temperature,
                onValueChange = { temperature = it },
                valueRange = FloatRange("35.0", "42.9"),
                labelUnit = "°C",
                labelPosition = Gravity.TOP,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 示例2: 音量调节
        Column {
            Text(
                text = "音量调节RIGHT: 0 -> 100",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Blue
            )
            LabeledSeekBarCompose(
                value = volume,
                onValueChange = { volume = it },
                valueRange = FloatRange("0", "100"),
                labelUnit = "%",
                labelPosition = Gravity.RIGHT,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 示例3: 亮度调节
        Column {
            Text(
                text = "亮度调节BOTTOM: 0 -> 1.00",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF4CAF50)
            )
            LabeledSeekBarCompose(
                value = brightness,
                onValueChange = { brightness = it },
                valueRange = FloatRange("0", "1.00"),
                labelPosition = Gravity.BOTTOM,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 示例4: 百分比（高精度）
        Column {
            Text(
                text = "高精度百分比LEFT: 0 - 1.000",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF9C27B0)
            )
            LabeledSeekBarCompose(
                value = percentage,
                onValueChange = { percentage = it },
                valueRange = FloatRange("0", "1.000"),
                labelPosition = Gravity.LEFT,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 显示当前所有值
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "当前数值:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "温度: ${"%.1f".format(temperature)}°C")
                Text(text = "音量: ${"%.0f".format(volume)}%")
                Text(text = "亮度: ${"%.2f".format(brightness)}")
                Text(text = "百分比: ${"%.3f".format(percentage)}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FloatSeekBarTheme {
        Greeting("Android")
    }
}