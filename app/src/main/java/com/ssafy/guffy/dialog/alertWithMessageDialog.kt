package com.ssafy.guffy.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ssafy.guffy.databinding.DialogAlertBinding
import com.ssafy.guffy.databinding.DialogAlertWithMessageBinding
import com.ssafy.guffy.databinding.DialogConfirmBinding


/***
 * 취소 없이 확인 버튼만 있는 다이얼로그
 */
class AlertWithMessageDialog(
    context: Context,
    title: String, message:String
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogAlertWithMessageBinding? = null

    private val binding get() = _binding!!


    private var id: String? = null
    private var title: String? = null
    private var message: String? = null

    init {
        this.id = id
        this.title = title
        this.message = message
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAlertWithMessageBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.alertWithMsgTitleTv.text = title
        binding.alertWithMsgMessageTv.text = message


        // 확인 버튼 클릭 - 인터페이스 등록 (나중에 호출시 구현필요함)
        binding.alertWithMsgYesBtn.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}