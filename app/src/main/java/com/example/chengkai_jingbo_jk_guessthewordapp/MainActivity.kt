package com.example.chengkai_jingbo_jk_guessthewordapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.Canvas
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.CoroutineScope
import com.example.chengkai_jingbo_jk_guessthewordapp.ui.theme.ChengkaiJingboJKGuessTheWordAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChengkaiJingboJKGuessTheWordAppTheme {
                GuessTheWordGame()
            }
        }
    }
}

@Composable
fun GuessTheWordGame() {
    var currentWord by rememberSaveable { mutableStateOf("APPLE") }
    var guessedLetters by rememberSaveable { mutableStateOf(listOf<Char>()) }
    var remainingTurns by rememberSaveable { mutableIntStateOf(6) }
    var remainingHits by rememberSaveable { mutableIntStateOf(3) }
    var showHint by rememberSaveable { mutableStateOf(false) }
    var vowelsShown by rememberSaveable { mutableStateOf(true) }
    var disabledLetters by rememberSaveable { mutableStateOf(listOf<Char>()) }
    var hintMessage by rememberSaveable { mutableStateOf("") }  // Store the hint message

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        BoxWithConstraints(modifier = Modifier.padding(paddingValues)) {
            val columns = if (maxWidth > maxHeight) 7 else 4 // Use 7 columns in landscape, 4 in portrait

            if (maxWidth > maxHeight) {
                // Landscape mode layout
                LandscapeLayout(
                    currentWord = currentWord,
                    guessedLetters = guessedLetters,
                    remainingTurns = remainingTurns,
                    showHint = showHint,
                    disabledLetters = disabledLetters,
                    vowelsShown = vowelsShown,
                    hintMessage = hintMessage,  // Pass hint message
                    onLetterSelected = { letter ->
                        guessedLetters = guessedLetters + letter
                        if (!currentWord.contains(letter)) {
                            remainingTurns -= 1
                        }
                        checkGameOver(
                            currentWord = currentWord,
                            guessedLetters = guessedLetters,
                            remainingTurns = remainingTurns,
                            snackbarHostState = snackbarHostState,
                            scope = scope,
                            onNewGame = {
                                guessedLetters = listOf()
                                remainingTurns = 6
                                remainingHits = 3
                                showHint = false
                                vowelsShown = true
                                disabledLetters = listOf()
                                hintMessage = ""  // Reset hint message
                            }
                        )
                    },
                    onHintClick = {
                        HandleHintClick(
                            currentWord = currentWord,
                            guessedLetters = guessedLetters,
                            remainingTurns = remainingTurns,
                            remainingHits = remainingHits,
                            vowelsShown = vowelsShown,
                            snackbarHostState = snackbarHostState,
                            scope = scope,
                            onRemainingHitsUpdate = { remainingHits = it },
                            onRemainingTurnsUpdate = { remainingTurns = it },
                            onDisabledLettersUpdate = { disabledLetters = disabledLetters + it },
                            onShowHintUpdate = { showHint = it },
                            onGuessedLettersUpdate = { guessedLetters = it },
                            onHintMessageUpdate = { hintMessage = it }  // Update hint message
                        )
                    },
                    onNewGame = {
                        guessedLetters = listOf()
                        remainingTurns = 6
                        remainingHits = 3
                        showHint = false
                        vowelsShown = true
                        disabledLetters = listOf()
                        hintMessage = ""  // Reset hint message
                    },
                    columns = columns
                )
            } else {
                // Portrait mode layout
                PortraitLayout(
                    currentWord = currentWord,
                    guessedLetters = guessedLetters,
                    remainingTurns = remainingTurns,
                    showHint = showHint,
                    disabledLetters = disabledLetters,
                    vowelsShown = vowelsShown,
                    onLetterSelected = { letter ->
                        guessedLetters = guessedLetters + letter
                        if (!currentWord.contains(letter)) {
                            remainingTurns -= 1
                        }
                        checkGameOver(
                            currentWord = currentWord,
                            guessedLetters = guessedLetters,
                            remainingTurns = remainingTurns,
                            snackbarHostState = snackbarHostState,
                            scope = scope,
                            onNewGame = {
                                guessedLetters = listOf()
                                remainingTurns = 6
                                remainingHits = 3
                                showHint = false
                                vowelsShown = true
                                disabledLetters = listOf()
                            }
                        )
                    },
                    onHintClick = {
                        HandleHintClick(
                            currentWord = currentWord,
                            guessedLetters = guessedLetters,
                            remainingTurns = remainingTurns,
                            remainingHits = remainingHits,
                            vowelsShown = vowelsShown,
                            snackbarHostState = snackbarHostState,
                            scope = scope,
                            onRemainingHitsUpdate = { remainingHits = it },
                            onRemainingTurnsUpdate = { remainingTurns = it },
                            onDisabledLettersUpdate = { disabledLetters = disabledLetters + it },
                            onShowHintUpdate = { showHint = it },
                            onGuessedLettersUpdate = { guessedLetters = it },
                            onHintMessageUpdate = { hintMessage = it }  // Update hint message
                        )
                    },
                    onNewGame = {
                        guessedLetters = listOf()
                        remainingTurns = 6
                        remainingHits = 3
                        showHint = false
                        vowelsShown = true
                        disabledLetters = listOf()
                    },
                    columns = columns
                )
            }
        }
    }
}

