package com.ssafy.guffy.mainfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.ssafy.guffy.R
import com.ssafy.guffy.activity.MainActivity
import com.ssafy.guffy.databinding.FragmentSettingsBinding
import com.ssafy.guffy.settingsfragment.TabItemInterestFragment
import com.ssafy.guffy.settingsfragment.TabItemMbtiFragment
import com.ssafy.guffy.settingsfragment.TabItemPwFragment

class SettingsFragment : Fragment() { // 이유 모름 바인딩 안먹힘
    private lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = context as MainActivity

        // default 비밀번호 수정
        childFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout_settings, TabItemPwFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logoutBtn = view.findViewById<MaterialButton>(R.id.settings_logout_btn)
        val tabLayout = view.findViewById<TabLayout>(R.id.settings_tab_layout)

        // 로그아웃 버튼 클릭
        logoutBtn.setOnClickListener {
            mainActivity.logout()
        }

        // 탭 아이템 변경 리스너
        tabLayout.addOnTabSelectedListener( object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position){
                    0 -> moveFragment(1) // 비밀번호 변경
                    1 -> moveFragment(2) // 관심사 변경
                    2 -> moveFragment(3) // mbti 변경
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun moveFragment(index:Int) {
        val transaction = childFragmentManager.beginTransaction()
        when(index) {
            1-> { // 비밀번호 수정 탭아이템
                transaction.replace(R.id.frame_layout_settings, TabItemPwFragment())
            }
            2 -> { // 내관심사 수정
                transaction.replace(R.id.frame_layout_settings, TabItemInterestFragment())
            }
            3-> { // mbti 수정
                transaction.replace(R.id.frame_layout_settings, TabItemMbtiFragment())
            }
        }
        transaction.commit()
    }

}