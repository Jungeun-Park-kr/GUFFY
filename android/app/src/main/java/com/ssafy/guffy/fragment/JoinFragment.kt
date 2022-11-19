package com.ssafy.guffy.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.ssafy.guffy.R
import com.ssafy.guffy.activity.LoginActivity
import com.ssafy.guffy.databinding.FragmentJoinBinding
import com.ssafy.guffy.util.Common.Companion.interstList
import com.ssafy.guffy.util.Common.Companion.showAlertDialog


private const val TAG = "JoinFragment 구피"

class JoinFragment : Fragment() {
    private lateinit var binding: FragmentJoinBinding
    private lateinit var loginActivity:LoginActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginActivity = context as LoginActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // spinner 연결
        var adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_list,
            android.R.layout.simple_spinner_dropdown_item
        );
        binding.genderSpinner.setAdapter(adapter)

//        // 선택된 칩 그룹 출력
//        binding.joinMbtiFilterChipGroup.setOnClickListener{
//            Log.d(TAG, "onViewCreated: ${binding.joinMbtiFilterChipGroup.checkedChipId}")
//        }

//        setOnCheckedStateChangeListener(ChipGroup.OnCheckedStateChangeListener listener)
//        체크된 칩이 변경될 때 호출할 콜백을 이 그룹에 등록합니다.

//        var mbtiCheckedlist = binding.joinFilterChipGroup.getCheckedChipIds()
//        Log.d(TAG, "onViewCreated: ${mbtiCheckedlist}")

        // 체크된 칩이 변경될 때 호출할 콜백을 이 그룹에 등록합니다.
//        setOnCheckedStateChangeListener()

//        setSelectionRequired(boolean selectionRequired)
        // 모든 하위 칩이 선택 취소되지 않도록 할지 여부를 설정합니다.

        var clickedInterestChipList = mutableListOf<String>()
        for (i in interstList.indices){
            // Chip 인스턴스 생성
            var chip = Chip(requireContext())
            chip.isCheckable = false
            // 칩 고유 아이디 생성
            chip.id = i
            // Chip 의 텍스트 지정
            chip.text = interstList.get(i)
            // chip group 에 해당 chip 추가
            binding.joinInterstChipGroup.addView(chip)

            chip.setOnClickListener {

                // 이미 체크한걸 체크하는 경우
                if (clickedInterestChipList.contains(interstList[chip.id])){
                    clickedInterestChipList.remove(interstList[chip.id])
                    chip.setChipBackgroundColorResource(R.color.grey)
                } else if (clickedInterestChipList.size < 5){
                    clickedInterestChipList.add(interstList[chip.id])
                    chip.setChipBackgroundColorResource(R.color.orange)
                }
                else{
                    showAlertDialog(loginActivity, "5개 이상은 선택할 수 없어요.", "InterestFull")
                }
            }
        }

        binding.joinNextBtn.setOnClickListener {
            if (binding.joinMbtiFilterChipGroup.checkedChipId == -1){ // mbti 하나도 선택 안 한 경우
                Toast.makeText(requireContext(), "mbti를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else if (clickedInterestChipList.size <= 2){ // 관심사 0 ~ 2개 선택한 경우
                Toast.makeText(requireContext(), "관심사를 3개 이상 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{ // 다음 버튼 누르면 로그인 화면으로 전환
                Toast.makeText(requireContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.login_frame_container, LoginFragment())
                    .commit()
            }
        }
    }

}