package com.tqp.guideview

/**
 * @author  tangqipeng
 * @date  4/22/21 10:38 AM
 * @email tangqipeng@aograph.com
 */
enum class KeyBackEnum(s: String, i: Int) {
    DEFAULT("默认状态", 0),//默认状态下点击返回键会关闭所有的guide
    INVALID("无效状态", 1),//这种状态下返回按钮失效
    EFFECTIVE("有效状态", 2)//这种状态下按返回键的效果和提示窗口一致
}