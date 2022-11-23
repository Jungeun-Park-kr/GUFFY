package com.ssafy.guffy.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.ApplicationClass.Companion.retrofitUserInterface
import com.ssafy.guffy.R
import com.ssafy.guffy.activity.LoginActivity
import com.ssafy.guffy.databinding.FragmentJoinBinding
import com.ssafy.guffy.models.User
import com.ssafy.guffy.util.Common.Companion.genderList
import com.ssafy.guffy.util.Common.Companion.interstList
import com.ssafy.guffy.util.Common.Companion.mbtiList
import com.ssafy.guffy.util.Common.Companion.passwordRegex
import com.ssafy.guffy.util.Common.Companion.showAlertDialog
import com.ssafy.guffy.util.Common.Companion.showAlertWithMessageDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.log


private const val TAG = "JoinFragment 구피"

private var emailSuccess = ""
private var certificationSuccess = false

class JoinFragment : Fragment() {
    private lateinit var binding: FragmentJoinBinding
    private lateinit var loginActivity: LoginActivity

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

    var certificationNumber = ""

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: 회원가입")
        binding.joinEmailCheckBtn.setOnClickListener {
            var email = binding.emailCheckTv.text.trim().toString()
            Log.d(TAG, "onViewCreated: ")
            CoroutineScope(Dispatchers.Main).launch {
                val result = retrofitUserInterface.getEmailIsUsed(email).awaitResponse().body()
                if (result == "yes") {
                    showAlertDialog(
                        loginActivity,
                        "이미 사용중인 아이디입니다.",
                        ""
                    )
                } else {
                    showAlertWithMessageDialog(
                        loginActivity,
                        "사용가능한 아이디입니다.",
                        "이메일로 발송된 인증번호를 입력해주세요."
                        ,"EmailSendSucceeded"
                    )
                    binding.joinEmailCheckBtn.backgroundTintList =
                        ContextCompat.getColorStateList(loginActivity, R.color.green)
                    binding.emailTextInputLayout.isEnabled = false
                    binding.joinEmailCheckBtn.isClickable = false
                    CoroutineScope(Dispatchers.Main).launch {
                        var result =
                            retrofitUserInterface.getMainAuth(binding.joinEmailEditText.text.toString().trim())
                                .awaitResponse().body().toString()
                        if (result.isNotEmpty()) {
                            certificationNumber = result
                            Log.d(TAG, "인증번호 : $certificationNumber")
                        }
                    }
                }
            }
        }



        binding.joinCertificationNumberBtn.setOnClickListener {
            val inputCertificationNumber = binding.joinCertificationNumberEditText.text.toString().trim()
            if (inputCertificationNumber.isEmpty()) {
                Toast.makeText(loginActivity, "인증번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d(TAG, "onViewCreated: 입력한 번호: $inputCertificationNumber")
                    if (inputCertificationNumber == certificationNumber) {
                        showAlertDialog(
                            loginActivity,
                            "인증이 완료되었습니다.",
                            ""
                        )
                        binding.joinCertificationNumberBtn.backgroundTintList =
                            ContextCompat.getColorStateList(loginActivity, R.color.green)
                        certificationSuccess = true
                        binding.joinCertificationNumberTextInputLayout.isEnabled = false
                        binding.joinCertificationNumberBtn.isClickable = false;
                        binding.joinEmailCheckBtn.backgroundTintList =
                            ContextCompat.getColorStateList(loginActivity, R.color.green)
                    } else {
                        Toast.makeText(loginActivity, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        val passwordListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!passwordRegex(s.toString())) {
                    binding.joinPwTextInputLayout.error = "적절한 비밀번호가 아닙니다."
                } else {
                    binding.joinPwTextInputLayout.hint = "적절한 비밀번호입니다."
                    binding.joinPwTextInputLayout.error = ""
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    when {
                        s.isEmpty() -> {
                            binding.joinPwTextInputLayout
                            binding.joinPwTextInputLayout.error = "비밀번호를 입력해주세요."
                        }
                    }
                }
            }

        }

        binding.joinPwTextInputLayout.editText?.addTextChangedListener(passwordListener)
        binding.joinPwTextInputLayout.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
        binding.joinPwTextInputLayout.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.joinPwTextInputLayout.hint = "비밀번호를 입력해주세요"
                if (!passwordRegex(binding.joinPwEditText.text.toString())) {
                    binding.joinPwTextInputLayout.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
                }
            } else {
                binding.joinPwEditText.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
                if (!passwordRegex(binding.joinPwEditText.text.toString())) {
                    binding.joinPwTextInputLayout.hint = "알파벳, 숫자, 특수문자 : 5 ~ 15자"
                }
            }
        }

        // spinner 연결
        var adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_list,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.joinGenderSpinner.setAdapter(adapter)

