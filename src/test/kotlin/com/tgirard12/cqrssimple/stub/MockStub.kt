package com.tgirard12.cqrssimple.stub

interface MockStub {

    val mockFun: MutableList<String>
    val mockData: MutableList<Any>
    val mockTime: MutableList<Long>
}
