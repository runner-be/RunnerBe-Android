package com.applemango.runnerbe.util

import android.annotation.SuppressLint
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.applemango.runnerbe.R
import com.applemango.runnerbe.presentation.model.vo.MapFilterData
import com.applemango.runnerbe.presentation.model.JoinedRunnerModel
import com.applemango.runnerbe.presentation.model.PostingModel
import com.applemango.runnerbe.presentation.model.type.RunnerDiligence
import com.applemango.runnerbe.presentation.model.type.RunningTag
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.dialog.stamp.getStampItemByCode
import com.applemango.runnerbe.presentation.screen.dialog.weather.WeatherItem
import com.applemango.runnerbe.presentation.screen.dialog.weather.getWeatherItemByCode
import com.applemango.runnerbe.presentation.model.type.CropRectRatio
import com.applemango.runnerbe.presentation.model.type.Pace
import com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.write.RunningLogType
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@BindingAdapter("imageDrawable")
fun bindImageFromRes(view: ImageView, drawableId: Int) {
    view.setImageResource(drawableId)
}

@BindingAdapter("profileImageFromUrl")
fun bindProfileImageFromUrl(view: ImageView, url: String?) {
    if (url.isNullOrEmpty() || url == "null") {
        Glide.with(view.context)
            .load(R.drawable.ic_user_default)
            .into(view)
    } else {
        Glide.with(view.context)
            .load(url)
            .transform(CenterCrop(), RoundedCorners(200))
            .placeholder(R.drawable.ic_user_default)
            .error(R.drawable.ic_user_default)
            .into(view)
    }

}

@BindingAdapter("date_string")
fun bindDate(textView: TextView, dateString: ZonedDateTime?) {
    runCatching {
        if (dateString == null) {
            textView.text = ""
            return
        }

        val dateFormatter = DateTimeFormatter.ofPattern("M/d (E)", Locale.KOREAN)
        val timeFormatter = DateTimeFormatter.ofPattern("a h:mm", Locale.ENGLISH)

        val formattedDate = dateString.format(dateFormatter)
        val formattedTime = dateString.format(timeFormatter)

        textView.text = "$formattedDate ${formattedTime.uppercase()}"
    }.onFailure { e ->
        textView.text = ""
        e.printStackTrace()
    }
}

@BindingAdapter("time_string")
fun bindTime(textView: TextView, dateString: String?) {
    if (dateString.isNullOrEmpty().not()) {
        textView.text = TimeString(dateString!!)
    }
}

@BindingAdapter("running_tag_string")
fun bindRunningTag(textView: TextView, runningTag: String?) {
    runningTag?.let {
        textView.text = when (it) {
            RunningTag.All.tag -> textView.resources.getString(R.string.all_work)
            RunningTag.Holiday.tag -> textView.resources.getString(R.string.holiday)
            RunningTag.After.tag -> textView.resources.getString(R.string.after_work)
            RunningTag.Before.tag -> textView.resources.getString(R.string.before_work)
            else -> it
        }
    } ?: run {
        textView.text = textView.context.resources.getString(R.string.all_work)
    }
}

@BindingAdapter("image_from_url_rounded")
fun bindImageFromURLRounded(imageView: ImageView, imageURL: String?) {
    if (imageURL.isNullOrEmpty()) {
        Glide.with(imageView.context)
            .load(R.drawable.ic_user_default)
            .into(imageView)
    } else {
        Glide.with(imageView.context)
            .load(imageURL)
            .error(R.drawable.ic_user_default)
            .transform(CenterCrop(), RoundedCorners(50))
            .into(imageView)
    }
}

@BindingAdapter("isEnabled")
fun View.isEnabled(isEnable: Boolean) {
    this.isEnabled = isEnable
}

@BindingAdapter("bind:isSelected")
fun View.isSelected(isSelected: Boolean) {
    this.isSelected = isSelected
}

@BindingAdapter("bind:imageResource")
fun ImageView.setDrawableImageResource(resourceId: Int) {
    this.setImageResource(resourceId)
}

@BindingAdapter("bind:setVisibilityInvisibleUnless")
fun setVisibilityInvisibleUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("runner_count")
fun runnerCountText(textView: TextView, peopleNum: Int) {
    textView.text =
        textView.context.resources.getString(R.string.max_people_count, peopleNum.toString())
}