@Composable
fun PortraitLayout(
    currentWord: String,
    guessedLetters: List<Char>,
    remainingTurns: Int,
    showHint: Boolean,
    disabledLetters: List<Char>,
    vowelsShown: Boolean,
    onLetterSelected: (Char) -> Unit,
    onHintClick: () -> Unit,
    onNewGame: () -> Unit,
    columns: Int  // Dynamically set number of columns based on orientation
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(6.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        HangmanCanvas(remainingTurns = remainingTurns)
        Spacer(modifier = Modifier.height(16.dp))

        WordDisplay(word = currentWord, guessedLetters = guessedLetters)
        Spacer(modifier = Modifier.height(48.dp))

        Text(text = "CHOOSE A LETTER", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        LetterSelectionPanel(
            guessedLetters = guessedLetters,
            disabledLetters = disabledLetters,
            vowelsShown = vowelsShown,
            onLetterSelected = onLetterSelected,
            columns = columns
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { onNewGame() }) {
                Text("New Game")
            }
        }
    }
}

@Composable
fun LandscapeLayout(
    currentWord: String,
    guessedLetters: List<Char>,
    remainingTurns: Int,
    showHint: Boolean,
    disabledLetters: List<Char>,
    vowelsShown: Boolean,
    hintMessage: String,  // Receive hint message
    onLetterSelected: (Char) -> Unit,
    onHintClick: () -> Unit,
    onNewGame: () -> Unit,
    columns: Int  // Dynamically set number of columns based on orientation
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f)
                .padding(4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "CHOOSE A LETTER",
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                style = MaterialTheme
                    .typography
                    .headlineSmall,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            LetterSelectionPanel(
                guessedLetters = guessedLetters,
                disabledLetters = disabledLetters,
                vowelsShown = vowelsShown,
                onLetterSelected = onLetterSelected,
                columns = columns
            )

            Row(
                modifier = Modifier
                    .padding(4.dp)
            ) {
                Button(onClick = { onHintClick() }) {
                    Text("Hint")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = { onNewGame() }) {
                    Text("New Game")
                }
            }

            Text(text = hintMessage.ifEmpty { " " },
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
        }

        // HangmanCanvas takes less space with weight(1f)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HangmanCanvas(remainingTurns = remainingTurns)
            Spacer(modifier = Modifier.height(16.dp))

            WordDisplay(word = currentWord, guessedLetters = guessedLetters)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun LetterSelectionPanel(
    guessedLetters: List<Char>,
    disabledLetters: List<Char>,
    vowelsShown: Boolean,
    onLetterSelected: (Char) -> Unit,
    columns: Int  // Dynamically set number of columns based on orientation
) {
    val alphabet = ('A'..'Z').toList()
    val vowels = listOf('A', 'E', 'I', 'O', 'U')

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),  // Use dynamic columns value
        modifier = Modifier.fillMaxWidth()
    ) {
        items(alphabet) { letter ->
            val isDisabled = guessedLetters.contains(letter) || disabledLetters.contains(letter)
            val shouldShowLetter = if (vowelsShown) true else !vowels.contains(letter)

            Button(
                onClick = { onLetterSelected(letter) },
                modifier = Modifier.padding(2.dp),
                enabled = !isDisabled && shouldShowLetter
            ) {
                Text(letter.toString())
            }
        }
    }
}

@Composable
fun WordDisplay(word: String, guessedLetters: List<Char>) {
    val displayText = word.map { if (guessedLetters.contains(it)) it else '_' }.joinToString(" ")
    Text(text = displayText, style = MaterialTheme.typography.headlineSmall)
}

