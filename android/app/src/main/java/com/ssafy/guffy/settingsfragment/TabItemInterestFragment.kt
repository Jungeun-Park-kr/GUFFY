package com.ssafy.guffy.settingsfragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.ApplicationClass.Companion.retrofitUserInterface
import com.ssafy.guffy.R
import com.ssafy.guffy.activity.ChattingActivity
import com.ssafy.guffy.activity.MainActivity
import com.ssafy.guffy.models.User
import com.ssafy.guffy.databinding.FragmentMainBinding
import com.ssafy.guffy.databinding.FragmentTabItemInterestBinding
import com.ssafy.guffy.dialog.ConfirmNoCancelDialog
import com.ssafy.guffy.fragment.LoginFragment
import com.ssafy.guffy.mainfragment.MainFragment
import com.ssafy.guffy.util.Common
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

private const val TAG = "TabItemInterestFragment_구피"

class TabItemInterestFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var binding : FragmentTabItemInterestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabItemInterestBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("LongLogTag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeChips()
        binding.tabItemInterestSaveBtn.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                var user = retrofitUserInterface.getUser(ApplicationClass.sharedPreferences.getString("email","").toString()).awaitResponse().body() as User
                user.interest1 = clickedInterestChipList.get(0)
                user.interest2 = clickedInterestChipList.get(1)
                user.interest3 = clickedInterestChipList.get(2)
                if (clickedInterestChipList.size == 3){
                    user.interest4 = ""
                    user.interest5 = ""
                }else if (clickedInterestChipList.size == 4){
                    user.interest4 = clickedInterestChipList.get(3)
                    user.interest5 = ""
                }else if (clickedInterestChipList.size == 5){
                    user.interest4 = clickedInterestChipList.get(3)
                    user.interest5 = clickedInterestChipList.get(4)
                }
                var result = retrofitUserInterface.updateUser(user).awaitResponse().body() as String
                Log.d(TAG, "변경된 유저 전송 결과 : $result ")
                if (result == "success"){
                    val dialog = ConfirmNoCancelDialog(
                        object : ConfirmNoCancelDialog.ConfirmNoCancelDialogInterface {
                            override fun onYesButtonClick(id: String) {
                                mainActivity.openFragment(1)
                            }
                        }, "관심사를 변경했습니다.",
                        "", ""
                    )
                    dialog.isCancelable = false
                    dialog.show(mainActivity.supportFragmentManager, "태그")
                }
            }

        }
    }

    private fun moveFragment(index: Int, key: String, value: Int) {
        val transaction = parentFragmentManager.beginTransaction()
        when (index) {
            //설정 화면 이동
            1 -> transaction.replace(R.id.frame_layout_main, MainFragment())
                .addToBackStack(null)
            //로그아웃 화면
            2 -> mainActivity.logout()
            // 채팅창 이동...
            3 -> {
                startActivity(Intent(mainActivity, ChattingActivity::class.java))
            }
        }
        transaction.commit()
    }

    private var clickedInterestChipList = ArrayList<String>()
    private fun makeChips() {
        for (i in Common.interstList.indices){
            // Chip 인스턴스 생성
            var chip = Chip(requireContext())
            chip.isCheckable = false
            // 칩 고유 아이디 생성
            chip.id = i
            // Chip 의 텍스트 지정
            chip.text = Common.interstList.get(i)
            // chip group 에 해당 chip 추가
            binding.settingsInterestChipGroup.addView(chip)

            chip.setOnClickListener {
                // 이미 체크한걸 체크하는 경우
                if (clickedInterestChipList.contains(Common.interstList[chip.id])){
                    clickedInterestChipList.remove(Common.interstList[chip.id])
                    chip.setChipBackgroundColorResource(R.color.grey)
                }
                else if (clickedInterestChipList.size < 5){ // 자리 있는 경우
                    clickedInterestChipList.add(Common.interstList[chip.id])
                    chip.setChipBackgroundColorResource(R.color.orange)
                }
                else { // 5개 이상인 경우
                    Common.showAlertDialog(mainActivity, "5개 이상은 선택할 수 없어요.", "InterestFull")
                }
            }
        }
    }
}