@BindingAdapter("gender_string")
fun genderString(textView: TextView, gender: String) {
    textView.text = if (gender == "전체") gender else textView.context.resources.getString(
        R.string.gender_string,
        gender
    )
}

@BindingAdapter("nick_name_text")
fun getNickNameString(textView: TextView, nickName: String?) {
    textView.text = nickName ?: textView.context.resources.getString(R.string.default_nickname)
}

@BindingAdapter("gender_text", "age_text")
fun getGenderAndAgeString(textView: TextView, age: String?, gender: String?) {
    textView.text = if (age == null && gender == null) {
        ""
    } else if (age == null) {
        gender
    } else if (gender == null) {
        age
    } else {
        String.format(textView.context.resources.getString(R.string.comma_text), gender, age)
    }
}

private const val THREE_HOURS_IN_MILLIS = 3 * 60 * 60 * 1000L

@BindingAdapter("bind:whetherEndCheckStatus")
fun getWhetherEndCheckStatus(textView: TextView, post: PostingModel) {
    val resources = textView.resources
    val currentTime = System.currentTimeMillis()

    fun setTextAndColor(textResId: Int, colorResId: Int) {
        textView.text = resources.getString(textResId)
        textView.setTextColor(ResourcesCompat.getColor(resources, colorResId, null))
    }

    if (post.gatheringTime == null || post.runningTime == null) {
        if (post.whetherEnd == "N") {
            setTextAndColor(R.string.recruiting, R.color.primary)
        } else {
            setTextAndColor(R.string.recruitment_deadline, R.color.dark_g3)
        }
        return
    }

    val startTime = dateStringToLongTime(post.gatheringTime)
    val runningTime = timeStringToLongTime(post.runningTime)

    when {
        currentTime < startTime -> {
            if (post.whetherEnd == "N") {
                setTextAndColor(R.string.recruiting, R.color.primary)
            } else {
                setTextAndColor(R.string.recruitment_deadline, R.color.dark_g3)
            }
        }

        currentTime < startTime + THREE_HOURS_IN_MILLIS + runningTime -> {
            setTextAndColor(R.string.recruitment_deadline, R.color.dark_g3)
        }

        else -> {
            setTextAndColor(R.string.recruitment_end, R.color.dark_g3)
        }
    }
}

@BindingAdapter("bind:afterPartyStatus")
fun getAfterPartyStatus(textView: TextView, isAfterParty: Int) {
    textView.text =
        textView.resources.getString(if (isAfterParty == 1) R.string.after_party_exist else R.string.after_party_not_exist)
}

@BindingAdapter("bind:filterRedDotVisibility")
fun ImageView.filterVisibility(filterData: MapFilterData) {
    val visibility = if (MapFilterData.isFilterApplied(filterData)) {
        View.VISIBLE
    } else {
        View.GONE
    }
    this.visibility = visibility
}

@BindingAdapter("bind:backgroundTop")
fun View.backgroundTop(panelTop: Int?) {
    val resource = if (panelTop == 0) R.drawable.bg_top_rectangle else R.drawable.bg_top_rounded
    this.setBackgroundResource(resource)
}

@BindingAdapter("bind:locationBtnVisibility")
fun ImageView.setLocationBtnVisibility(panelTop: Int?) {
    this.visibility = if ((panelTop ?: 0) < 150) View.GONE else View.VISIBLE
}

@BindingAdapter("bind:mapRefreshBtnVisibility")
fun LinearLayoutCompat.setMapRefreshBtnVisibility(panelTop: Int?) {
    this.visibility = if ((panelTop ?: 0) < 150) View.GONE else View.VISIBLE
}

@BindingAdapter("bind:paceText")
fun TextView.getPaceImage16(pace: String?) {
    this.text = Pace.getPaceByName(pace)?.time
}

@BindingAdapter("bind:paceImage16")
fun ImageView.getPaceImage16(pace: String?) {
    this.isVisible = pace != null
    this.setImageResource(
        when (pace) {
            Pace.BEGINNER.key -> R.drawable.ic_beginner_pace //입문
            Pace.AVERAGE.key -> R.drawable.ic_general_pace //평균
            Pace.HIGH.key -> R.drawable.ic_master_pace//고수
            else -> R.drawable.ic_grand_matser_pace //초고수
        }
    )
}

