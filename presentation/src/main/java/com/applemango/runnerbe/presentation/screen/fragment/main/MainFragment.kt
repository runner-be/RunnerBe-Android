package com.applemango.runnerbe.presentation.screen.fragment.main

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.applemango.runnerbe.R
import com.applemango.runnerbe.databinding.FragmentMainBinding
import com.applemango.runnerbe.databinding.ItemTabListBinding
import com.applemango.runnerbe.presentation.model.type.MainBottomTab
import com.applemango.runnerbe.presentation.model.type.Pace
import com.applemango.runnerbe.presentation.screen.fragment.base.BaseFragment
import com.applemango.runnerbe.presentation.screen.fragment.map.RunnerMapViewModel
import com.applemango.runnerbe.util.MainFragmentPageAdapter
import com.applemango.runnerbe.util.imageSrcCompatResource
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 메인 탭 페이지 전체 프래그먼트를 관리합니다.
 * author: niaka
 */
@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private var tabIconIdList: List<Int> = MainBottomTab.values().map { it.iconResourceId }
//    private var postDetailDialog: PostDetailSheetDialog? = null

    private val viewModel: RunnerMapViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageSetting()
        observeBind()
        mainViewModel.fetchUserId()

        setFragmentResultListener("filter") { _, bundle ->
            val paces: List<Pace> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelableArray("paces", Pace::class.java)?.toList()
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelableArray("paces")?.filterIsInstance<Pace>()
            } ?: emptyList()

            Log.e("MainFragment", paces.toString())

            viewModel.setFilter(
                paces = paces,
                gender = bundle.getString("gender"),
                afterParty = bundle.getString("afterParty"),
                jobTag = bundle.getString("job"),
                minAge = bundle.getInt("minAge"),
                maxAge = bundle.getInt("maxAge")
            )
        }
    }

    private fun observeBind() {
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.currentItem.collect {
                binding.fragmentBodyPager.currentItem = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isShowInfoDialog.collect {
                if (it) checkAdditionalUserInfo(mainViewModel.userId.value)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.clickedPost.collectLatest {
                if (it != null) {
                    navigate(
                        MainFragmentDirections.actionMainFragmentToPostDetailFragment(it)
                    )
                    mainViewModel.clickedPost.value = null
                }
            }
        }
    }

    /**
     * 혹시 추후에 바텀 탭 이미지가 변경되는 경우 사용할 수 있도록 커스텀 layout 사용하는 방식으로 진행했습니다.
     * 나중에 이미지가 변경되지 않더라도 compose UI로 분사할 때 효율적으로 변경할 수 있습니다!
     */
    private fun pageSetting() {
        binding.fragmentBodyPager.adapter = MainFragmentPageAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle,
            tabIconIdList
        )
        binding.fragmentBodyPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.fragmentBodyPager) { tab, position ->
            DataBindingUtil.bind<ItemTabListBinding>(
                LayoutInflater.from(requireContext()).inflate(R.layout.item_tab_list, null)
            )?.apply {
                this.icon.imageSrcCompatResource(tabIconIdList[position])
            }?.run { tab.customView = this.root }
        }.attach()
    }
}