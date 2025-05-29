package com.example.prviprojekatrma.quiz.questions

data class Question(
    val id: Int,
    val questionType: QuestionType,
    val imageUrl: String,
    val options: List<String>,
    val correctAnswer: String,
    val text : String
)