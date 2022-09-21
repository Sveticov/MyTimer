package com.svetikov.mytimer


import androidx.lifecycle.ViewModel

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class MyViewModel : ViewModel() {

   suspend fun calculationTimeExercise(timeStart: Int = 0):Flow<Int> {
      return  flow<Int> {

            emit(timeStart)
            var timeCalculation = timeStart

            while (timeCalculation > 0) {
                timeCalculation--
                delay(1000L)
                emit(timeCalculation)
            }

        }
   }


    fun calculateExercise(maxExercise:Float):List<Float>{

        return listOf(
            maxExercise,
            maxExercise*0.80f,
            maxExercise*0.70f,
            maxExercise*0.65f,
            maxExercise*0.6f,
            maxExercise*0.50f,
        )
    }
}

