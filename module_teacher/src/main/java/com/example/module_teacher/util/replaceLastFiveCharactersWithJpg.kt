package com.example.module_teacher.util

fun replaceLastFiveCharactersWithJpg(input: String): String {
    return if (input.length >= 5) {
        input.substring(0, input.length - 5) + ".jpg"
    } else {
        // 如果字符串长度不足五个字符，则直接返回原字符串
        input
    }
}
fun replaceLastFiveCharactersWithMp4(input: String): String {
    return if (input.length >= 5) {
        input.substring(0, input.length - 5) + ".mp4"
    } else {
        // 如果字符串长度不足五个字符，则直接返回原字符串
        input
    }
}