@Composable
fun HangmanCanvas(remainingTurns: Int) {
    Canvas(modifier = Modifier.size(200.dp)) {
        val strokeWidth = 8f

        drawLine(
            color = Color.Black,
            start = Offset(50f, 50f),
            end = Offset(50f, 300f),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = Color.Black,
            start = Offset(50f, 50f),
            end = Offset(150f, 50f),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = Color.Black,
            start = Offset(150f, 50f),
            end = Offset(150f, 100f),
            strokeWidth = strokeWidth
        )

        if (remainingTurns <= 5) {
            drawCircle(
                color = Color.Black,
                center = Offset(150f, 130f),
                radius = 30f,
                style = Stroke(width = strokeWidth)
            )
        }
        if (remainingTurns <= 4) {
            drawLine(
                color = Color.Black,
                start = Offset(150f, 160f),
                end = Offset(150f, 240f),
                strokeWidth = strokeWidth
            )
        }
        if (remainingTurns <= 3) {
            drawLine(
                color = Color.Black,
                start = Offset(150f, 180f),
                end = Offset(110f, 220f),
                strokeWidth = strokeWidth
            )
        }
        if (remainingTurns <= 2) {
            drawLine(
                color = Color.Black,
                start = Offset(150f, 180f),
                end = Offset(190f, 220f),
                strokeWidth = strokeWidth
            )
        }
        if (remainingTurns <= 1) {
            drawLine(
                color = Color.Black,
                start = Offset(150f, 240f),
                end = Offset(110f, 290f),
                strokeWidth = strokeWidth
            )
        }
        if (remainingTurns <= 0) {
            drawLine(
                color = Color.Black,
                start = Offset(150f, 240f),
                end = Offset(190f, 290f),
                strokeWidth = strokeWidth
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
fun HandleHintClick(
    currentWord: String,
    guessedLetters: List<Char>,
    remainingTurns: Int,
    remainingHits: Int,
    vowelsShown: Boolean,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onRemainingHitsUpdate: (Int) -> Unit,
    onRemainingTurnsUpdate: (Int) -> Unit,
    onDisabledLettersUpdate: (List<Char>) -> Unit,
    onShowHintUpdate: (Boolean) -> Unit,
    onGuessedLettersUpdate: (List<Char>) -> Unit,
    onHintMessageUpdate: (String) -> Unit  // New callback for updating hint message
) {
    if (remainingHits <= 0 || remainingTurns <= 1) {
        scope.launch {
            snackbarHostState.showSnackbar("Hint not available")
        }
    } else {
        when (remainingHits) {
            3 -> {
                onHintMessageUpdate("Hint 1: food")  // Update hint message instead of using Snackbar
                onShowHintUpdate(true)
            }
            2 -> {
                val remainingWrongLetters = ('A'..'Z').filterNot { it in currentWord || it in guessedLetters }
                val lettersToDisable = remainingWrongLetters.shuffled().take(remainingWrongLetters.size / 2)

                onDisabledLettersUpdate(lettersToDisable)
                onRemainingTurnsUpdate(remainingTurns - 1)
                onHintMessageUpdate("Hint 2: Half of the wrong letters are disabled")
            }
            1 -> {
                val vowels = listOf('A', 'E', 'I', 'O', 'U')
                val vowelsInWord = currentWord.filter { vowels.contains(it) }.toList()

                onGuessedLettersUpdate(guessedLetters + vowelsInWord)
                onDisabledLettersUpdate(vowels)
                onRemainingTurnsUpdate(remainingTurns - 1)
                onHintMessageUpdate("Hint 3: All vowels in the word are revealed")
            }
        }
        onRemainingHitsUpdate(remainingHits - 1)
    }
}

fun checkGameOver(
    currentWord: String,
    guessedLetters: List<Char>,
    remainingTurns: Int,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    onNewGame: () -> Unit
) {
    if (remainingTurns <= 0) {
        scope.launch {
            snackbarHostState.showSnackbar("❌ You lost! The word was \"$currentWord\"!")
        }
        onNewGame()
    } else if (currentWord.all { guessedLetters.contains(it) }) {
        scope.launch {
            snackbarHostState.showSnackbar("✅ Congratulations! You guessed the word!")
        }
        onNewGame()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChengkaiJingboJKGuessTheWordAppTheme {
        GuessTheWordGame()
    }
}
