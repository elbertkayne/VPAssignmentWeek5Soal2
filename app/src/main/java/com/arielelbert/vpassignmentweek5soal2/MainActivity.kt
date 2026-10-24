package com.arielelbert.vpassignmentweek5soal2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arielelbert.vpassignmentweek5soal2.view.quizScreen
import com.arielelbert.vpassignmentweek5soal2.ui.theme.VPAssignmentWeek5Soal2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VPAssignmentWeek5Soal2Theme() {
                quizScreen()
            }
        }
    }
}