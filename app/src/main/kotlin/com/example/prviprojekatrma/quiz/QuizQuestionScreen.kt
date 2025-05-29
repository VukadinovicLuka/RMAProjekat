import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import coil.compose.rememberImagePainter
import com.example.prviprojekatrma.animations.AnimatedQuestionContent
import com.example.prviprojekatrma.quiz.QuizContract
import com.example.prviprojekatrma.quiz.QuizViewModel

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.quiz(
    route: String,
    navController: NavController,
    onQuizFinish: () -> Unit
) = composable(
    route = route
) {
    val quizViewModel = hiltViewModel<QuizViewModel>()
    val quizState by quizViewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        showCancelDialog(
            navController = navController,
            onConfirm = {
                showDialog = false
                quizViewModel.setEvent(QuizContract.QuizUiEvent.ResetQuiz)
            },
            onDismiss = {
                showDialog = false
            }
        )
    }

    if (quizState.isQuizFinished) {
        QuizResultScreen(score = quizState.score, onQuizFinish = onQuizFinish, quizViewModel = quizViewModel)
    } else {
        QuizQuestionScreen(
            navController = navController,
            quizState = quizState,
            onAnswerSelected = { quizViewModel.setEvent(QuizContract.QuizUiEvent.SubmitAnswer(it)) },
            onQuizCancel = { showDialog = true }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuizQuestionScreen(
    navController: NavController,
    quizState: QuizContract.QuizState,
    onAnswerSelected: (String) -> Unit,
    onQuizCancel: () -> Unit
) {
    if (quizState.questions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val currentQuestion = quizState.questions[quizState.currentQuestionIndex]
    var visible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Time remaining: ${quizState.timeRemaining}s",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onQuizCancel) {
                Text(text = "Cancel Quiz")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Question ${quizState.currentQuestionIndex + 1}/${quizState.questions.size}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedQuestionContent(
            targetState = quizState.currentQuestionIndex
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = currentQuestion.text
                )
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberImagePainter(data = currentQuestion.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                currentQuestion.options.forEach { option ->
                    Button(
                        onClick = {
                            visible = false
                            onAnswerSelected(option)
                            visible = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = option)
                    }
                }
            }
        }
    }
}

@Composable
fun showCancelDialog(navController: NavController, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Cancel Quiz") },
        text = { Text("Are you sure you want to cancel the quiz? Your progress will be lost.") },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                navController.navigate("breeds")
            }) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QuizResultScreen(score: Int, onQuizFinish: () -> Unit, quizViewModel: QuizViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quiz Finished!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your Score: $score",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    quizViewModel.setEvent(QuizContract.QuizUiEvent.PublishScore)
                    onQuizFinish()
                },
                colors = ButtonDefaults.buttonColors(Color.Gray)
            ) {
                Text(text = "Publish")
            }
            Button(
                onClick = {
                    quizViewModel.setEvent(QuizContract.QuizUiEvent.FinishQuiz)
                    onQuizFinish()
                },
                colors = ButtonDefaults.buttonColors(Color.Green)
            ) {
                Text(text = "Finish")
            }
        }
    }
}
