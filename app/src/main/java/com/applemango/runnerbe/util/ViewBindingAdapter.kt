package com.applemango.runnerbe.util

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.applemango.runnerbe.R
import com.applemango.runnerbe.data.dto.Posting
import com.applemango.runnerbe.domain.entity.Pace
import com.applemango.runnerbe.presentation.model.RunnerDiligence
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.screen.dialog.dateselect.DateSelectData
import com.applemango.runnerbe.presentation.screen.dialog.stamp.StampItem
import com.applemango.runnerbe.presentation.screen.dialog.weather.WeatherItem
import com.applemango.runnerbe.presentation.screen.fragment.mypage.runninglog.write.RunningLogType
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.util.Calendar

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
        val time = dateString?.toInstant()?.toEpochMilli()
        val format = SimpleDateFormat("M/d (E)-k-mm").format(time).split("-")
        val hour = format[1]
        textView.text = DateSelectData(
            formatDate = format[0],
            AMAndPM = if (hour.toInt() in 12..23) "PM" else "AM",
            hour = if (hour.toInt() >= 24) "0" else if (hour.toInt() <= 12) hour else "${hour.toInt() - 12}",
            minute = "${if (format[2].toInt() in 0..9) "0" else ""}${format[2].toInt()}"
        ).getFullDisplayDate()
    }.onFailure {
        textView.text = ""
        it.printStackTrace()
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
fun getWhetherEndCheckStatus(textView: TextView, post: Posting) {
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

@BindingAdapter("bind:paceImage16")
fun ImageView.getPaceImage16(pace: String?) {
    this.isVisible = pace != null
    this.setImageResource(
        when (pace) {
            Pace.BEGINNER.key -> R.drawable.ic_beginner_pace //입문
            Pace.AVERAGE.key -> R.drawable.ic_general_pace //평균
            Pace.HIGH.key -> R.drawable.ic_master_pace//고수
            else -> R.drawable.ic_master_pace //초고수
        }
    )
}

@BindingAdapter("bind:attendanceImage16")
fun ImageView.getAttendanceImage16(attendande: String?) {
    this.isVisible = attendande != null
    this.setImageResource(
        when (attendande) {
            RunnerDiligence.EFFORT_RUNNER.value -> {
                R.drawable.ic_effort_runner_face
            }

            RunnerDiligence.ERROR_RUNNER.value -> {
                R.drawable.ic_error_runner_face
            }

            RunnerDiligence.SINCERITY_RUNNER.value -> {
                R.drawable.ic_sincerity_runner_face
            }

            else -> {
                R.drawable.ic_effort_runner_face
            }
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

@BindingAdapter("bind:stampSrc")
fun ImageView.setStampImageSrc(stampItem: StampItem?) {
    if (stampItem == null) {
        setImageResource(R.drawable.ic_stamp_unavailable)
    } else {
        setImageResource(stampItem.image)
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