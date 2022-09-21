package com.svetikov.mytimer

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.svetikov.mytimer.ui.theme.ColorSurface
import com.svetikov.mytimer.ui.theme.MyTimerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.floor

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
    var hideHeadSetting by remember {
        mutableStateOf(false)
    }
    var maxExercise by remember {
        mutableStateOf("0")
    }
    var listExerciseTimes by remember {
        mutableStateOf(listOf<Float>())
    }
    //todo
    var builder = NotificationCompat.Builder(context,"test")
        .setContentTitle("Ok")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    Button(onClick = {
        builder
    }) {
        Text(text = "ok")
    }

    //todo

    val scope = rememberCoroutineScope()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ColorSurface
    ) {
        Column(
            modifier = Modifier.padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!hideHeadSetting)
                OutlinedTextField(value = setTime, onValueChange = { setTime = it },
                    textStyle = TextStyle(color = Color.White),
                    label = {
                        Text(text = "Set time rest", color = Color.White)
                    })


            Spacer(modifier = Modifier.padding(top = 20.dp))

            if (!hideHeadSetting)
                OutlinedTextField(
                    value = maxExercise,
                    onValueChange = { maxExercise = it },
                    textStyle = TextStyle(Color.White),
                    label = {
                        Text(
                            text = "Max Exercise",
                            color = Color.White
                        )
                    }
                )
            if (!hideHeadSetting)
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(20.dp)
                        .offset(x = 100.dp)
                        .size(50.dp)
                        .clickable {
                            hideHeadSetting = true
                            listExerciseTimes = model.calculateExercise(maxExercise.toFloat())

                        }
                )
//            Button(onClick = {
//                hideHeadSetting = true
//                // println(maxExercise)
//                listExerciseTimes = model.calculateExercise(maxExercise.toFloat())
//                // println(exerciseTimes)
//            }) {
//                Text(text = "Enter")
//            }
            if (hideHeadSetting)
                Text(
                    text = " $exerciseLive",//todo timer
                    fontSize = 200.sp,
                    color = Color.White,
                    modifier = Modifier.padding(end = 55.dp)
                )
            //todo start and pause sound
            if (exerciseLive < 10 && exerciseLive != 0) {
                scope.launch {
                    sound.start()
                    delay(15000L)
                    sound.pause()
                }
            }

            var idExercise by remember {
                mutableStateOf(0)
            }
            LazyRow(modifier = Modifier.padding(end = 35.dp)) {
                itemsIndexed(listExerciseTimes) { index, it ->
                    Box(modifier = Modifier
                        .padding(5.dp)
                        .clickable {
                            scope.launch {
                                // println(it)
                                if ((index) == idExercise) {
                                    model
                                        .calculationTimeExercise(setTime.toInt())
                                        .collect { exerciseLive = it }
                                    idExercise++
                                }
                                if (idExercise >= 6) {
                                    idExercise = 0
                                    hideHeadSetting = false
                                    listExerciseTimes = emptyList()
                                }
                            }
                        }) {
                        Text(
                            text = "${(floor(it.toDouble())).toInt()}",
                            fontSize = if (index == idExercise) 40.sp else 22.sp,
                            color = Color.White
                        )
                    }

                }
            }
            if (hideHeadSetting)
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(20.dp)
                        .offset(x = 100.dp)
                        .size(50.dp)
                        .clickable {
                            idExercise = 0
                            hideHeadSetting = false
                            listExerciseTimes = emptyList()
                            maxExercise = "0"
                            setTime = "0"
                        }
                )

        }
    }

}





















