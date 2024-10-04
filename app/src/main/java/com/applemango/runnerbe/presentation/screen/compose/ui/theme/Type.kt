package com.applemango.runnerbe.presentation.screen.compose.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.applemango.runnerbe.R

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val aggro = FontFamily(
    Font(R.font.sb_aggro_otf_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.sb_aggro_otf_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.sb_aggro_otf_bold, FontWeight.Bold, FontStyle.Normal)
)

val notoSans = FontFamily(
    Font(R.font.noto_sans_cj_kkr_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.noto_sans_cj_kkr_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.noto_sans_cj_kkr_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.noto_sans_cj_kkr_regular, FontWeight.Normal, FontStyle.Normal)
)

val pretendard = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.pretendard_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.pretendard_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.pretendard_thin, FontWeight.Thin, FontStyle.Normal)
)

val pretendardTypography = Typography(
    caption = TextStyle(
        fontFamily = pretendard,
        fontWeight = FontWeight.Normal
    )
)