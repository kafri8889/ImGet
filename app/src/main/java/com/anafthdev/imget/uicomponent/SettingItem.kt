package com.anafthdev.imget.uicomponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension

@Composable
fun SettingItem(
    title: String,
    summary: String,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
    value: @Composable BoxScope.() -> Unit = {},
) {

    val constraintSet = ConstraintSet {
        val (
            cTitle,
            cSummary,
            cValue,
            cContent
        ) = createRefsFor(*SettingItemComponent.entries.toTypedArray())

        constrain(cTitle) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)

            width = Dimension.fillToConstraints
        }

        constrain(cSummary) {
            top.linkTo(cTitle.bottom, 4.dp)
            start.linkTo(parent.start)
            end.linkTo(cValue.start)

            width = Dimension.fillToConstraints
        }

        constrain(cValue) {
            top.linkTo(cTitle.top)
            bottom.linkTo(cSummary.bottom)
            end.linkTo(parent.end)
        }

        constrain(cContent) {
            top.linkTo(cSummary.bottom)
            centerHorizontallyTo(parent)

            width = Dimension.matchParent
        }
    }

    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .layoutId(SettingItemComponent.Title)
        )

        Text(
            text = summary,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Light
            ),
            modifier = Modifier
                .layoutId(SettingItemComponent.Summary)
        )

        Box(
            content = value,
            modifier = Modifier
                .layoutId(SettingItemComponent.Value)
        )

        Box(
            content = content,
            modifier = Modifier
                .layoutId(SettingItemComponent.Content)
        )
    }
}

private enum class SettingItemComponent {
    Title,
    Summary,
    Value,
    Content
}
