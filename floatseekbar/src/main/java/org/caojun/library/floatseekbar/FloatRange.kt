package org.caojun.library.floatseekbar

data class FloatRange(
    val min: String,
    val max: String
) {
    companion object {
        val DEFAULT = FloatRange("0", "100")
    }
}