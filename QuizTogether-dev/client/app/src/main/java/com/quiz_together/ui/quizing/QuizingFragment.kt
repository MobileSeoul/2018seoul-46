package com.quiz_together.ui.quizing

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.GridView
import com.bumptech.glide.Glide
import com.quiz_together.App
import com.quiz_together.R
import com.quiz_together.data.model.AdminMsg
import com.quiz_together.data.model.AnswerMsg
import com.quiz_together.data.model.BroadcastStatus
import com.quiz_together.data.model.ChatMsg
import com.quiz_together.data.model.EndMsg
import com.quiz_together.data.model.GiftType
import com.quiz_together.data.model.QuestionMsg
import com.quiz_together.data.model.WinnersMsg
import com.quiz_together.util.plusAssign
import com.quiz_together.util.setTouchable
import com.quiz_together.util.toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.frag_quizing.*
import java.util.concurrent.TimeUnit

class QuizingFragment : Fragment(), QuizingContract.View {

    val TAG = "QuizingFragment#$#"
    val CAN_PICK = -1
    val ICON_IS_IMG_SATUS = -1

    val MSG_LINE_CNT_WHEN_REDUCE_WINDOW = 4

    val DEPRECIATE = -1

    override lateinit var presenter: QuizingContract.Presenter

    lateinit var gvAdapter: QuizingAdapter
    lateinit var rcpbController: SelectorController

    var curQuizStep = 0
    var isAlive: Boolean = false
    lateinit var quizStatus: QuizStatus
    var quizBefStatus = QuizStatus.ANSWERING
    lateinit var userMsgs: Array<String>
    var pickNum: Int = CAN_PICK // -1 is can select, but cant touch when value is more than 0

    var isExpandChatWindow = false

    var finalMsg = arrayListOf<String>()
    var isOpenKbd = false

    private val compositeDisposable = CompositeDisposable()

    var isAdmin = false
    var lastQuestionNum = -1

    override var isActive: Boolean = false
        get() = isAdded

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val root = inflater.inflate(R.layout.frag_quizing, container, false)