@BindingAdapter("bind:attendanceImage16")
fun ImageView.getAttendanceImage16(diligence: String?) {
    this.setImageResource(
        when (diligence) {
            RunnerDiligence.SINCERITY_RUNNER.key -> R.drawable.ic_attendance_grade_a
            RunnerDiligence.BEGINNER_RUNNER.key, null -> R.drawable.ic_attendance_grade_b
            RunnerDiligence.EFFORT_RUNNER.key -> R.drawable.ic_attendance_grade_c
            else -> R.drawable.ic_attendance_grade_d
        }
    )
}

@BindingAdapter("bind:paceText")
fun TextView.getPaceText(pace: String?) {
    this.isVisible = pace != null
    this.text = when (pace) {
        Pace.BEGINNER.key -> Pace.BEGINNER.time //입문
        Pace.AVERAGE.key -> Pace.AVERAGE.time //평균
        Pace.HIGH.key -> Pace.HIGH.time //고수
        else -> Pace.MASTER.time //초고수
    }
}

@BindingAdapter("bind:isVisible")
fun View.visible(isVisible: Boolean) {
    this.isVisible = isVisible
}

@BindingAdapter("bind:glideProfileImageFromUrl")
fun ImageView.setImageUrl(url: String?) {
    val image = url ?: R.drawable.ic_user_default
    Glide.with(this)
        .load(image)
        .error(R.drawable.ic_user_default)
        .into(this)
}

@BindingAdapter("bind:glideImageFromUri")
fun ImageView.setImageUri(uri: Uri?) {
    Glide.with(this)
        .load(uri)
        .into(this)
}

@BindingAdapter("bind:isSwitchChecked")
fun SwitchCompat.isChecked(isChecked: Boolean?) {
    if (isChecked == null) this.isChecked = true
    else this.isChecked = isChecked
}

@BindingAdapter("bind:logTeamSrc")
fun ImageView.setLogTeamSrc(type: RunningLogType?) {
    when (type) {
        null -> {
            setImageResource(R.drawable.ic_team_lock)
        }

        RunningLogType.ALONE -> setImageResource(R.drawable.ic_team_lock)
        RunningLogType.TEAM -> setImageResource(R.drawable.ic_team_default)
    }
}

@BindingAdapter("bind:stampSrc", "bind:isTeamLog")
fun ImageView.setStampImageSrc(stampItem: StampItem?, gatheringId: Int?) {
    if (stampItem == null) {
        if (gatheringId == null) {
            setImageResource(R.drawable.ic_stamp_unavailable)
        } else {
            setImageResource(R.drawable.ic_stamp_available)
        }
    } else {
        setImageResource(stampItem.image)
    }
}

@BindingAdapter("bind:stampText")
fun TextView.setStampText(stampItem: StampItem?) {
    if (stampItem == null) {
        this.text = context.getString(R.string.running_log_add_stamp)
    } else {
        this.text = stampItem.name
    }
}

@BindingAdapter("bind:weatherSrc")
fun ImageView.setWeatherImageSrc(weatherItem: WeatherItem?) {
    val icon = weatherItem?.image ?: R.drawable.ic_weather_default
    Glide.with(context)
        .load(icon)
        .into(this)
}

@BindingAdapter("bind:logVisibility")
fun SwitchCompat.setLogVisibility(visibility: Boolean) {
    isChecked = visibility
}

@BindingAdapter("bind:bookmarkSrc")
fun ImageView.setBookmarkImageSrc(isBookmarked: Boolean) {
    val src = if (isBookmarked) {
        R.drawable.ic_book_mark_on
    } else {
        R.drawable.ic_book_mark_off
    }
    setImageResource(src)
}

@BindingAdapter("bind:cropRatioBackground")
fun TextView.setSelectedRatioBackground(ratio: CropRectRatio) {
    val background = if (this.id == ratio.viewId) {
        R.drawable.bg_primary_stroke_radius_10
    } else {
        0
    }
    this.setBackgroundResource(background)
}

@BindingAdapter("bind:setPostMinMaxGuideVisibility")
fun TextView.setPostWriteAttendanceGuideVisibility(size: Int) {
    this.visibility = if (size == 2 || size == 8) View.VISIBLE else View.GONE
}

