package com.example.prviprojekatrma.quiz

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prviprojekatrma.auth.AuthStore
import com.example.prviprojekatrma.breeds.repository.BreedsRepository
import com.example.prviprojekatrma.leaderboard.repository.LeaderboardRepository
import com.example.prviprojekatrma.quiz.questions.Question
import com.example.prviprojekatrma.quiz.questions.QuestionType
import com.example.prviprojekatrma.score.db.ScoreData
import com.example.prviprojekatrma.score.repository.ScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: BreedsRepository,
    private val authStore: AuthStore,
    private val scoreRepository: ScoreRepository,
    private val leaderboardRepository: LeaderboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QuizContract.QuizState())
    val state = _state.asStateFlow()
    private fun setState(reducer: QuizContract.QuizState.() -> QuizContract.QuizState) =
        _state.update(reducer)

    private val events = MutableSharedFlow<QuizContract.QuizUiEvent>()
    fun setEvent(event: QuizContract.QuizUiEvent) = viewModelScope.launch { events.emit(event) }

    private var isPublished = false

    init {
        observeEvents()
        loadQuestions()
        startQuizTimer()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    QuizContract.QuizUiEvent.LoadQuestions -> loadQuestions()
                    is QuizContract.QuizUiEvent.SubmitAnswer -> submitAnswer(it.answer)
                    QuizContract.QuizUiEvent.ResetQuiz -> resetQuiz()
                    QuizContract.QuizUiEvent.PublishScore -> publishScore()
                    QuizContract.QuizUiEvent.FinishQuiz -> finishQuiz()
                    QuizContract.QuizUiEvent.StartQuizTimer -> startQuizTimer()
                }
            }
        }
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            try {
                val questions = mutableListOf<Question>()

                // Generate 20 random questions
                for (i in 1..20) {
                    val questionType = QuestionType.values().random()
                    when (questionType) {
                        QuestionType.GUESS_THE_BREED -> {
                            val breedImage = repository.getRandomBreedImage()
                            val breedData = repository.getBreedById(breedImage.breedId)
                            val incorrectAnswers = repository.getRandomBreedNames(exclude = breedData.name, count = 3)
                            questions.add(
                                Question(
                                    id = i,
                                    questionType = QuestionType.GUESS_THE_BREED,
                                    imageUrl = breedImage.imageUrl,
                                    options = (incorrectAnswers + breedData.name).shuffled(),
                                    correctAnswer = breedData.name,
                                    text = "Guess the breed: "
                                )
                            )
                        }
                        QuestionType.PICK_THE_ODD_ONE -> {
                            val breedImage = repository.getRandomBreedImage()
                            val breedData = repository.getBreedById(breedImage.breedId)
                            val incorrectTemperament = repository.getRandomTemperament(exclude = breedData.temperament.split(", "))
                            questions.add(
                                Question(
                                    id = i,
                                    questionType = QuestionType.PICK_THE_ODD_ONE,
                                    imageUrl = breedImage.imageUrl,
                                    options = (breedData.temperament.split(", ").take(3) + incorrectTemperament).shuffled(),
                                    correctAnswer = incorrectTemperament,
                                    text = "Pick the odd one: "
                                )
                            )
                        }
                        QuestionType.GUESS_THE_TEMPERAMENT -> {
                            val breedImage = repository.getRandomBreedImage()
                            val breedData = repository.getBreedById(breedImage.breedId)
                            val correctTemperament = breedData.temperament.split(", ").random()
                            val incorrectTemperaments = repository.getRandomTemperaments(exclude = breedData.temperament.split(", "), count = 3)
                            questions.add(
                                Question(
                                    id = i,
                                    questionType = QuestionType.GUESS_THE_TEMPERAMENT,
                                    imageUrl = breedImage.imageUrl,
                                    options = (incorrectTemperaments + correctTemperament).shuffled(),
                                    correctAnswer = correctTemperament,
                                    text = "Guess the temperament: "
                                )
                            )
                        }
                    }
                }

                questions.shuffle()  // Shuffle questions for randomness
                _state.value = _state.value.copy(questions = questions)
            } catch (e: Exception) {
                Log.e("QuizViewModel", "Error loading questions", e)
                setState {
                    copy(error = QuizContract.QuizError.LoadQuestionsFailed(cause = e))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitAnswer(answer: String) {
        viewModelScope.launch {
            try {
                val currentQuestion = _state.value.questions[_state.value.currentQuestionIndex]
                val isCorrect = currentQuestion.correctAnswer == answer
                val newScore = if (isCorrect) _state.value.score + 1 else _state.value.score

                if (_state.value.currentQuestionIndex < _state.value.questions.size - 1) {
                    _state.value = _state.value.copy(
                        currentQuestionIndex = _state.value.currentQuestionIndex + 1,
                        score = newScore
                    )
                } else {
                    finalizeScore(newScore)
                }
            } catch (e: Exception) {
                setState {
                    copy(error = QuizContract.QuizError.SubmitAnswerFailed(cause = e))
                }
            }
        }
    }

    private fun finalizeScore(currentScore: Int) {
        val BTO = currentScore
        val MVT = 300
        val PVT = _state.value.timeRemaining
        val UBP = BTO * 2.5 * (1 + (PVT + 120) / MVT.toFloat()).coerceAtMost(100.0f)
        val finalScore = UBP.toInt()

        _state.value = _state.value.copy(
            score = finalScore,
            isQuizFinished = true,
        )
    }

    private fun startQuizTimer() {
        viewModelScope.launch {
            var timeRemaining = _state.value.timeRemaining
            while (timeRemaining > 0) {
                kotlinx.coroutines.delay(1000L)
                timeRemaining--
                _state.value = _state.value.copy(timeRemaining = timeRemaining)
            }
            if (timeRemaining == 0L) {
                finalizeScore(_state.value.score)
            }
        }
    }

    private fun resetQuiz() {
        _state.value = QuizContract.QuizState()
        loadQuestions()
        startQuizTimer()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun publishScore() {
        viewModelScope.launch {
            try {
                isPublished = true
                saveScoreData()
                leaderboardRepository.postResult(
                    authStore.authData.value.nickname.trim().replace("\\s".toRegex(), "_"),
                    _state.value.score.toDouble(), 1
                )
            } catch (e: Exception) {
                setState {
                    copy(error = QuizContract.QuizError.PublishScoreFailed(cause = e))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun finishQuiz() {
        viewModelScope.launch {
            try {
                isPublished = false
                saveScoreData()
            } catch (e: Exception) {
                setState {
                    copy(error = QuizContract.QuizError.FinishQuizFailed(cause = e))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun saveScoreData() {
        val scoreData = ScoreData(
            name = authStore.authData.value.name,
            lastname = authStore.authData.value.lastname,
            email = authStore.authData.value.email,
            nickname = authStore.authData.value.nickname,
            score = _state.value.score,
            attemptedAt = LocalDateTime.now().toString(), // Konvertovanje u String
            isPublished = isPublished
        )
        scoreRepository.storeScore(scoreData)
    }
}
