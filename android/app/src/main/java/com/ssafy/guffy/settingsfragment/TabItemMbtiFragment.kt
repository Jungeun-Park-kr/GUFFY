package com.ssafy.guffy.settingsfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.guffy.ApplicationClass
import com.ssafy.guffy.ApplicationClass.Companion.retrofitUserService
import com.ssafy.guffy.R
import com.ssafy.guffy.activity.MainActivity
import com.ssafy.guffy.databinding.FragmentTabItemMbtiBinding
import com.ssafy.guffy.dialog.ConfirmNoCancelDialog
import com.ssafy.guffy.models.User
import com.ssafy.guffy.util.Common
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse


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
//        var mbtiSaveBtn = view.findViewById(R.id.tab_item_mbti_save_btn)

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
                        ApplicationClass.sharedPreferences.getString("email", "").toString()
                    ).awaitResponse().body() as User

                    user.mbti = Common.mbtiList.get(clickedMBTIChip)
                    val result = retrofitUserService.updateUser(user).awaitResponse().body() as String
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