@BindingAdapter("bind:setPostMinMaxGuideText")
fun TextView.setPostWriteAttendanceGuide(size: Int) {
    this.text = when (size) {
        2 -> context.getString(R.string.msg_recruitment_count_minimum)
        8 -> context.getString(R.string.msg_recruitment_count_maximum)
        else -> ""
    }
}

// 러닝 로그
@BindingAdapter("bind:logDateText")
fun TextView.setLogDateText(date: ZonedDateTime?) {
    if (date == null) return
    this.text = parseLocalDateToKorean(date.toLocalDate())
}

@BindingAdapter("bind:logStampSrc")
fun ImageView.setLogStampSrc(stampCode: String?) {
    if (stampCode == null) return
    val stamp = getStampItemByCode(stampCode)
    stamp?.image?.let {
        this.setImageResource(it)
    }
}

@BindingAdapter("bind:logStampText")
fun TextView.setLogStampText(stampCode: String?) {
    if (stampCode == null) return
    val stamp = getStampItemByCode(stampCode)
    this.text = stamp?.name ?: ""
}

@BindingAdapter("bind:logContentImageSrc")
fun ImageView.setLogContentImageSrc(url: String?) {
    if (url == null) {
        this.visibility = View.GONE
    } else {
        this.visibility = View.VISIBLE
        Glide.with(this.context)
            .load(url)
            .into(this)
    }
}

@BindingAdapter("bind:logWeatherImageSrc")
fun ImageView.setLogWeatherImageSrc(weatherCode: String?) {
    if (weatherCode == null) return
    val weatherItem = getWeatherItemByCode(weatherCode)
    Glide.with(this.context)
        .load(weatherItem.image)
        .into(this)
}

@BindingAdapter("bind:logTeamSizeText")
fun TextView.setLogStampText(size: Int?) {
    if (size == null) this.text = "0 명"
    else this.text = context.getString(R.string.running_log_team_size, size)
}

@BindingAdapter("bind:logReceivedStampTitleText")
fun TextView.setLogReceivedStampTitleText(nickname: String?) {
    if (nickname == null) return
    else this.text = context.getString(R.string.running_log_got_stamp, nickname)
}

@BindingAdapter("bind:logIsVisible")
fun SwitchCompat.setLogIsVisible(isOpened: Int?) {
    if (isOpened == null) return
    val visibility = isOpened == 1
    this.isChecked = visibility
}

@BindingAdapter("bind:logTeamImageSrc")
fun ImageView.setLogTeamImage(gatheringId: Int?) {
    when (gatheringId == null) {
        true -> setImageResource(R.drawable.ic_team_lock)
        false -> setImageResource(R.drawable.ic_team_default)
    }
}

// 게시글
@BindingAdapter("bind:postAddressText")
fun TextView.setPostAddressText(address: String?) {
    this.text = address ?: "주소 추가 필요"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("bind:postPlaceAddressText")
fun TextView.setPostAddressText(posting: PostingModel?) {
    this.text = "${posting?.placeAddress} ${posting?.placeExplain}"
}

// 알림
@BindingAdapter("bind:alarmBackground")
fun View.setAlarmBackground(whetherRead: String) {
    if (whetherRead != "Y") {
        this.setBackgroundColor(context.getColor(R.color.dark_g7))
    } else {
        this.setBackgroundColor(context.getColor(R.color.primary_glow))
    }
}

// 참여한 러닝
@BindingAdapter("bind:logWriteOrSeeText")
fun TextView.setLogWriteOrSeeText(posting: PostingModel) {
    val logId = posting.logId
    this.text =if (logId == null) {
         "로그 쓰기"
    } else "로그 보기"
}

// 함께한 러너
@BindingAdapter("bind:groupLogSeeCtrl")
fun TextView.setGroupLogSee(joinedRunnerResult: JoinedRunnerModel) {
    if (joinedRunnerResult.logId != null) {
        this.visibility = View.VISIBLE
        if (joinedRunnerResult.isOpened == 1) {
            with(this) {
                setBackgroundResource(R.drawable.bg_dark_5_5g_solid_radius_8)
                isEnabled = true
                text = "로그 보기"
            }
        } else {
            with(this) {
                setBackgroundResource(R.drawable.bg_dark_6g_solid_radius_8)
                isEnabled = false
                text = "비공개"
            }
        }
    } else {
        this.visibility = View.GONE
    }
}