package com.arielelbert.vpassignmentweek5soal2.model

data class quiz(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

enum class State {
    START,
    QUIZ,
    RESULT
}
