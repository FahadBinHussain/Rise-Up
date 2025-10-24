package com.alphonyapps.riseup.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.alphonyapps.riseup.R

val laila = FontFamily(
    Font(R.font.laila_regular, FontWeight.Normal)
)

// val robotoSlab = FontFamily(
//     Font(R.font.robotoslab_regular, FontWeight.Normal)
// )

// Set of Material typography styles to start with
val Typography = Typography(
    headlineSmall = TextStyle(
        fontFamily = laila,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    titleMedium = TextStyle(
        // fontFamily = robotoSlab, // Use default for now
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )
)
