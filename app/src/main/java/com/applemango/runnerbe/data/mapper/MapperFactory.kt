package com.applemango.runnerbe.data.mapper

import com.applemango.runnerbe.data.dto.AlarmsDto
import com.applemango.runnerbe.data.dto.CommonDto
import com.applemango.runnerbe.data.dto.JoinedRunnerDto
import com.applemango.runnerbe.data.dto.MonthlyRunningLogDto
import com.applemango.runnerbe.data.dto.MyPageDto
import com.applemango.runnerbe.data.dto.NewUserDto
import com.applemango.runnerbe.data.dto.OtherUserDto
import com.applemango.runnerbe.data.dto.PostingDetailDto
import com.applemango.runnerbe.data.dto.PostingDto
import com.applemango.runnerbe.data.dto.ProfileUrl
import com.applemango.runnerbe.data.dto.RunningLogDetailDto
import com.applemango.runnerbe.data.dto.RunningTalkMessageDto
import com.applemango.runnerbe.data.dto.RunningTalkRoomDto
import com.applemango.runnerbe.data.dto.SocialLoginDto
import com.applemango.runnerbe.data.dto.UserDto
import com.applemango.runnerbe.entity.AlarmEntity
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.JoinedRunnerEntity
import com.applemango.runnerbe.entity.MonthlyRunningLogEntity
import com.applemango.runnerbe.entity.MyPageEntity
import com.applemango.runnerbe.entity.NewUserEntity
import com.applemango.runnerbe.entity.OtherUserEntity
import com.applemango.runnerbe.entity.PostingDetailEntity
import com.applemango.runnerbe.entity.PostingEntity
import com.applemango.runnerbe.entity.ProfileUrlEntity
import com.applemango.runnerbe.entity.RunningLogDetailEntity
import com.applemango.runnerbe.entity.RunningTalkMessageEntity
import com.applemango.runnerbe.entity.RunningTalkRoomEntity
import com.applemango.runnerbe.entity.SocialLoginEntity
import com.applemango.runnerbe.entity.UserEntity

interface CommonMapper: BaseMapper<CommonDto, CommonEntity>
interface UserMapper: BaseMapper<UserDto, UserEntity>
interface MyPageMapper: BaseMapper<MyPageDto, MyPageEntity>
interface ProfileUrlMapper: BaseMapper<ProfileUrl, ProfileUrlEntity>
interface PostingMapper: BaseMapper<PostingDto, PostingEntity>
interface PostingDetailMapper: BaseMapper<PostingDetailDto, PostingDetailEntity>
interface JoinedRunnerMapper: BaseMapper<JoinedRunnerDto, JoinedRunnerEntity>
interface MonthlyRunningLogMapper: BaseMapper<MonthlyRunningLogDto, MonthlyRunningLogEntity>
interface RunningLogDetailMapper: BaseMapper<RunningLogDetailDto, RunningLogDetailEntity>
interface AlarmMapper: BaseMapper<AlarmsDto, List<AlarmEntity>>
interface SocialLoginMapper: BaseMapper<SocialLoginDto, SocialLoginEntity>
interface NewUserMapper: BaseMapper<NewUserDto, NewUserEntity>
interface OtherUserMapper: BaseMapper<OtherUserDto, OtherUserEntity>
interface RunningTalkRoomMapper: BaseMapper<RunningTalkRoomDto, RunningTalkRoomEntity>
interface RunningTalkMessageMapper: BaseMapper<RunningTalkMessageDto, RunningTalkMessageEntity>
