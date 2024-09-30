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
    var remainingTurns by rememberSaveable { mutableStateOf(6) }
    var remainingHits by rememberSaveable { mutableStateOf(3) }
    var showHint by rememberSaveable { mutableStateOf(false) }
    var vowelsShown by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        BoxWithConstraints(modifier = Modifier.padding(paddingValues)) {
            if (maxWidth < maxHeight) {
                PortraitLayout(
                    currentWord = currentWord,
                    guessedLetters = guessedLetters,
                    remainingTurns = remainingTurns,
                    showHint = showHint,
                    onLetterSelected = { letter ->
                        guessedLetters = guessedLetters + letter
                        if (!currentWord.contains(letter)) {
                            remainingTurns -= 1
                        }
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
                            onShowHintUpdate = { showHint = it },
                            onVowelsShownUpdate = { vowelsShown = it }
                        )
                    },
                    onNewGame = {
                        guessedLetters = listOf()
                        remainingTurns = 6
                        remainingHits = 3
                        showHint = false
                        vowelsShown = false
                    }
                )
            } else {
                LandscapeLayout(
                    currentWord = currentWord,
                    guessedLetters = guessedLetters,
                    remainingTurns = remainingTurns,
                    showHint = showHint,
                    onLetterSelected = { letter ->
                        guessedLetters = guessedLetters + letter
                        if (!currentWord.contains(letter)) {
                            remainingTurns -= 1
                        }
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
                            onShowHintUpdate = { showHint = it },
                            onVowelsShownUpdate = { vowelsShown = it }
                        )
                    },
                    onNewGame = {
                        guessedLetters = listOf()
                        remainingTurns = 6
                        remainingHits = 3
                        showHint = false
                        vowelsShown = false
                    }
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
    onLetterSelected: (Char) -> Unit,
    onHintClick: () -> Unit,
    onNewGame: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HangmanCanvas(remainingTurns = remainingTurns)
        Spacer(modifier = Modifier.height(16.dp))

        WordDisplay(word = currentWord, guessedLetters = guessedLetters)
        Spacer(modifier = Modifier.height(16.dp))

        LetterSelectionPanel(guessedLetters = guessedLetters, onLetterSelected = onLetterSelected)
        Spacer(modifier = Modifier.height(16.dp))

        Row( modifier = Modifier
            .fillMaxHeight()){
            Button(onClick = { onHintClick() }) {
                Text("Hint")
            }

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
    onLetterSelected: (Char) -> Unit,
    onHintClick: () -> Unit,
    onNewGame: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "CHOOSE A LETTER", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            LetterSelectionPanel(guessedLetters = guessedLetters, onLetterSelected = onLetterSelected)

            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

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

            Row( modifier = Modifier
                .fillMaxHeight()){
                Button(onClick = { onHintClick() }) {
                    Text("Hint")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = { onNewGame() }) {
                    Text("New Game")
                }
            }
        }
    }
}

@Composable
fun LetterSelectionPanel(guessedLetters: List<Char>, onLetterSelected: (Char) -> Unit) {
    val alphabet = ('A'..'Z').toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = Modifier.fillMaxWidth())
    {
        items(alphabet) { letter ->
            Button(
                onClick = { onLetterSelected(letter) },
                modifier = Modifier.padding(4.dp),
                enabled = !guessedLetters.contains(letter)
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
            // head
            drawCircle(
                color = Color.Black,
                center = Offset(150f, 130f),
                radius = 30f,
                style = Stroke(width = strokeWidth)
            )
        }
        if (remainingTurns <= 4) {
            // body
            drawLine(
                color = Color.Black,
                start = Offset(150f, 160f),
                end = Offset(150f, 240f),
                strokeWidth = strokeWidth
            )
        }
        if (remainingTurns <= 3) {
            // left arm
            drawLine(
                color = Color.Black,
                start = Offset(150f, 180f),
                end = Offset(110f, 220f),
                strokeWidth = strokeWidth
            )
        }
        if (remainingTurns <= 2) {
            // right arm
            drawLine(
                color = Color.Black,
                start = Offset(150f, 180f),
                end = Offset(190f, 220f),
                strokeWidth = strokeWidth
            )
        }
        if (remainingTurns <= 1) {
            // left leg
            drawLine(
                color = Color.Black,
                start = Offset(150f, 240f),
                end = Offset(110f, 290f),
                strokeWidth = strokeWidth
            )
        }
        if (remainingTurns <= 0) {
            // right leg
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
    snackbarHostState: SnackbarHostState, // 使用 SnackbarHostState
    scope: CoroutineScope,
    onRemainingHitsUpdate: (Int) -> Unit,
    onRemainingTurnsUpdate: (Int) -> Unit,
    onShowHintUpdate: (Boolean) -> Unit,
    onVowelsShownUpdate: (Boolean) -> Unit
) {

    if (remainingHits <= 0 || remainingTurns <= 1) {
        // 使用 Snackbar 替代 Toast
        scope.launch {
            snackbarHostState.showSnackbar("Hint not available")
        }
    } else {
        // 根据点击次数执行不同的提示
        when (remainingHits) {
            3 -> {
                onShowHintUpdate(true) // 第一次点击显示提示信息
            }
            2 -> {
                // 第二次点击禁用一半剩余的错误字母，并减少一次机会
                val wrongLetters = ('A'..'Z').filterNot { it in currentWord || it in guessedLetters }
                //TODO: Disable half of the remaining wrong letters
                // 随机禁用一半错误字母逻辑实现
                onRemainingTurnsUpdate(remainingTurns - 1)
            }
            1 -> {
                // 第三次点击显示所有元音字母，并减少一次机会
                onVowelsShownUpdate(true)
                //TODO: Show all vowels logic implementation，然后直接吊死
                onRemainingTurnsUpdate(remainingTurns - 1)
            }
        }
        onRemainingHitsUpdate(remainingHits - 1)
    }
}

// TODO: Implement the remaining functions  1. 结束游戏 2. 看看还有什么遗漏的



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChengkaiJingboJKGuessTheWordAppTheme {
        GuessTheWordGame()
    }
}