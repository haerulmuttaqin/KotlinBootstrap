package com.stevdza.san.kotlinbs.forms

import androidx.compose.runtime.*
import com.stevdza.san.kotlinbs.components.BSButton
import com.stevdza.san.kotlinbs.components.SpanText
import com.stevdza.san.kotlinbs.models.InputSize
import com.stevdza.san.kotlinbs.models.button.ButtonSize
import com.stevdza.san.kotlinbs.models.button.ButtonVariant
import com.stevdza.san.kotlinbs.util.UniqueIdGenerator
import com.varabyte.kobweb.browser.file.loadDataUrlFromDisk
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import kotlinx.browser.document
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Text

/**
 * UI component that allows users to upload files from their local system
 * to a web application. It provides a button and an input field where you can see
 * the name of the selected file.
 * @param id Unique identifier of the parent.
 * @param label File Input label that is shown on top of the component.
 * @param placeholder A placeholder text that will show up when a file is not selected.
 * @param size The size of the File Input.
 * @param disabled Whether this component is disabled or not.
 * @param accept A string value that you can use to specify which type is accepted/selectable.
 * @param onFileSelected Lambda which is triggered when a user selects a file. The first
 * string represents a file name, while the second one represents the actual BASE_64
 * encoded file.
 * */
@Composable
fun BSFileInput(
    modifier: Modifier = Modifier,
    id: String? = null,
    label: String? = null,
    placeholder: String = "No file selected.",
    size: InputSize = InputSize.Default,
    disabled: Boolean = false,
    accept: String = "image/png, image/jpeg",
    onFileSelected: (String, String) -> Unit
) {
    val randomId = remember {
        id ?: UniqueIdGenerator.generateUniqueId("fileinput")
    }
    var placeholderText by remember { mutableStateOf(placeholder) }
    Div(attrs = modifier.toAttrs()) {
        if(label != null) {
            Label(
                attrs = Modifier
                    .classNames("form-label")
                    .toAttrs(),
                forId = randomId
            )
            {
                Text(value = label)
            }
        }
        Row(
            modifier = Modifier
                .id(randomId)
                .thenIf(
                    condition = disabled,
                    // TODO: Will this color get used anywhere? Should we extract it into a constant?
                    other = Modifier.backgroundColor(Color.rgb(0xFAFAFA))
                )
                .border(
                    width = 1.px,
                    style = LineStyle.Solid,
                    color = rgba(r = 206, g = 212, b = 218, a = 1)
                )
                .padding(all = 0.px)
                .onClick {
                    if (!disabled) {
                        document.loadDataUrlFromDisk(
                            accept = accept,
                            onLoad = {
                                onFileSelected(filename, it)
                                placeholderText = filename
                            }
                        )
                    }
                }
                .overflow(Overflow.Hidden)
                .textOverflow(TextOverflow.Ellipsis)
                .thenIf(
                    condition = size != InputSize.Default,
                    other = Modifier.classNames(size.value)
                )
                .borderRadius(0.375.cssRem),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BSButton(
                modifier = Modifier
                    .margin(all = 0.px)
                    .borderRadius(topRight = 0.px, bottomRight = 0.px),
                text = "Browse...",
                variant = ButtonVariant.Light,
                size = when (size) {
                    InputSize.Default -> {
                        ButtonSize.Default
                    }

                    InputSize.Small -> {
                        ButtonSize.Small
                    }

                    InputSize.Large -> {
                        ButtonSize.Large
                    }
                },
                disabled = disabled,
                onClick = {}
            )
            SpanText(
                modifier = Modifier
                    .thenIf(
                        condition = disabled,
                        other = Modifier.classNames("text-muted")
                    )
                    .margin(leftRight = 12.px),
                text = placeholderText
            )
        }
    }
}