package com.applemango.runnerbe.di

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.applemango.runnerbe.presentation.screen.fragment.map.address.AddressAdapter
import com.applemango.runnerbe.presentation.screen.fragment.mypage.JoinedRunningPostAdapter
import com.applemango.runnerbe.presentation.screen.fragment.mypage.calendar.WeeklyCalendarAdapter
import com.applemango.runnerbe.presentation.screen.fragment.mypage.otheruser.OtherUserJoinedPostAdapter
import com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.GotStampAdapter
import com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.ProfileAdapter
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
}