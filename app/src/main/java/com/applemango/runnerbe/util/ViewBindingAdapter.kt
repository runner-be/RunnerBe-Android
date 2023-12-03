package com.applemango.runnerbe.util

import android.content.ContentValues.TAG
import android.location.Address
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.applemango.runnerbe.R
import com.applemango.runnerbe.presentation.model.RunningTag
import com.applemango.runnerbe.presentation.screen.dialog.dateselect.DateSelectData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
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
fun bindDate(textView: TextView, dateString: String?) {
    runCatching {
        val temp = dateString?.replace("T", " ")?.replace("Z", " ")
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(temp)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        val format = SimpleDateFormat("M/d (E)-k-mm").format(date).split("-")
        val hour = format[1]
        textView.text = DateSelectData(
            formatDate = format[0],
            AMAndPM = if(hour.toInt() in 12 ..23) "PM" else "AM",
            hour = if(hour.toInt() >= 24) "0" else if (hour.toInt() <= 12) hour else "${hour.toInt() - 12}",
            minute = "${if(format[2].toInt() in 0..9) "0" else ""}${format[2].toInt()}"
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
        textView.text = when(it) {
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

@BindingAdapter("runner_count")
fun runnerCountText(textView: TextView, peopleNum: Int) {
    textView.text = textView.context.resources.getString(R.string.max_people_count, peopleNum.toString())
}

@BindingAdapter("gender_string")
fun genderString(textView: TextView, gender : String) {
    textView.text = if(gender == "전체") gender else textView.context.resources.getString(R.string.gender_string, gender)
}

@BindingAdapter("nick_name_text")
fun getNickNameString(textView: TextView, nickName: String?) {
    textView.text = nickName?:textView.context.resources.getString(R.string.default_nickname)
}

@BindingAdapter("gender_text", "age_text")
fun getGenderAndAgeString(textView: TextView, age: String?, gender: String?) {
    textView.text = if(age == null && gender == null) {
        ""
    } else if(age == null) {
        gender
    } else if(gender == null) {
        age
    } else {
        String.format(textView.context.resources.getString(R.string.comma_text), gender, age)
    }
}