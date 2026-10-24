package com.arielelbert.vpassignmentweek5soal2.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arielelbert.vpassignmentweek5soal2.model.State
import com.arielelbert.vpassignmentweek5soal2.viewmodel.QuizViewModel

val PrimaryDark = Color(0xFF4C5980)
val LightCardColor = Color(0xFFE8EEF5)
val WrongRedColor = Color(0xFFD9534F)

@Composable
fun quizScreen(viewModel: QuizViewModel = viewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (viewModel.screenState) {
            State.START -> StartQuizContent(viewModel)
            State.QUIZ -> QuizContent(viewModel)
            State.RESULT -> ResultContent(viewModel)
        }
    }
}

@Composable
fun StartQuizContent(viewModel: QuizViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Mini Quiz",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "How many questions do you want? (1 - 5)",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = viewModel.inputQuestionCount,
            onValueChange = {
                viewModel.inputQuestionCount = it
                if (viewModel.errorMessage != null) viewModel.errorMessage = null
            },
            label = { Text("Number of questions") },
            isError = viewModel.errorMessage != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        if (viewModel.errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = viewModel.errorMessage!!,
                color = WrongRedColor,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.startQuiz() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Start Quiz", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun QuizContent(viewModel: QuizViewModel) {
    val question = viewModel.currentQuestion ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Question ${viewModel.currentQuestionIndex + 1}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = question.text,
            fontSize = 16.sp,
            color = Color.DarkGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        question.options.forEachIndexed { index, optionText ->
            val isSelected = viewModel.selectedAnswerIndex == index
            Button(
                onClick = { viewModel.selectAnswer(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) PrimaryDark else LightCardColor,
                    contentColor = if (isSelected) Color.White else Color.Black
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = optionText, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.nextQuestion() },
            enabled = viewModel.selectedAnswerIndex != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryDark,
                disabledContainerColor = Color.LightGray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Next", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun ResultContent(viewModel: QuizViewModel) {
    val wrongQuestions = viewModel.WrongAnsweredQuestions()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "Quiz Finished!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your Score: ${viewModel.score} / ${viewModel.selectedQuestions.size}",
            fontSize = 18.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (wrongQuestions.isEmpty()) {
            Text(
                text = "Perfect! No wrong answers.",
                color = PrimaryDark,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(wrongQuestions) { (question, userAnswer, correctAnswer) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Q${question.id}: ${question.text}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Your Answer: $userAnswer",
                            color = WrongRedColor,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Correct Answer: $correctAnswer",
                            color = PrimaryDark,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.restartQuiz() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Restart Quiz", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    quizScreen()
}