        return root
    }

    override fun setLoadingIndicator(active: Boolean) {
        activity?.window?.setTouchable(active)
    }

    fun pickAnswer(num: Int) {

        if (isAdmin) return

        pickNum = num
        setAnswerClickable(false)
        presenter.sendAnswer(curQuizStep, pickNum)

        if (pickNum > 0)
            rcpbController.setRCPB(pickNum, SelectorController.SelectorColor.SELECT, 100, true)
    }



    fun initListeners() {

        ibBack.setOnClickListener { activity!!.onBackPressed() }

        rcpbQ1.setOnClickListener { _ ->
            if (pickNum != CAN_PICK)
                return@setOnClickListener
            pickAnswer(1)
        }
        rcpbQ2.setOnClickListener { _ ->
            if (pickNum != CAN_PICK)
                return@setOnClickListener
            pickAnswer(2)
        }
        rcpbQ3.setOnClickListener { _ ->
            if (pickNum != CAN_PICK)
                return@setOnClickListener
            pickAnswer(3)
        }

        // send msg button
        btSendMsg.setOnClickListener {
            sendEdittextMessage()
        }

        rlNextStep.setOnClickListener { v ->

            if (!isAdmin) return@setOnClickListener
            rlNextStep.isClickable = false

            if (quizStatus == QuizStatus.ENDING) {
                presenter.endBroadcast()
                return@setOnClickListener
            }

            if (quizBefStatus == QuizStatus.ANSWERING) {

                if (lastQuestionNum == curQuizStep)
                    presenter.openWinners()
                else
                    presenter.openQuestion(curQuizStep + 1)

                quizBefStatus = QuizStatus.QUIZING
            } else if (quizBefStatus == QuizStatus.QUIZING) {
                presenter.openAnswer(curQuizStep)
                quizBefStatus = QuizStatus.ANSWERING
            }

        }

//        https://stackoverflow.com/questions/4312319/how-to-capture-the-virtual-keyboard-show-hide-event-in-android
//        >> second answer

        val activityRootView = activity!!.getWindow().getDecorView().findViewById<View>(android.R.id.content)
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener{
            val heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight()
//            activityRootView.getRootView().getHeight().toString() // 2960
//            TAG,activityRootView.getHeight().toString() // 2672 > 1619
//            TAG,heightDiff.toString() // 1341 > 288
            if (heightDiff > activityRootView.getRootView().getHeight() / 6) {

                if (isOpenKbd === false) {
                    ibChatOn.visibility = View.INVISIBLE

                    if (quizStatus == QuizStatus.ENDING) {
                        llResult2.visibility = View.VISIBLE
                        llResult1.visibility = View.INVISIBLE
                        llNotice.visibility = View.INVISIBLE
                        rlNextStep.visibility = View.INVISIBLE
                    }

                }
                isOpenKbd = true
            } else if (isOpenKbd === true) {
                ibChatOn.visibility = View.VISIBLE
                csMsgBox.visibility = View.GONE

                if (quizStatus == QuizStatus.ENDING) {
                    llResult1.visibility = View.VISIBLE
                    llResult2.visibility = View.INVISIBLE
                    llNotice.visibility = View.VISIBLE
                    rlNextStep.visibility = View.VISIBLE
                }

                isOpenKbd = false
            }
        }


        etMsg.setOnKeyListener{ v, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                sendEdittextMessage()
                true
            } else
                false
        }


        ibChatOn.setOnClickListener { v ->
            csMsgBox.visibility = View.VISIBLE

            // give focus to edittext ( https://stackoverflow.com/questions/8080579/android-textfield-set-focus-soft-input-programmatically )
            etMsg.post{
                etMsg.requestFocusFromTouch()
                val lManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                lManager.showSoftInput(etMsg, 0)
            }
        }

    }

    fun setAnswerClickable(isClickable: Boolean) {
        rcpbQ1.isClickable = isClickable
        rcpbQ2.isClickable = isClickable
        rcpbQ3.isClickable = isClickable
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // background from gif
        Glide.with(activity!!.applicationContext)
                .load(R.drawable.quiz_background)
                .into(ivBackground)

        presenter.start()

        rcpbController = SelectorController(
                arrayOf(rcpbQ1, rcpbQ2, rcpbQ3),
                arrayOf(tvQuestion1, tvQuestion2, tvQuestion3, tvCnt1, tvCnt2, tvCnt3))

        initListeners()
        initQuizCalledByOncreate()



    }

    fun setQuizNum(num: Int,gage:Boolean) {

        if(gage) {
            tvQuizNum.setTextColor(ContextCompat.getColor(App.instance.applicationContext, R.color.speciYellow))

            cpGage.visibility = View.VISIBLE

            compositeDisposable += Observable.interval(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .take(10)
                    .subscribe {
                        val curSec = it.toInt() + 1
                        cpGage.progress = curSec

                        if(curSec == 10) {
                            cpGage.visibility = View.GONE
                            tvQuizNum.setTextColor(ContextCompat.getColor(App.instance.applicationContext, R.color.deepBlue))
                        }
                    }
        }

        curQuizStep = num

        tvQuizNum.text = "$num"
        tvQuizNum.visibility = View.VISIBLE
        ivIcon.setImageDrawable(context!!.getDrawable(R.drawable.icc_white_circle)) // need to use

    }

    fun setIcon(imgId: Int) {
        ivIcon.setImageDrawable(context!!.getDrawable(imgId))
//        ivIcon.visibility = View.VISIBLE
        tvQuizNum.visibility = View.INVISIBLE
    }

    private fun setDynamicWidth(gridView: GridView) {
        val gridViewAdapter = gridView.adapter ?: return
        var totalWidth: Int
        val items = gridViewAdapter.count
        val listItem = gridViewAdapter.getView(0, null, gridView)
        listItem.measure(0, 0)
        totalWidth = listItem.measuredWidth
        totalWidth = totalWidth * items
        val params = gridView.layoutParams
        params.width = totalWidth
        gridView.layoutParams = params
    }


    fun initQuizCalledByOncreate() {
        isAlive = true
        quizStatus = QuizStatus.BEFORE_START
        userMsgs = arrayOf("", "", "", "", "", "", "", "", "") // "" count is nine !

        userMsgs.forEach { updateUserMsg(it) }

        setAnswerClickable(false)

        cvToolbar.visibility = View.VISIBLE
        llNotice.visibility = View.INVISIBLE
        llQuestion.visibility = View.INVISIBLE
        llResult1.visibility = View.INVISIBLE
        llResult2.visibility = View.INVISIBLE

        if (isAdmin) setIcon(R.drawable.icc_play)

    }

    override fun initQuizCalledByPresenter() {
    }

    fun viewUpdate(quizStatus_: QuizStatus, questionNum: Int, cvToolbarShow: Int,
                   llNoticeShow: Int, llQuestionShow: Int, llResultShow: Int, isExpandChatWindow_: Boolean, imgId: Int) {
        quizStatus = quizStatus_
        isExpandChatWindow = isExpandChatWindow_
        if (questionNum == ICON_IS_IMG_SATUS) setIcon(imgId)
        else setQuizNum(questionNum,true)
        cvToolbar.visibility = View.VISIBLE
        llNotice.visibility = llNoticeShow
        llQuestion.visibility = llQuestionShow


        // this part is llResult1 / llNotice <-> llResult2
        if (quizStatus_ != QuizStatus.ENDING) {
            llResult1.visibility = llResultShow
            llResult2.visibility = llResultShow
        } else {
            if (isOpenKbd) {
                llResult2.visibility = View.VISIBLE
                llResult1.visibility = View.INVISIBLE
                llNotice.visibility = View.INVISIBLE
                rlNextStep.visibility = View.INVISIBLE
            } else {
                llResult2.visibility = View.INVISIBLE
                llResult1.visibility = View.VISIBLE
            }
        }
        updateExpandChatWindow()
    }

    // firebase
    override fun showAdminMsg(adminMsg: AdminMsg) {
        Log.i(TAG, "showAdminMsg : ${adminMsg.toString()}")
        updateAdminMsg(adminMsg.message)
    }

    override fun showChatMsg(chatMsg: ChatMsg) {
        Log.i(TAG, "showChatMsg : ${chatMsg.toString()}")
        updateUserMsg("${chatMsg.userName} : ${chatMsg.message}")
    }

    override fun showQuestionView(questionMsg: QuestionMsg) {
        Log.i(TAG, "showQuestionView : ${questionMsg.toString()}")

        viewUpdate(quizStatus_ = QuizStatus.QUIZING,
                questionNum = questionMsg.step,
                cvToolbarShow = DEPRECIATE,
                llNoticeShow = View.INVISIBLE,
                llQuestionShow = View.VISIBLE,
                llResultShow = View.INVISIBLE,
                isExpandChatWindow_ = false,
                imgId = R.drawable.icc_white_circle)

        pickNum = CAN_PICK

        if (!isAdmin)
            setAnswerClickable(true)

        rcpbController.apply {

            setRCPB(1, SelectorController.SelectorColor.DEFAULT, 0)
            setRCPB(2, SelectorController.SelectorColor.DEFAULT, 0)
            setRCPB(3, SelectorController.SelectorColor.DEFAULT, 0)

            tvQuestion.text = questionMsg.questionProp.title
            cleanPickNumbers()

            questionMsg.questionProp.options.let {
                setQuestions(it.get(0), it.get(1), it.get(2))
            }
        }

        startTimer(null, pickEnd, turnRestView)

    }

    override fun showAnswerView(answerMsg: AnswerMsg) {
        Log.i(TAG, "showAnswerView : ${answerMsg.toString()}")

        if (!isAdmin && answerMsg.answerNo != pickNum && pickNum != CAN_PICK) {
            isAlive = false
        }

        val imgRss = if (isAdmin) R.drawable.icc_play else if (isAlive) R.drawable.icc_life_on else R.drawable.icc_life_off

        viewUpdate(quizStatus_ = QuizStatus.ANSWERING,
                questionNum = ICON_IS_IMG_SATUS,
                cvToolbarShow = DEPRECIATE,
                llNoticeShow = View.INVISIBLE,
                llQuestionShow = View.VISIBLE,
                llResultShow = View.INVISIBLE,
                isExpandChatWindow_ = false,
                imgId = imgRss)

        // exception !!
        if (isAdmin) setQuizNum(answerMsg.step,false)

        val pick1Cnt = answerMsg.questionStatistics.get("1") ?: 0
        val pick2Cnt = answerMsg.questionStatistics.get("2") ?: 0
        val pick3Cnt = answerMsg.questionStatistics.get("3") ?: 0
        val sumPick = (pick1Cnt + pick2Cnt + pick3Cnt).toDouble()

        rcpbController.apply {

            setRCPB(1, SelectorController.SelectorColor.DEFAULT, (20 + pick1Cnt / sumPick * 60).toInt())
            setRCPB(2, SelectorController.SelectorColor.DEFAULT, (20 + pick2Cnt / sumPick * 60).toInt())
            setRCPB(3, SelectorController.SelectorColor.DEFAULT, (20 + pick3Cnt / sumPick * 60).toInt())

            setRCPBOnlyColor(answerMsg.answerNo, SelectorController.SelectorColor.O)
            if (!isAdmin && !isAlive) {
                setRCPBOnlyColor(pickNum, SelectorController.SelectorColor.X)
            }

            tvQuestion.text = answerMsg.questionProp.title

            answerMsg.questionProp.options.let {
                setQuestions(it.get(0), it.get(1), it.get(2))
                setNumbers(pick1Cnt.toString(), pick2Cnt.toString(), pick3Cnt.toString())
            }
        }

        startTimer(null, turnRestView, null)
    }

    override fun showWinnerView(winnersMsg: WinnersMsg) {
        Log.i(TAG, "showWinnerView : ${winnersMsg.toString()}")

        var msgTurn = 0

        finalMsg.add("") // admin msg is finalMsg[0]
        finalMsg.add("${if (winnersMsg.giftType == GiftType.PRIZE) winnersMsg.prize else winnersMsg.giftDescription}")

        if (isAlive) {
            finalMsg.add("${winnersMsg.winnerMessage}")
        } else {
            finalMsg.add("수고하셨습니다. 담에도 파이팅!")
        }

        llNotice.visibility = View.VISIBLE
        tvAdminMsg.text = winnersMsg.winnerMessage

        compositeDisposable += Observable.interval(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (!isOpenKbd)
                        updateAdminMsg(finalMsg.get(msgTurn++))
                    if (msgTurn == 3) msgTurn = 0
                }

        viewUpdate(quizStatus_ = QuizStatus.ENDING,
                questionNum = ICON_IS_IMG_SATUS,
                cvToolbarShow = DEPRECIATE,
                llNoticeShow = View.VISIBLE,
                llQuestionShow = View.INVISIBLE,
                llResultShow = View.VISIBLE,
                isExpandChatWindow_ = false,
                imgId = if (isAdmin) R.drawable.icc_play else R.drawable.icc_profile)

        rlNextStep.isClickable = true

        val users = mutableListOf<Pair<String, String>>()

        var tmpStr: String? = null

        winnersMsg.userName.forEach {
            tmpStr?.run {
                users.add(Pair(it, tmpStr!!))
                tmpStr = null
            } ?: run {
                tmpStr = it
            }
        }

        if (winnersMsg.userName.size % 2 != 0) users.add(Pair(tmpStr!!, ""))


        if (users.size == 0) return;

        gvResult1.numColumns = users.size
        gvResult1.isEnabled = false
        gvResult2.numColumns = users.size
        gvResult2.isEnabled = false

        gvAdapter = QuizingAdapter(this.context!!)
        gvAdapter.users = users
        gvResult1.adapter = gvAdapter
        gvResult2.adapter = gvAdapter
        setDynamicWidth(gvResult1)
        setDynamicWidth(gvResult2)
    }

    override fun endQuiz(endMsg: EndMsg) {
        Log.i(TAG, "endQuiz : ${endMsg.toString()}")
        presenter.unsubscribeFirebase(false)
        "퀴즈를 이용해주셔서 감사합니다".toast()

        activity?.finish()
    }

    // for showQuestionView
    val pickEnd: () -> Any = {


        if (!isAdmin && pickNum == CAN_PICK)
            pickAnswer(0)

    }

    val turnRestView: () -> Any = {
        Log.i(TAG, "turnRestView")

        if (isAdmin) {
            presenter.updateBroadcastStatus(BroadcastStatus.WATING)
            rlNextStep.isClickable = true
        }

        viewUpdate(quizStatus_ = QuizStatus.RESTING,
                questionNum = ICON_IS_IMG_SATUS,
                cvToolbarShow = DEPRECIATE,
                llNoticeShow = View.VISIBLE,
                llQuestionShow = View.INVISIBLE,
                llResultShow = View.INVISIBLE,
                isExpandChatWindow_ = false,
                imgId = if (isAdmin) R.drawable.icc_play else R.drawable.icc_profile)

    }

    fun startTimer(doWhen5Sec: (() -> Any)?, doWhen10Sec: (() -> Any)?, doWhen15Sec: (() -> Any)?) {

        compositeDisposable += Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(3)
                .map { ((it + 1) * 5).toInt() }
                .subscribe {
                    if (it == 5) {
                        doWhen5Sec?.run { doWhen5Sec() }
                    } else if (it == 10) {
                        doWhen10Sec?.run { doWhen10Sec() }
                    } else if (it == 15) {
                        doWhen15Sec?.run { doWhen15Sec() }
                    }
                }
    }

    companion object {
        fun newInstance(isAdmin_: Boolean) = QuizingFragment().apply {
            isAdmin = isAdmin_
        }
    }

    fun updateExpandChatWindow() {
        var msgRst = ""
        userMsgs
                .forEachIndexed { index, _ ->
                    if (isExpandChatWindow)
                        msgRst += userMsgs.get(index) + "\n"
                    if (!isExpandChatWindow && index >= MSG_LINE_CNT_WHEN_REDUCE_WINDOW)
                        msgRst += userMsgs.get(index) + "\n"
                }

        tvUserMsg.text = msgRst
    }

    fun updateAdminMsg(msg: String) {

        if (!isActive) return

        tvAdminMsg.text = msg

        Observable.just(msg)
                .observeOn(AndroidSchedulers.mainThread())
                .filter {
                    quizStatus == QuizStatus.RESTING ||
                            quizStatus == QuizStatus.BEFORE_START ||
                            quizStatus == QuizStatus.ENDING
                }
                .subscribe {
                    llNotice.visibility = View.VISIBLE
                }
    }

    fun updateUserMsg(msg: String) {
        var msgRst = ""
        userMsgs
                .filterIndexed { index, _ -> index != userMsgs.size - 1 }
                .forEachIndexed { index, _ ->
                    userMsgs.set(index, userMsgs.get(index + 1))

                    if (isExpandChatWindow)
                        msgRst += userMsgs.get(index) + "\n"
                    if (!isExpandChatWindow && index >= MSG_LINE_CNT_WHEN_REDUCE_WINDOW)
                        msgRst += userMsgs.get(index) + "\n"
                }

        userMsgs.set(userMsgs.size - 1, msg)
        msgRst += userMsgs.last()

        tvUserMsg.text = msgRst
    }

    fun sendEdittextMessage() {
        presenter.sendMsg(etMsg.text.toString())
        etMsg.setText("")
    }

    override fun setQuestionCnt(cnt: Int) {
        lastQuestionNum = cnt
    }

    override fun endQuizFromErr() {
        "방개설 실패".toast()
        activity!!.finish()
    }

    override fun finishActivity() {
        activity!!.finish()
    }

    enum class QuizStatus(val value: Int) {
        BEFORE_START(100),
        RESTING(200),
        QUIZING(300),
        ANSWERING(400),
        ENDING(500),
    }

    override fun onPause() {
        super.onPause()

        if(!compositeDisposable.isDisposed)
            compositeDisposable.dispose()

    }

}