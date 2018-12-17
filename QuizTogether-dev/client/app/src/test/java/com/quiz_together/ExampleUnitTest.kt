package com.quiz_together

import android.util.Log
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    val TAG = "ExampleUnitTest"

    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)

        val aaa:String? = null
//        aaa?.let {
//            print("AA")
//        }?:let {
//            print("BB")
//        }

        aaa?.run {
            print("call when value is not null")
        }?:run {
            print("call when value is null")
        }


    }

    @Test
    fun calculTest() {
        val pick1Cnt = 100
        val pick2Cnt = 200
        val pick3Cnt = 300

        println((100/600*100))


        println((pick1Cnt/(pick1Cnt+pick2Cnt+pick3Cnt).toDouble()*100).toString())
        println((pick2Cnt/(pick1Cnt+pick2Cnt+pick3Cnt)*100).toString())
        println((pick3Cnt/(pick1Cnt+pick2Cnt+pick3Cnt)*100).toString())



    }




}
