package com.applemango.runnerbe.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.applemango.runnerbe.R
import com.applemango.runnerbe.domain.entity.Pace
import com.applemango.runnerbe.presentation.screen.compose.ui.theme.pretendardTypography

@Composable
fun PaceComponentMini(pace: Pace) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color = colorResource(id = R.color.dark_g4_5))
            .padding(start = 2.dp, top = 2.dp, bottom = 2.dp, end = 4.dp)
    ) {
        Image(
            modifier = Modifier.padding(end = 2.dp), painter = painterResource(
                id = when (pace) {
                    Pace.BEGINNER -> R.drawable.ic_beginner_pace_14
                    Pace.AVERAGE -> R.drawable.ic_general_pace_14
                    Pace.HIGH -> R.drawable.ic_master_pace_14
                    Pace.MASTER -> R.drawable.ic_grand_master_pace_14
                }
            ), contentDescription = "pace icon"
        )
        Text(
            text = pace.time,
            color = colorResource(id = R.color.dark_g1),
            fontSize = 10.sp,
            style = pretendardTypography.caption
        )
    }
}