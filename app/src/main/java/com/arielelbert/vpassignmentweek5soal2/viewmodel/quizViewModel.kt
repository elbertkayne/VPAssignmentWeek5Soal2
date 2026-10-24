package com.arielelbert.vpassignmentweek5soal2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.arielelbert.vpassignmentweek5soal2.model.quiz
import com.arielelbert.vpassignmentweek5soal2.model.State

class QuizViewModel : ViewModel() {
    private val allQuestions = listOf(
        quiz(1, "Ibu Kota Di Indonesia?", listOf("Surabaya", "Bandung", "Jakarta", "Medan"), 2),
        quiz(2, "Ada berapa hari dalam seminggu?", listOf("5", "6", "7", "8"), 2),
        quiz(3, "Mata Kuliah VP pakai apa?", listOf("Word", "Android Studio", "Excel", "Photoshop"), 1),
        quiz(4, "Apakah Mata Kuliah VP susah?", listOf("Sangat Sulit", "Sulit", "Biasa Saja", "Sangat Mudah"), 3),
        quiz(5, "Natal dirayakan pada bulan?", listOf("Agustus", "Oktober", "Desember", "Januari"), 2)
    )

    var screenState by mutableStateOf(State.START)
    var inputQuestionCount by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)

    var selectedQuestions by mutableStateOf<List<quiz>>(emptyList())
    var currentQuestionIndex by mutableStateOf(0)
    var selectedAnswerIndex by mutableStateOf<Int?>(null)
    private var userAnswers = mutableMapOf<Int, Int>()

    val currentQuestion: quiz?
        get() = selectedQuestions.getOrNull(currentQuestionIndex)

    val score: Int
        get() = userAnswers.count { (qIndex, aIndex) ->
            selectedQuestions[qIndex].correctAnswerIndex == aIndex
        }

    fun startQuiz() {
        val count = inputQuestionCount.toIntOrNull()
        if (count == null || count <= 0 || count > allQuestions.size) {
            errorMessage = "Maximum is ${allQuestions.size} questions"
            return
        }

        errorMessage = null
        selectedQuestions = allQuestions.shuffled().take(count)
        currentQuestionIndex = 0
        selectedAnswerIndex = null
        userAnswers.clear()
        screenState = State.QUIZ
    }

    fun selectAnswer(index: Int) {
        selectedAnswerIndex = index
    }

    fun nextQuestion() {
        val answerIndex = selectedAnswerIndex ?: return
        userAnswers[currentQuestionIndex] = answerIndex
        selectedAnswerIndex = null

        if (currentQuestionIndex + 1 < selectedQuestions.size) {
            currentQuestionIndex++
        } else {
            screenState = State.RESULT
        }
    }

    fun restartQuiz() {
        inputQuestionCount = ""
        errorMessage = null
        screenState = State.START
    }

    fun WrongAnsweredQuestions(): List<Triple<quiz, String, String>> {
        val wrongList = mutableListOf<Triple<quiz, String, String>>()
        selectedQuestions.forEachIndexed { index, question ->
            val userAnswerIdx = userAnswers[index]
            if (userAnswerIdx != question.correctAnswerIndex && userAnswerIdx != null) {
                wrongList.add(
                    Triple(
                        question,
                        question.options[userAnswerIdx],
                        question.options[question.correctAnswerIndex]
                    )
                )
            }
        }
        return wrongList
    }
}