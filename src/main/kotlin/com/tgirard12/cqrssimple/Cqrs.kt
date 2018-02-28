package com.tgirard12.cqrssimple

interface Handler {
    val name: String? get() = this::class.simpleName
}
