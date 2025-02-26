package com.applemango.presentation.ui.mapper

import com.applemango.domain.entity.AddressEntity
import com.applemango.domain.entity.AlarmEntity
import com.applemango.domain.entity.JoinedRunnerEntity
import com.applemango.domain.entity.MonthlyRunningLogEntity
import com.applemango.domain.entity.OtherUserEntity
import com.applemango.domain.entity.PostingDetailEntity
import com.applemango.domain.entity.PostingEntity
import com.applemango.domain.entity.RunningLogDetailEntity
import com.applemango.domain.entity.RunningTalkMessageEntity
import com.applemango.domain.entity.RunningTalkRoomEntity
import com.applemango.domain.entity.UserEntity
import com.applemango.presentation.ui.model.AddressModel
import com.applemango.presentation.ui.model.AlarmModel
import com.applemango.presentation.ui.model.JoinedRunnerModel
import com.applemango.presentation.ui.model.MonthlyRunningLogsModel
import com.applemango.presentation.ui.model.OtherUserMyPageModel
import com.applemango.presentation.ui.model.PostingDetailModel
import com.applemango.presentation.ui.model.PostingModel
import com.applemango.presentation.ui.model.RunningLogDetailModel
import com.applemango.presentation.ui.model.RunningTalkMessageModel
import com.applemango.presentation.ui.model.RunningTalkRoomModel
import com.applemango.presentation.ui.model.UserModel

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
