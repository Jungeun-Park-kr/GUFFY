package com.ssafy.guffy.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.guffy.R
import com.ssafy.guffy.activity.ChattingActivity
import com.ssafy.guffy.activity.MainActivity
import com.ssafy.guffy.databinding.FragmentLoginBinding

private lateinit var binding : FragmentLoginBinding
class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.loginJoinBtn.setOnClickListener{
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_frame_container, JoinFragment())
                .commit()

        }

        binding.loginFindPwBtn.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_frame_container, FindPwFragment())
                .commit()
        }

        binding.loginLoginbtn.setOnClickListener {
            // 메인으로 가는
//            startActivity(Intent(requireActivity(), MainActivity::class.java))
            startActivity(Intent(requireActivity(), ChattingActivity::class.java))
        }

    }
}