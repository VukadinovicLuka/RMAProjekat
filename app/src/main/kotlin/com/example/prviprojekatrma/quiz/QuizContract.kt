package com.example.prviprojekatrma.quiz

import com.example.prviprojekatrma.quiz.questions.Question

interface QuizContract {

    data class QuizState(
        val questions: List<Question> = emptyList(),
        val currentQuestionIndex: Int = 0,
        val score: Int = 0,
        val isQuizFinished: Boolean = false,
        val timeRemaining: Long = 300L, // 5 minutes
        val error: QuizError? = null // Dodato polje za greške
    )

    sealed class QuizUiEvent {
        data object LoadQuestions : QuizUiEvent()
        data class SubmitAnswer(val answer: String) : QuizUiEvent()
        data object ResetQuiz : QuizUiEvent()
        data object PublishScore : QuizUiEvent()
        data object FinishQuiz : QuizUiEvent()
        data object StartQuizTimer : QuizUiEvent()
    }

    sealed class QuizError {
        data class LoadQuestionsFailed(val cause: Throwable? = null) : QuizError()
        data class SubmitAnswerFailed(val cause: Throwable? = null) : QuizError()
        data class ResetQuizFailed(val cause: Throwable? = null) : QuizError()
        data class PublishScoreFailed(val cause: Throwable? = null) : QuizError()
        data class FinishQuizFailed(val cause: Throwable? = null) : QuizError()
    }
}
