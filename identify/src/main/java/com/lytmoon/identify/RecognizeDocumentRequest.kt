package com.lytmoon.identify

data class RequestParameterBody(
    val header: Header,
    val parameter: Parameter,
    val payload: Payload
)

data class Header(
    val app_id: String,
    val status: Int
)

data class Parameter(
    val hh_ocr_recognize_doc: OcrRecognizeDoc
)

data class OcrRecognizeDoc(
    val recognizeDocumentRes: RecognizeDocumentRes
)

data class RecognizeDocumentRes(
    val encoding: String,
    val compress: String,
    val format: String
)

data class Payload(
    val image: Image
)

data class Image(
    val encoding: String,
    val image: String,
    val status: Int
)

