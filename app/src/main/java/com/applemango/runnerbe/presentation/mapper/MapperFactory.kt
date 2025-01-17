package com.applemango.runnerbe.presentation.mapper

import com.applemango.runnerbe.entity.AddressEntity
import com.applemango.runnerbe.entity.AlarmEntity
import com.applemango.runnerbe.entity.JoinedRunnerEntity
import com.applemango.runnerbe.entity.MonthlyRunningLogEntity
import com.applemango.runnerbe.entity.OtherUserEntity
import com.applemango.runnerbe.entity.PostingDetailEntity
import com.applemango.runnerbe.entity.PostingEntity
import com.applemango.runnerbe.entity.RunningLogDetailEntity
import com.applemango.runnerbe.entity.RunningTalkMessageEntity
import com.applemango.runnerbe.entity.RunningTalkRoomEntity
import com.applemango.runnerbe.entity.UserEntity
import com.applemango.runnerbe.presentation.model.AddressModel
import com.applemango.runnerbe.presentation.model.AlarmModel
import com.applemango.runnerbe.presentation.model.JoinedRunnerModel
import com.applemango.runnerbe.presentation.model.MonthlyRunningLogsModel
import com.applemango.runnerbe.presentation.model.OtherUserMyPageModel
import com.applemango.runnerbe.presentation.model.PostingDetailModel
import com.applemango.runnerbe.presentation.model.PostingModel
import com.applemango.runnerbe.presentation.model.RunningLogDetailModel
import com.applemango.runnerbe.presentation.model.RunningTalkMessageModel
import com.applemango.runnerbe.presentation.model.RunningTalkRoomModel
import com.applemango.runnerbe.presentation.model.UserModel

interface JoinedRunnerMapper: BaseMapper<JoinedRunnerModel, JoinedRunnerEntity>
interface RunningLogDetailMapper: BaseMapper<RunningLogDetailModel, RunningLogDetailEntity>
interface MonthlyRunningLogMapper: BaseMapper<MonthlyRunningLogsModel, MonthlyRunningLogEntity>
interface UserMapper: BaseMapper<UserModel, UserEntity>
interface OtherUserMyPageMapper: BaseMapper<OtherUserMyPageModel, OtherUserEntity>
interface AddressMapper: BaseMapper<AddressModel, AddressEntity>
interface PostingMapper: BaseMapper<PostingModel, PostingEntity>
interface PostingDetailMapper: BaseMapper<PostingDetailModel, PostingDetailEntity>
interface AlarmMapper: BaseMapper<AlarmModel, AlarmEntity>
interface RunningTalkRoomMapper: BaseMapper<RunningTalkRoomModel, RunningTalkRoomEntity>
interface RunningTalkMessageMapper: BaseMapper<RunningTalkMessageModel, RunningTalkMessageEntity>
