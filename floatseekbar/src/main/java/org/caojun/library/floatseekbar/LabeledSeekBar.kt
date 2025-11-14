package org.caojun.library.floatseekbar

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import kotlin.math.pow
import kotlin.math.roundToInt

class LabeledSeekBar : LinearLayout {
    private lateinit var tvLabel: TextView
    private lateinit var seekBar: SeekBar
    private var labelFormat = "%s"
    private var showLabel = true
    private var labelUnit = ""

    // 获取小数位数（只读）
    private var decimalDigits: Int = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        // 设置垂直方向布局
        orientation = VERTICAL

        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.labeled_seek_bar_layout, this, true)

        // 初始化视图
        tvLabel = findViewById(R.id.tvLabel)
        seekBar = findViewById(R.id.seekBar)

        // 设置SeekBar监听器
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                p0: SeekBar?,
                p1: Int,
                p2: Boolean
            ) {
                updateLabelText()
                changeListener?.onProgressChanged(p0, p1, p2)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                updateLabelText()
                changeListener?.onStartTrackingTouch(p0)
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                updateLabelText()
                changeListener?.onStopTrackingTouch(p0)
            }

        })

        setLabelPosition(Gravity.START)

        // 初始化标签文本
        updateLabelText()
    }

    // 获取单个数值的小数位数
    private fun getDecimalDigits(numberStr: String): Int {
        val dotIndex = numberStr.indexOf('.')
        if (dotIndex == -1) return 0

        val decimalPart = numberStr.substring(dotIndex + 1)
        return decimalPart.length
    }

    private val scaleFactor: Int
        // 获取缩放因子
        get() = 10.0.pow(decimalDigits.toDouble()).toInt()

    private fun updateLabelText() {
        val value = intToFloat(getProgress())
        val formattedValue = formatFloatValue(value)
        val text = if (labelFormat.contains("%s")) {
            String.format(labelFormat, formattedValue)
        } else if (labelFormat.contains("%f") || labelFormat.contains("%.")) {
            String.format(labelFormat, value)
        } else {
            String.format(labelFormat, formattedValue)
        }

        tvLabel.text = text
    }

    private fun formatFloatValue(value: Float): String {
        return if (decimalDigits == 0) {
            value.roundToInt().toString()
        } else {
            String.format("%." + decimalDigits + "f", value)
        }
    }

    // 整数转浮点数
    private fun intToFloat(progress: Int): Float {
        return progress / scaleFactor.toFloat()
    }

    // 浮点数转整数进度
    private fun floatToInt(float: Float): Int {
        return (float * scaleFactor).toInt()
    }

    fun setLabelPosition(gravity: Int) {
        when (gravity) {
            Gravity.TOP -> {
                orientation = VERTICAL
                removeAllViews()
                addView(tvLabel)
                addView(seekBar)
                tvLabel.gravity = Gravity.CENTER_HORIZONTAL
            }

            Gravity.BOTTOM -> {
                orientation = VERTICAL
                removeAllViews()
                addView(seekBar)
                addView(tvLabel)
                tvLabel.gravity = Gravity.CENTER_HORIZONTAL
            }

            Gravity.START, Gravity.LEFT -> {
                orientation = HORIZONTAL
                removeAllViews()
                addView(tvLabel)
                addView(seekBar)
                tvLabel.gravity = Gravity.CENTER_VERTICAL
            }

            Gravity.END, Gravity.RIGHT -> {
                orientation = HORIZONTAL
                removeAllViews()
                addView(seekBar)
                addView(tvLabel)
                tvLabel.gravity = Gravity.CENTER_VERTICAL
                seekBar.layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT).apply {
                    weight = 1f
                }
                tvLabel.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                    weight = 0f
                }
            }
        }
    }

    fun setRange(minStr: String, maxStr: String) {
        val mMin = minStr.toFloatOrNull() ?: return
        val mMax = maxStr.toFloatOrNull() ?: return

        val min = mMin.coerceAtMost(mMax)
        val max = mMin.coerceAtLeast(mMax)

        // 自动计算小数位数
        decimalDigits = maxOf(
            getDecimalDigits(minStr),
            getDecimalDigits(maxStr)
        )
        // 先设min的话，如果大于max，min就是max
        seekBar.max = floatToInt(max)
        seekBar.min = floatToInt(min)

        updateLabelText()
    }

    fun setLabelUnit(unit: String) {
        labelUnit = if (unit == "%") {
            "%%"
        } else {
            unit
        }

        labelFormat = "%.${decimalDigits}f$labelUnit"
        updateLabelText()
    }

    fun getProgress(): Int {
        return seekBar.progress
    }
    fun getProgressFloat(): Float {
        return intToFloat(getProgress())
    }

    fun setProgress(progress: Float) {
        setProgress(floatToInt(progress))
    }
    fun setProgress(progress: Int) {
        seekBar.progress = progress
        updateLabelText()
    }

    fun setShowLabel(show: Boolean) {
        showLabel = show
        tvLabel.visibility = if (show) VISIBLE else GONE
    }

    private var changeListener: SeekBar.OnSeekBarChangeListener? = null

    fun setOnSeekBarChangeListener(listener: SeekBar.OnSeekBarChangeListener?) {
        this.changeListener = listener
    }
}