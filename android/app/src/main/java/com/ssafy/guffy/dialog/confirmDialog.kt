package com.ssafy.guffy.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ssafy.guffy.databinding.DialogConfirmBinding

/***
 * 취소, 확인 버튼 있는 다이얼로그
 */
//TODO  Confirm Dialog 사용법
/**
 * dialog.show(manager: FragmentManager, tag: String) 호출 시, manager로 넘겨줄 값 !
        activity ➡ this.supportFragmentManager
        fragment ➡ activity?.supportFragmentManager!!
    로 사용하면 된다.

    *** 사용 예시 (https://velog.io/@dear_jjwim/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%BB%A4%EC%8A%A4%ED%85%80-%EB%8B%A4%EC%9D%B4%EC%96%BC%EB%A1%9C%EA%B7%B8Custom-Dialog-%EB%A7%8C%EB%93%A4%EA%B8%B0-ClickListener%EB%A1%9C-%ED%81%B4%EB%A6%AD-%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EC%A0%95%EC%9D%98%EA%B9%8C%EC%A7%80)
    binding.deleteFriendBtn.onclick() {
        val dialog = ConfirmDialog(this, "친구 삭제", "진짜로 삭제하시겠습니까?", pkgId)
        // 알림창이 띄워져있는 동안 배경 클릭 막기
        dialog.isCancelable = false
        dialog.show(this.supportFragmentManager, "ConfirmDialog")
    }
    override fun onYesButtonClick(id: Int) { // id 값 필요도 없음 걍 0값 넣어주면 됨
        // 액티비티 종료를 원한다면 finish()를 호출
        deletePackageApiCall(id)
    }

 */
class ConfirmDialog(
    confirmDialogInterface: ConfirmDialogInterface,
    title: String, message:String, id: String
) : DialogFragment() {

    // 뷰 바인딩 정의
    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!

    private var confirmDialogInterface: ConfirmDialogInterface? = null

    private var id: String? = null
    private var title: String? = null
    private var message: String? = null

    init {
        this.title = title
        this.message = message
        this.id = id
        this.confirmDialogInterface = confirmDialogInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogConfirmBinding.inflate(inflater, container, false)
        val view = binding.root

        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.confirmTitleTv.text = title
        binding.confirmMessageTv.text = message


        // 취소 버튼 클릭 : 다이얼로그 닫기
        binding.confirmNoBtn.setOnClickListener {
            dismiss()
        }

        // 확인 버튼 클릭 - 인터페이스 등록 (나중에 호출시 구현필요함)
        binding.confirmYesBtn.setOnClickListener {
            this.confirmDialogInterface?.onYesButtonClick(id!!) // 파라미터 필요할 경우 대비해서 생성함 (걍 창 종료만 할거면 아무값이나 넣어도 됨)
            dismiss()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface ConfirmDialogInterface {
    fun onYesButtonClick(id: String) // 확인 버튼 클릭을 처리해줄 인터페이스 (호출한 곳에서 나중에 구현해줘야 합니다!)
}