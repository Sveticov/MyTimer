package com.svetikov.mytimer

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.svetikov.mytimer.ui.theme.MyTimerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTimerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MyTimer(this)
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MyTimer(context: Context, model: MyViewModel = viewModel()) {

    val sound: MediaPlayer = MediaPlayer.create(context, R.raw.sound1)
    var setTime by remember {
        mutableStateOf("60")
    }
    var exerciseLive by remember {
        mutableStateOf(0)
    }
    val scope = rememberCoroutineScope()
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(20.dp)) {
            OutlinedTextField(value = setTime, onValueChange = { setTime = it }, label = {
                Text(text = "Set time rest")
            })
            Button(
                onClick = {
                    scope.launch {
                        model.calculationTimeExercise(setTime.toInt()).collect { exerciseLive = it }
                    }

                },
                enabled = exerciseLive == 0
            ) {
                Text(text = "Start")
            }
            Text(text = " Exercise live $exerciseLive", fontSize = 22.sp)
            if (exerciseLive < 5 && exerciseLive != 0) {
                scope.launch {
                    sound.start()
                    delay(15000L)
                    sound.pause()
                }
            }


            Spacer(modifier = Modifier.padding(top = 20.dp))
//            Row() {
//                Button(onClick = {
//                    scope.launch { sound.start() }
//                }) {
//                    Text("play")
//                }
//                Spacer(modifier = Modifier.padding(8.dp))
//                Button(onClick = { sound.pause() }) {
//                    Text("pause")
//                }
//                Spacer(modifier = Modifier.padding(8.dp))
//                Button(onClick = { sound.stop() }) {
//                    Text("stop")
//                }
//            }
            var maxExercise by remember {
                mutableStateOf("")
            }
            var exerciseTimes by remember {
                mutableStateOf(listOf<Float>())
            }
            Card(
                modifier = Modifier.padding(10.dp),
                backgroundColor = Color.LightGray
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    OutlinedTextField(
                        value = maxExercise,
                        onValueChange = { maxExercise = it },
                        label = { Text(text = "Max Excercise") }
                    )
                    Button(onClick = {
                        exerciseTimes = model.calculateExercise(maxExercise.toFloat())
                    }) {
                        Text(text = "Enter")
                    }
                    LazyRow() {
                        items(exerciseTimes) {
                            Box(modifier = Modifier
                                .padding(5.dp)
                                .clickable {
                                    scope.launch {
                                        model
                                            .calculationTimeExercise(setTime.toInt())
                                            .collect { exerciseLive = it }
                                    }
                                }) {
                                Text(text = "$it")
                            }

                        }
                    }


                }
            }

        }

    }

}
















