package com.ssafy.guffy.settingsfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.ssafy.guffy.R
import com.ssafy.guffy.util.Common

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TabItemInterestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TabItemInterestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_item_interest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chipGroup = view.findViewById<ChipGroup>(R.id.settings_interest_chip_group)
        makeChips(chipGroup)

    }
    private fun makeChips(chipGroup:ChipGroup) {
        var clickedInterestChipCnt = 0
        var clickedInterestChipList = mutableListOf<String>()
        for (i in Common.interstList.indices){
            // Chip 인스턴스 생성
            var chip = Chip(requireContext())
            // 칩 고유 아이디 생성
            chip.id = i
            // Chip 의 텍스트 지정
            chip.text = Common.interstList.get(i)
            // chip group 에 해당 chip 추가
            chipGroup.addView(chip)

            chip.setOnClickListener {
                if (clickedInterestChipCnt < 5 && !chip.isCheckable){
                    chip.setChipBackgroundColorResource(R.color.orange)
                    clickedInterestChipCnt++
                    clickedInterestChipList.add(Common.interstList.get(chip.id))
                }else{
                    chip.setChipBackgroundColorResource(R.color.grey)
                    clickedInterestChipCnt--
                }
            }
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TabItemInterestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}