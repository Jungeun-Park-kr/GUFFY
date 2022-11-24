package com.ssafy.guffy.settingsfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.ApplicationClass.Companion.retrofitUserService
import com.ssafy.guffy.ApplicationClass.Companion.sharedPreferences
import com.ssafy.guffy.R
import com.ssafy.guffy.activity.MainActivity
import com.ssafy.guffy.databinding.FragmentTabItemMbtiBinding
import com.ssafy.guffy.dialog.ConfirmNoCancelDialog
import com.ssafy.guffy.models.User
import com.ssafy.guffy.util.Common
import com.ssafy.guffy.util.Common.Companion.mbtiList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

private const val TAG = "TabItemMbtiFragment 구피"

class TabItemMbtiFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private lateinit var binding: FragmentTabItemMbtiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabItemMbtiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var clickedMBTIChip = -1
        var mbtiString = ""
        CoroutineScope(Dispatchers.IO).launch {
            val result =
                retrofitUserService.getUser(sharedPreferences.getString("email", "").toString())
                    .awaitResponse().body()
            if (result != null) {
                mbtiString = result.mbti
            }
            for (i in 0..mbtiList.size) {
                if (mbtiString == mbtiList[0]) {
                    clickedMBTIChip = i
                    break;
                }
            }

            Log.d(TAG, "onViewCreated: $clickedMBTIChip")


            when (clickedMBTIChip) {
                0 -> {
                    binding.settingsMbtiIsfpChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                1 -> {
                    binding.settingsMbtiIsfjChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                2 -> {
                    binding.settingsMbtiIstpChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                3 -> {
                    binding.settingsMbtiIstjChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                4 -> {
                    binding.settingsMbtiIntjChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                5 -> {
                    binding.settingsMbtiIntpChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                6 -> {
                    binding.settingsMbtiEstpChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                7 -> {
                    binding.settingsMbtiEstjChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                8 -> {
                    binding.settingsMbtiEntpChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                9 -> {
                    binding.settingsMbtiEntjChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                10 -> {
                    binding.settingsMbtiInfpChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                11 -> {
                    binding.settingsMbtiInfjChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                12 -> {
                    binding.settingsMbtiEsfpChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                13 -> {
                    binding.settingsMbtiEsfjChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                14 -> {
                    binding.settingsMbtiEnfpChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                15 -> {
                    binding.settingsMbtiEnfjChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                16 -> {
                    binding.settingsMbtiIdkChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                17 -> {
                    binding.settingsMbtiHateChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }
                18 -> {
                    binding.settingsMbtiPrivateChip.backgroundTintList =
                        ContextCompat.getColorStateList(mainActivity, R.color.orange)
                }

//                R.id.settings_mbti_isfp_chip -> clickedMBTIChip = 0
//                R.id.settings_mbti_isfj_chip -> clickedMBTIChip = 1
//                R.id.settings_mbti_istp_chip -> clickedMBTIChip = 2
//                R.id.settings_mbti_istj_chip -> clickedMBTIChip = 3
//
//                R.id.settings_mbti_intj_chip -> clickedMBTIChip = 4
//                R.id.settings_mbti_intp_chip -> clickedMBTIChip = 5
//                R.id.settings_mbti_estp_chip -> clickedMBTIChip = 6
//                R.id.settings_mbti_estj_chip -> clickedMBTIChip = 7
//
//                R.id.settings_mbti_entp_chip -> clickedMBTIChip = 8
//                R.id.settings_mbti_entj_chip -> clickedMBTIChip = 9
//                R.id.settings_mbti_infp_chip -> clickedMBTIChip = 10
//                R.id.settings_mbti_infj_chip -> clickedMBTIChip = 11
//
//                R.id.settings_mbti_esfp_chip -> clickedMBTIChip = 12
//                R.id.settings_mbti_esfj_chip -> clickedMBTIChip = 13
//                R.id.settings_mbti_enfp_chip -> clickedMBTIChip = 14
//                R.id.settings_mbti_enfj_chip -> clickedMBTIChip = 15
//
//                R.id.settings_mbti_idk_chip -> clickedMBTIChip = 16
//                R.id.settings_mbti_hate_chip -> clickedMBTIChip = 17
//                R.id.settings_mbti_private_chip -> clickedMBTIChip = 18
            }
        }

        binding.tabItemMbtiSaveBtn.setOnClickListener {
            when (binding.settingsMbtiMbtiFilterChipGroup.checkedChipId) {
                R.id.settings_mbti_isfp_chip -> clickedMBTIChip = 0
                R.id.settings_mbti_isfj_chip -> clickedMBTIChip = 1
                R.id.settings_mbti_istp_chip -> clickedMBTIChip = 2
                R.id.settings_mbti_istj_chip -> clickedMBTIChip = 3

                R.id.settings_mbti_intj_chip -> clickedMBTIChip = 4
                R.id.settings_mbti_intp_chip -> clickedMBTIChip = 5
                R.id.settings_mbti_estp_chip -> clickedMBTIChip = 6
                R.id.settings_mbti_estj_chip -> clickedMBTIChip = 7

                R.id.settings_mbti_entp_chip -> clickedMBTIChip = 8
                R.id.settings_mbti_entj_chip -> clickedMBTIChip = 9
                R.id.settings_mbti_infp_chip -> clickedMBTIChip = 10
                R.id.settings_mbti_infj_chip -> clickedMBTIChip = 11

                R.id.settings_mbti_esfp_chip -> clickedMBTIChip = 12
                R.id.settings_mbti_esfj_chip -> clickedMBTIChip = 13
                R.id.settings_mbti_enfp_chip -> clickedMBTIChip = 14
                R.id.settings_mbti_enfj_chip -> clickedMBTIChip = 15

                R.id.settings_mbti_idk_chip -> clickedMBTIChip = 16
                R.id.settings_mbti_hate_chip -> clickedMBTIChip = 17
                R.id.settings_mbti_private_chip -> clickedMBTIChip = 18
            }
            if (clickedMBTIChip == -1) {
                Common.showAlertDialog(mainActivity, "mbti를 선택해주세요", "")
            } else {

                CoroutineScope(Dispatchers.Main).launch {
                    var user = retrofitUserService.getUser(
                        sharedPreferences.getString("email", "").toString()
                    ).awaitResponse().body() as User

                    user.mbti = mbtiList.get(clickedMBTIChip)
                    val result =
                        retrofitUserService.updateUser(user).awaitResponse().body() as String
                    if (result == "success") {
                        val dialog = ConfirmNoCancelDialog(
                            object : ConfirmNoCancelDialog.ConfirmNoCancelDialogInterface {
                                override fun onYesButtonClick(id: String) {
                                    mainActivity.openFragment(1)
                                }
                            }, "MBTI를 변경했습니다.",
                            "", ""
                        )
                        dialog.isCancelable = false
                        dialog.show(mainActivity.supportFragmentManager, "태그")
                    }
                }

            }

        }
    }

}