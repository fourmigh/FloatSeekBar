package org.caojun.library.floatseekbar

import android.view.Gravity
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import android.widget.SeekBar
import androidx.compose.runtime.Composable

@Composable
fun LabeledSeekBarCompose(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: FloatRange = FloatRange.DEFAULT,
    labelUnit: String = "",
    showLabel: Boolean = true,
    labelPosition: Int = Gravity.START,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {

    AndroidView(
        factory = { context ->
            LabeledSeekBar(context).apply {
                // 设置范围
                setRange(valueRange.min, valueRange.max)

                setShowLabel(showLabel)

                setLabelUnit(labelUnit)

                // 设置标签位置
                setLabelPosition(labelPosition)

                setProgress(value)

                // 设置监听器
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (fromUser) {
                            val newValue = getProgressFloat()
                            onValueChange(newValue)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        // 可以在这里添加开始触摸的回调
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        // 可以在这里添加结束触摸的回调
                    }
                })
            }
        },
        modifier = modifier,
        update = { seekBar ->
            // 当外部value变化时更新（避免循环更新）
            if (seekBar.getProgressFloat() != value) {
                seekBar.setProgress(value)
            }

            // 更新启用状态
            seekBar.isEnabled = enabled
        }
    )
}