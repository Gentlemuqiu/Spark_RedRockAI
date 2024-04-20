package com.example.api.orc.dataBean

/**
 *  author : 29364
 *  date: 2024/4/19 12:39
 *  version : 1.0
 *  description :
 */
data class JsonParse(
    var header: Header,
    var payload: Payload
)

data class Header(
    var code: Int,
    var message: String,
    var sid: String
)

data class Payload(
    var recognizeDocumentRes: Result
)

data class Result(
    var compress: String,
    var encoding: String,
    var format: String,
    var text: String
)
