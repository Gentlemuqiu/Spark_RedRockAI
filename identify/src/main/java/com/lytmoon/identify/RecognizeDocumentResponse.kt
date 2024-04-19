package com.lytmoon.identify

data class RecognizeDocumentResponse(
    val header: Header1,
    val payload: Payload1
)

data class Header1(
    val code: Int,
    val message: String,
    val sid: String
)

data class Payload1(
    val recognizeDocumentRes: RecognizeDocumentRes1
)

data class RecognizeDocumentRes1(
    val compress: String,
    val encoding: String,
    val format: String,
    val text: String
)