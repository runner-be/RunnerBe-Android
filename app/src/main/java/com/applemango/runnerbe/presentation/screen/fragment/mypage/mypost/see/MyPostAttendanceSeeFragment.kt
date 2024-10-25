package com.applemango.runnerbe.presentation.screen.fragment.mypage.mypost.see

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentMyPostAttendanceSeeBinding
import com.applemango.runnerbe.presentation.screen.deco.RecyclerViewItemDeco
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment

class MyPostAttendanceSeeFragment : BaseFragment<FragmentMyPostAttendanceSeeBinding>(R.layout.fragment_my_post_attendance_see) {
    private val viewModel: MyPostAttendanceSeeViewModel by viewModels()
    private val args : MyPostAttendanceSeeFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.userListUpdate(args.users.toList())
        context?.let {
            binding.attendanceSeeRecyclerView.addItemDecoration(RecyclerViewItemDeco(it, 18))
        }
    }
}