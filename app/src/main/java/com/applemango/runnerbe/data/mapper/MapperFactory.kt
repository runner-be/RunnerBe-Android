package com.applemango.runnerbe.data.mapper

import com.applemango.runnerbe.data.dto.new.AlarmsDto
import com.applemango.runnerbe.data.dto.new.CommonDto
import com.applemango.runnerbe.data.dto.new.BookmarksDto
import com.applemango.runnerbe.data.dto.new.JoinedRunnerDto
import com.applemango.runnerbe.data.dto.new.MonthlyRunningLogDto
import com.applemango.runnerbe.data.dto.new.NewUserDto
import com.applemango.runnerbe.data.dto.new.OtherUserDto
import com.applemango.runnerbe.data.dto.new.PostingDetailDto
import com.applemango.runnerbe.data.dto.new.PostingDto
import com.applemango.runnerbe.data.dto.new.ProfileUrl
import com.applemango.runnerbe.data.dto.new.RunningLogDetailDto
import com.applemango.runnerbe.data.dto.new.RunningTalkMessagesDto
import com.applemango.runnerbe.data.dto.new.RunningTalkRoomsDto
import com.applemango.runnerbe.data.dto.new.SocialLoginDto
import com.applemango.runnerbe.data.dto.new.UserDto
import com.applemango.runnerbe.data.network.request.AttendanceAccessionRequest
import com.applemango.runnerbe.data.network.request.WriteRunningRequest
import com.applemango.runnerbe.entity.AlarmEntity
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.BookmarksEntity
import com.applemango.runnerbe.entity.JoinedRunnersEntity
import com.applemango.runnerbe.entity.MonthlyRunningLogEntity
import com.applemango.runnerbe.entity.NewUserEntity
import com.applemango.runnerbe.entity.OtherUserEntity
import com.applemango.runnerbe.entity.PostingDetailEntity
import com.applemango.runnerbe.entity.PostingEntity
import com.applemango.runnerbe.entity.ProfileUrlEntity
import com.applemango.runnerbe.entity.RunningLogDetailEntity
import com.applemango.runnerbe.entity.RunningTalkMessagesEntity
import com.applemango.runnerbe.entity.RunningTalkRoomsEntity
import com.applemango.runnerbe.entity.SocialLoginEntity
import com.applemango.runnerbe.entity.UserEntity
import com.applemango.runnerbe.usecaseImpl.post.AttendanceAccessionUseCase.AttendanceAccessionParam
import com.applemango.runnerbe.usecaseImpl.post.WritePostUseCase.WriteRunningParam

interface CommonMapper: BaseMapper<CommonDto, CommonEntity>
interface UserMapper: BaseMapper<UserDto, UserEntity>
interface ProfileUrlMapper: BaseMapper<ProfileUrl, ProfileUrlEntity>
interface PostingMapper: BaseMapper<PostingDto, PostingEntity>
interface PostingDetailMapper: BaseMapper<PostingDetailDto, PostingDetailEntity>
interface BookmarksMapper: BaseMapper<BookmarksDto, BookmarksEntity>
interface WriteRunningMapper: BaseMapper<WriteRunningRequest, WriteRunningParam>
interface JoinedRunnerAttendanceMapper: BaseMapper<AttendanceAccessionRequest, AttendanceAccessionParam>
interface JoinedRunnersMapper: BaseMapper<List<JoinedRunnerDto>, List<JoinedRunnersEntity>>
interface MonthlyRunningLogMapper: BaseMapper<MonthlyRunningLogDto, MonthlyRunningLogEntity>
interface RunningLogDetailMapper: BaseMapper<RunningLogDetailDto, RunningLogDetailEntity>
interface AlarmMapper: BaseMapper<AlarmsDto, List<AlarmEntity>>
interface SocialLoginMapper: BaseMapper<SocialLoginDto, SocialLoginEntity>
interface NewUserMapper: BaseMapper<NewUserDto, NewUserEntity>
interface OtherUserMapper: BaseMapper<OtherUserDto, OtherUserEntity>

interface RunningTalkRoomsMapper: BaseMapper<RunningTalkRoomsDto, RunningTalkRoomsEntity>
interface RunningTalkMessagesMapper: BaseMapper<RunningTalkMessagesDto, RunningTalkMessagesEntity>

