package com.quiz_together.ui.create

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import com.quiz_together.R
import com.quiz_together.data.Repository
import com.quiz_together.data.model.Broadcast
import com.quiz_together.data.model.GiftType
import com.quiz_together.data.remote.ApiHelper
import com.quiz_together.ui.quizing.QuizingActivity
import com.quiz_together.util.setTouchable
import com.quiz_together.util.setVisibilityFromBoolean
import kotlinx.android.synthetic.main.frag_create.*


class CreateFragment : Fragment(), CreateContract.View, View.OnClickListener {

    val TAG = "CreateFragment"
    override lateinit var presenter: CreateContract.Presenter
    override var isActive: Boolean = false
        get() = isAdded

    private val COLUMN_NO = 6
    private var openingDate: String? = null
    private var isReserved = false
    private var isClickedReservation = false

    companion object {
        @JvmStatic
        val RESERVED = "RESERVED"

        fun newInstance() = CreateFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isReserved = arguments?.getBoolean(RESERVED) ?: false
        if (isReserved) {
            tvDatePicker.visibility = View.VISIBLE
            cancel.visibility = View.VISIBLE
            reservation.visibility = View.GONE
            open.setBackgroundResource(R.drawable.open_reserved_quiz)
        }

        initQuizInfoArea()

        tvDatePicker.setOnClickListener(this)
        reservation.setOnClickListener(this)
        cancel.setOnClickListener(this)
        open.setOnClickListener(this)
        save.setOnClickListener(this)
        btMoney.setOnClickListener(this)
        btGoods.setOnClickListener(this)
        etGiftDescription.setOnClickListener(this)
        etPrize.setOnClickListener(this)

        if (Repository.hasSavedQuiz()) {
            loadQuiz()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun setLoadingIndicator(active: Boolean) {
        activity?.window?.setTouchable(active)
        pb.setVisibilityFromBoolean(active)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.reservation -> {
                showDatePicker()
                isClickedReservation = true
                Toast.makeText(context, "방 예약 완료", Toast.LENGTH_LONG).show()
                activity?.finish()
            }

            R.id.cancel -> {
                activity?.finish()
            }

            R.id.open -> {
                // dialog
                requestCreateBroadcast()
                // d
            }

            R.id.save -> {
                if (isReserved) {
                    // update Broadcast
                } else {
                    saveQuiz()
                }
                activity?.finish()
            }

            R.id.btGoods, R.id.btMoney -> {
                setGiftType(if (v.id == R.id.btGoods) GiftType.GIFT else GiftType.PRIZE)
            }

            R.id.etPrize, R.id.etGiftDescription -> {
                etGiftDescription.visibility = View.GONE
                etPrize.visibility = View.GONE
                setGiftTypeVisibility(View.VISIBLE)
            }

            R.id.tvDatePicker -> showDatePicker()
        }
    }

    private fun requestCreateBroadcast() {
        val broadcast = extractBroadcast()
        Repository.createBroadcast(broadcast, object : ApiHelper.GetSuccessBroadcastIdCallback {
            override fun onSuccessLoaded(broadcastId: String) {
                Toast.makeText(context, "방 개설", Toast.LENGTH_LONG).show()
                if (!isClickedReservation) { // 방 개설 시
                    val intent = Intent(context, QuizingActivity::class.java)
                    intent.putExtra(QuizingActivity.BROADCAST_ID, broadcastId)
                    intent.putExtra(QuizingActivity.IS_ADMIN, true)

                    startActivity(intent)
                }
                activity?.finish()
            }

            override fun onDataNotAvailable() {
                Toast.makeText(context, "방 개설 실패", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setGiftTypeVisibility(visibility: Int) {
        btGoods.visibility = visibility
        btMoney.visibility = visibility
        etGiftDescription.setText("")
        etPrize.setText("")

        if (visibility == View.VISIBLE) {
            btGoods.isSelected = false
            btMoney.isSelected = false
        }
    }

    private fun setGiftType(type: GiftType) {
        if (type == GiftType.GIFT) {
            etGiftDescription.visibility = View.VISIBLE
        } else {
            etPrize.visibility = View.VISIBLE
        }
        setGiftTypeVisibility(View.GONE)
    }

    private fun getGiftType(): GiftType {
        return if (btGoods.isSelected) GiftType.GIFT else GiftType.PRIZE
    }

    private fun extractBroadcast(): Broadcast {
        val title = etTtile.text.toString()
        val description = etDescription.text.toString()
        val scheduledTime = openingDate?.toLong()
        val giftType = getGiftType()
        val prize = if (etPrize.text.toString() == "") null else etPrize.text.toString().toLong()
        val giftDescription = etGiftDescription.text.toString()
        val winnersMsg = etWinnerMessage.text.toString()

        val questionList = (quizViewPager.adapter as QuizPagerAdapter).extractQuestions()
        val questionCount = questionList.count()

        return Broadcast(null, title, description, scheduledTime, null, giftType,
                prize, giftDescription, Repository.getUserId(), null, winnersMsg, null,
                questionList, questionCount, null)
    }

    private fun saveQuiz() {
        Repository.saveQuiz(extractBroadcast())
    }

    private fun loadQuiz() {
        val savedBroadcast = Repository.getSavedQuiz() ?: return

        etTtile.setText(savedBroadcast.title)
        etDescription.setText(savedBroadcast.description)
        openingDate = savedBroadcast.scheduledTime.toString()
        setGiftType(savedBroadcast.giftType)
        etPrize.setText(savedBroadcast.prize.toString())
        etGiftDescription.setText(savedBroadcast.giftDescription)
        etWinnerMessage.setText(savedBroadcast.winnerMessage)

        val questionList = savedBroadcast.questionList
        (quizViewPager.adapter as QuizPagerAdapter).fragmentList.forEachIndexed { index, inputQuizFragment ->
            inputQuizFragment.setQuestion(questionList[index])
        }
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialogFragment()
        datePicker.setOnTimeSetListener(mOnDateSetListener)
        datePicker.show(activity?.fragmentManager, "datePicker")
    }

    private fun showTimerPicker() {
        val timePicker = TimePickerDialogFragment()
        timePicker.setOnTimeSetListener(mOnTimeSetListener)
        timePicker.show(activity?.fragmentManager, "timePicker")
    }

    private val mOnDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                showTimerPicker()

                val formattedYear = year % 100
                tvDatePicker.text = "$formattedYear. $month. $dayOfMonth"

                openingDate = year.toString() + month.toString() + dayOfMonth.toString()

                if (isClickedReservation) {
                    requestCreateBroadcast()
                }
            }

    private val mOnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view: TimePicker?, hourOfDay: Int, minute: Int ->

                val meridiem = if (hourOfDay < 11) "오전" else "오후"
                val formattedHour = if (hourOfDay < 11) hourOfDay else hourOfDay - 11

                tvDatePicker.text = "${tvDatePicker.text} $meridiem $formattedHour 시 $minute 분 시작"
                openingDate += hourOfDay.toString() + minute.toString()
            }

    private fun initQuizInfoArea() {
        val numberRecyclerViewAdapter = NumberRecyclerViewAdapter(activity?.applicationContext!!)
        numberRecyclerViewAdapter.setItemClickListener {
            quizViewPager.currentItem = it
            numberRecyclerViewAdapter.setCurrentItem(it)
        }

        rvQuizNumbers.layoutManager = GridLayoutManager(activity, COLUMN_NO)
        rvQuizNumbers.adapter = numberRecyclerViewAdapter

        quizViewPager.adapter = QuizPagerAdapter(childFragmentManager)
        indicator.setViewPager(quizViewPager)
        quizViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                numberRecyclerViewAdapter.setCurrentItem(position)
            }
        })

        numberRecyclerViewAdapter.mFragmentList = (quizViewPager.adapter as QuizPagerAdapter).fragmentList
    }
}
