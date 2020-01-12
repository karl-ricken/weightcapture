package com.ricken.weightcaptureapplication.database.`object`

import com.ricken.weightcaptureapplication.IdElement

class Scale : IdElement {
    var name: String? = null

    constructor()
    constructor(name: String?) {
        this.name = name
    }

    override fun toString(): String {
        return name!!
    }
}