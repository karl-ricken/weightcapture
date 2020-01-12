package com.ricken.weightcaptureapplication.database.table

class TWeight : TableId("data") {
    @JvmField
    val scale = "scale"
    @JvmField
    val time = "time"
    @JvmField
    val value = "value"
}