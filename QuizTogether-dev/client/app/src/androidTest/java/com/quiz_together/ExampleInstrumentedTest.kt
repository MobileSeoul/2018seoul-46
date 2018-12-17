package com.quiz_together

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.google.gson.Gson
import com.quiz_together.data.Repository
import com.quiz_together.data.model.*
import com.quiz_together.data.remote.ApiHelper
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    val TAG = "Example#$#"

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.quiz_together", appContext.packageName)
    }

    fun testRunText(runText:String){
        println("###########################################")
        println("${runText} is START")
    }

    // tt : testText
    fun tt(textText:String) {
        println("### TESTRESULT : ${textText}")
    }

    @Test
    fun testSignup() {
        testRunText("testSignup")

        // bbb
        // u51746133bf56b26c7e0988465c5e8c31

        Repository.signup("aaa","dummy",object:ApiHelper.UserViewCallback{
            override fun onLoginLoaded(respLogin: UserView) {
                tt(respLogin.name)
                tt(respLogin.userId)
            }

            override fun onDataNotAvailable() {
                tt("onDataNotAvailable")

            }
        })
    }

    @Test
    fun testLogin() {
        testRunText("testLogin")

        Repository.login("u51746133bf56b26c7e0988465c5e8c31",object:ApiHelper.UserViewCallback{
            override fun onLoginLoaded(respLogin: UserView) {
                tt(respLogin.name)
                tt(respLogin.userId)
            }

            override fun onDataNotAvailable() {
                tt("onDataNotAvailable")
            }
        })
    }


    @Test
    fun testGetPagingBroadcastList(){
        testRunText("testGetPagingBroadcastList")

//        Repository.getPagingBroadcastList(object:ApiHelper.GetBroadcastsCallback{
//            override fun onBroadcastsLoaded(broadcasts: List<Broadcast>) {
//                broadcasts.forEach{
//                    tt(it.toString())
//                }
//                tt("onBroadcastsLoaded")
//            }
//            override fun onDataNotAvailable() {
//                tt("onDataNotAvailable")
//            }
//        })

    }

    @Test
    fun testGetBroadcastForUpdateById(){
        testRunText("testGetBroadcastForUpdateById")

        println("###########################################")
        println("testGetBroadcastForUpdateById START")

        Repository.getBroadcastForUpdateById("b82d4e3b1873ef25f7264af5a2113f5a7",
                object : ApiHelper.GetBroadcastCallback{
                    override fun onBroadcastLoaded(broadcasts: Broadcast) {
                        tt(broadcasts.toString())
                    }

                    override fun onDataNotAvailable() {
                        tt("onDataNotAvailable")
                    }

                })



    }

    @Test
    fun testCreateBroadcast() {
        testRunText("testCreateBroadcast")


        val dummyText = "1002"

        val dummyString = "test${dummyText}"
        val dummyInt = dummyText.toInt()
        val dummyLong = dummyText.toLong()

        val ql = mutableListOf<Question>()

        val options = arrayListOf<String>()
        options.add(dummyString)
        options.add(dummyString)
        options.add(dummyString)


        ql.add(Question(1, null, QuestionProp(dummyString, options), CategoryType.NORMAL))
        ql.add(Question(2, null, QuestionProp(dummyString, options), CategoryType.NORMAL))
//
//        Repository.createBroadcast(Broadcast(
//                null,
//                dummyString,
//                dummyString,
//                null,
//                GiftType.PRIZE,
//                dummyLong,
//                dummyString,
//                "u51746133bf56b26c7e0988465c5e8c31",
//                null,
//                dummyString,
//                null,
//                ql,
//                0
//
//        ),
//        object: ApiHelper.GetSuccessCallback{
//            override fun onSuccessLoaded() {
//                tt("onSuccessLoaded")
//            }
//
//            override fun onDataNotAvailable() {
//                tt("onDataNotAvailable")
//            }
//
//        })

    }

    @Test
    fun json2string2json2objectTest() {
        val jsObj = JSONObject()

        jsObj.accumulate("pushType","END_MESSAGE")
        println("jsObj >> ${jsObj.toString()}")

        val strObj = jsObj.toString()
        println("jsStr >> ${strObj}")

        val jsObj2 = JSONObject(strObj)
        println("jsObj2 >> ${jsObj2.toString()}")

        val gson = Gson()

        val gsObj = gson.fromJson(jsObj2.toString(),EndMsg::class.java)

        println("gsObj >> ${gsObj.toString()}")
    }


    @Test
    fun json2string2json2objectTest2() {

        val gson = Gson()

        val jsObj = JSONObject()

        val bb = arrayListOf<String>()
        bb.add("A1")
        bb.add("A2")
        bb.add("A3")

        val questionMsg = QuestionMsg(PushType.ADMIN_MESSAGE, QuestionProp("title2",bb),100)

        println("questionMsg >> ${questionMsg.toString()}")

        val strObj = gson.toJson(questionMsg)
        println("strObj >> ${strObj.toString()}")

        val questionMsg2 = gson.fromJson(strObj.toString(),QuestionMsg::class.java)
        println("questionMsg2 >> ${questionMsg2.toString()}")



    }



    @Test
    fun json2string2json2objectTest3() {

        val m = mutableMapOf<String,String>()

        m.put("AA","AA1")
        m.put("BB","BB2")

        val gson = Gson()
        val mm =  gson.toJson(m)

        println("mm >> ${mm.toString()}")

    }


    @Test
    fun updateBroadcastStatusTest() {
        testRunText("updateBroadcastStatus")

        Repository.updateBroadcastStatus("b82d4e3b1873ef25f7264af5a2113f5a7","u51746133bf56b26c7e0988465c5e8c31",BroadcastStatus.OPEN_ANSWER,
                object: ApiHelper.GetSuccessCallback{
                    override fun onSuccessLoaded() {
                        Log.i(TAG,"onSuccessLoaded")
                    }

                    override fun onDataNotAvailable() {
                        Log.i(TAG,"onDataNotAvailable")
                    }

                }
        )

    }

    @Test
    fun enumCompareString() {

        val str = "NOTICE_MESSAGE"

        Log.i(TAG,PushType.NOTICE_MESSAGE.toString())
        Log.i(TAG,PushType.NOTICE_MESSAGE.name)
        Log.i(TAG,PushType.NOTICE_MESSAGE.value.toString())

//        if ( str == PushType.NOTICE_MESSAGE.toString() )





    }





}
