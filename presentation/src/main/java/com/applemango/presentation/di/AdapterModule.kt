package com.applemango.presentation.di

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.applemango.presentation.ui.screen.dialog.appliedrunner.WaitingRunnerInfoAdapter
import com.applemango.presentation.ui.screen.fragment.bookmark.BookMarkAdapter
import com.applemango.presentation.ui.screen.fragment.chat.RunningTalkAdapter
import com.applemango.presentation.ui.screen.fragment.chat.detail.RunningTalkDetailListAdapter
import com.applemango.presentation.ui.screen.fragment.chat.detail.image.detail.ImageDetailViewPagerAdapter
import com.applemango.presentation.ui.screen.fragment.chat.detail.image.preview.RunningTalkDetailImageAdapter
import com.applemango.presentation.ui.screen.fragment.main.alarm.AlarmAdapter
import com.applemango.presentation.ui.screen.fragment.main.postdetail.RunnerInfoAdapter
import com.applemango.presentation.ui.screen.fragment.map.PostAdapter
import com.applemango.presentation.ui.screen.fragment.map.address.AddressAdapter
import com.applemango.presentation.ui.screen.fragment.mypage.calendar.weekly.WeeklyCalendarAdapter
import com.applemango.presentation.ui.screen.fragment.mypage.joinedrunning.JoinedRunningPostAdapter
import com.applemango.presentation.ui.screen.fragment.mypage.mypost.accession.AttendanceAccessionAdapter
import com.applemango.presentation.ui.screen.fragment.mypage.mypost.see.AttendanceSeeAdapter
import com.applemango.presentation.ui.screen.fragment.mypage.runninglog.detail.GotStampAdapter
import com.applemango.presentation.ui.screen.fragment.mypage.runninglog.groupprofile.ProfileAdapter
import com.applemango.presentation.ui.screen.fragment.mypage.runninglog.otheruser.OtherUserJoinedPostAdapter
import com.applemango.presentation.ui.screen.fragment.mypage.setting.creator.CreatorAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
class AdapterModule {

    @Provides
    @FragmentScoped
    fun provideNavController(
        fragment: Fragment
    ) : NavController {
        return fragment.findNavController()
    }

    @Provides
    @FragmentScoped
    fun provideAddressAdapter(): AddressAdapter {
        return AddressAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideProfileAdapter(): ProfileAdapter {
        return ProfileAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideWeeklyCalendarAdapter(): WeeklyCalendarAdapter {
        return WeeklyCalendarAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideMemberStampAdapter(): GotStampAdapter {
        return GotStampAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideJoinedPostAdapter(): OtherUserJoinedPostAdapter {
        return OtherUserJoinedPostAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideJoinedRunningPostAdapter(): JoinedRunningPostAdapter {
        return JoinedRunningPostAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideBookmarkAdapter(): BookMarkAdapter {
        return BookMarkAdapter()
    }

    @Provides
    @FragmentScoped
    fun providePostAdapter(): PostAdapter {
        return PostAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideWaitingRunnerInfoAdapter() : WaitingRunnerInfoAdapter {
        return WaitingRunnerInfoAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideRunnerInfoAdapter() : RunnerInfoAdapter {
        return RunnerInfoAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideImageDetailViewPagerAdapter(): ImageDetailViewPagerAdapter {
        return ImageDetailViewPagerAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideRunningTalkAdapter(): RunningTalkAdapter {
        return RunningTalkAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideRunningTalkDetailAdapter(): RunningTalkDetailListAdapter {
        return RunningTalkDetailListAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideRunningTalkDetailImageAdapter(): RunningTalkDetailImageAdapter {
        return RunningTalkDetailImageAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideAttendanceSeeAdapter(): AttendanceSeeAdapter {
        return AttendanceSeeAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideAttendanceAccessionAdapter(): AttendanceAccessionAdapter {
        return AttendanceAccessionAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideNotificationAdapter(): AlarmAdapter {
        return AlarmAdapter()
    }

    @Provides
    @FragmentScoped
    fun provideCreatorAdapter(): CreatorAdapter = CreatorAdapter()
}