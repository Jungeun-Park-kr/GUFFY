package com.ssafy.guffy.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.guffy.R
import com.ssafy.guffy.databinding.FragmentFindPwBinding

private const val TAG = "FindPwFragment 구피"
private lateinit var binding : FragmentFindPwBinding
class FindPwFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindPwBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.findpwTempPwSendBtn.setOnClickListener {
            showDialog()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_frame_container, LoginFragment())
                .commit()
        }

    }

    // 다이어로그 생성 함수
    fun showDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder
            .setTitle("비밀번호 찾기")
            .setMessage("회원가입한 이메일로 임시 비밀번호가 전송되었습니다.")
            .setPositiveButton("OK"){dialog, which->
                Log.d(TAG, "showDialog: 확인버튼 누름")
            }
            .create()
            .show()

    }
}