        var genderChoice = ""
        binding.joinGenderSpinner.onItemClickListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                genderChoice = genderList[p2]
                Log.d(TAG, "onItemSelected: p2 = $p2, genderChoice = $genderChoice")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                genderChoice = genderList[p2]
                Log.d(TAG, "onItemSelected: click; $genderChoice")
            }
        }

        // mbti 칩 그룹 생성
        var clickedMBTIChip = -1

        binding.joinMbtiChipGroup.setOnCheckedStateChangeListener { group, checkedId ->
            when (binding.joinMbtiChipGroup.checkedChipId) {
                R.id.join_isfp_chip -> clickedMBTIChip = 0
                R.id.join_isfj_chip -> clickedMBTIChip = 1
                R.id.join_istp_chip -> clickedMBTIChip = 2
                R.id.join_istj_chip -> clickedMBTIChip = 3

                R.id.join_intj_chip -> clickedMBTIChip = 4
                R.id.join_intp_chip -> clickedMBTIChip = 5
                R.id.join_estp_chip -> clickedMBTIChip = 6
                R.id.join_estj_chip -> clickedMBTIChip = 7

                R.id.join_entp_chip -> clickedMBTIChip = 8
                R.id.join_entj_chip -> clickedMBTIChip = 9
                R.id.join_infp_chip -> clickedMBTIChip = 10
                R.id.join_infj_chip -> clickedMBTIChip = 11

                R.id.join_esfp_chip -> clickedMBTIChip = 12
                R.id.join_esfj_chip -> clickedMBTIChip = 13
                R.id.join_enfp_chip -> clickedMBTIChip = 14
                R.id.join_enfj_chip -> clickedMBTIChip = 15

                R.id.join_idk_chip -> clickedMBTIChip = 16
                R.id.join_hate_chip -> clickedMBTIChip = 17
                R.id.join_private_chip -> clickedMBTIChip = 18
            }
            Log.d(TAG, "onViewCreated: ${mbtiList[clickedMBTIChip]}")
        }

        var clickedInterestChipList = mutableListOf<String>()
        for (i in interstList.indices) {
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
                if (clickedInterestChipList.contains(interstList[chip.id])) {
                    clickedInterestChipList.remove(interstList[chip.id])
                    chip.setChipBackgroundColorResource(R.color.grey)
                } else if (clickedInterestChipList.size < 5) {
                    clickedInterestChipList.add(interstList[chip.id])
                    chip.setChipBackgroundColorResource(R.color.orange)
                } else {
                    showAlertDialog(loginActivity, "5개 이상은 선택할 수 없어요.", "InterestFull")
                }
                Log.d(TAG, "onViewCreated: 선택한 관심사 리스트 : $clickedInterestChipList")
            }
        }

        binding.joinNextBtn.setOnClickListener {
            // email 중복 체크 했는지
            if (binding.joinEmailEditText.isEnabled) {
                showAlertDialog(loginActivity, "이메일 인증을 진행해주세요", "")
            }
            // 이메일 인증 번호
            else if (binding.joinCertificationNumberEditText.isEnabled) {
                showAlertDialog(loginActivity, "이메일 인증번호를 입력해주세요", "")
            }
            // pw가 적절한지
            if (!passwordRegex(binding.joinPwEditText.text.toString())) {
                showAlertDialog(loginActivity, "적절한 비밀번호가 아닙니다", "")
            }
            // gender를 선택했는지
            else if (genderChoice.isEmpty()) {
                showAlertDialog(loginActivity, "성별을 선택해주세요", "")
            }
            // mbti 하나도 선택 안 한 경우
            else if (clickedMBTIChip == -1) {
                showAlertDialog(loginActivity, "mbti를 선택해주세요", "")
            }
            // 관심사 5개 이하 선택한 경우
            else if (clickedInterestChipList.size < 3) {
                showAlertDialog(loginActivity, "관심사를 3개 이상 선택해주세요", "")
            }
            // 다음 버튼 누르면 로그인 화면으로 전환
            else {
                Log.d(TAG, "onViewCreated: 모든 케이스 통과")
                Log.d(TAG, "onViewCreated: ${interstList}")

                Log.d(TAG, "onViewCreated: 칩 개수:" + clickedInterestChipList.size)


                if (clickedInterestChipList.size == 3) {
                    clickedInterestChipList.add("")
                    clickedInterestChipList.add("")
                } else if (clickedInterestChipList.size == 4) {
                    clickedInterestChipList.add("")
                }

                if (genderChoice == "여자") genderChoice = "F"
                else if (genderChoice == "남자") genderChoice = "M"
                else genderChoice = "N"

                Log.d(
                    TAG, "onViewCreated: " +
                            "email: $emailSuccess, gender:$genderChoice, chip1:${clickedInterestChipList[0]}" +
                            ", chip2:${clickedInterestChipList[1]}, chip3:${clickedInterestChipList[2]}," +
                            "chip4:${clickedInterestChipList[3]}, chip5:${clickedInterestChipList[4]}, " +
                            "mbti:${mbtiList[clickedMBTIChip]}, pw:${binding.joinPwEditText.text.toString()}"
                )

                emailSuccess = binding.joinEmailEditText.text.toString().trim()

                var user = User(
                    emailSuccess,
                    genderChoice,
                    0,
                    clickedInterestChipList[0],
                    clickedInterestChipList[1],
                    clickedInterestChipList[2],
                    clickedInterestChipList[3] ?: "",
                    clickedInterestChipList[4] ?: "",
                    mbtiList[clickedMBTIChip],
                    "",
                    binding.joinPwEditText.text.toString(),
                    ""
                )
                Log.d(TAG, "onViewCreated: ${user}")
                // 회원가입 성공 dialog, 다이어로그의 확인버튼 누르면 로그인 액티비티로 넘어감
                CoroutineScope(Dispatchers.Main).launch {
                    val result = retrofitUserInterface.addUser(user).awaitResponse().body() as String
                    Log.d(TAG, "onViewCreated: 회원가입 : $result")
                    if (result == "success") {
                        val builder = AlertDialog.Builder(requireContext())
                        builder
                            .setTitle("회원가입이 완료되었습니다.")
                            .setPositiveButton("OK") { dialog, which ->
                                Log.d(TAG, "showDialog: 확인버튼 누름")
                                requireActivity()
                                    .supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.login_frame_container, LoginFragment())
                                    .commit()
                            }
                            .create()
                            .show()
                    }
                }
            }
        }
    }

}
