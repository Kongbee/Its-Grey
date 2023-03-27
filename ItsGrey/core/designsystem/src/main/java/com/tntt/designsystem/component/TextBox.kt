package com.tntt.designsystem.component

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import com.tntt.designsystem.theme.IgTheme
import com.tntt.model.BoxData
import com.tntt.model.BoxState
import com.tntt.model.TextBoxInfo

data class TextBoxData(
    val text: String,
    val fontSize: Float,
)

@Composable
fun TextBox(
    parent: Rect,
    textBoxInfo: TextBoxInfo,
) {

    val position = remember(parent) {
        mutableStateOf(
            Offset(
                textBoxInfo.boxData.offsetRatioX * parent.width,
                textBoxInfo.boxData.offsetRatioY * parent.height
            )
        )
    }
    val size = remember(parent) {
        mutableStateOf(
            Size(
                textBoxInfo.boxData.widthRatio * parent.width,
                textBoxInfo.boxData.heightRatio * parent.height
            )
        )
    }
    val fontSize = remember(parent, textBoxInfo) {
        mutableStateOf(
            textBoxInfo.fontSizeRatio * textBoxInfo.boxData.widthRatio * parent.width
        )
    }

    Box(
        position = position.value,
        size = size.value,
    ) {
        Text(
            text = textBoxInfo.text,
            fontSize = fontSize.value.sp,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = (CORNER_SIZE / 2f).dp)
        )
    }
}

@Composable
fun TextBoxForEdit(
    activeBoxId: String,
    inActiveBoxId: String,
    parent: Rect,
    textBoxInfo: TextBoxInfo,
    updateTextBoxInfo: (TextBoxInfo) -> Unit,
    onClick: (id: String) -> Unit,
    onClickDelete: () -> Unit,
) {

    val state = remember(activeBoxId) {
        mutableStateOf(if(inActiveBoxId == textBoxInfo.id) BoxState.InActive else if (activeBoxId == textBoxInfo.id) BoxState.Active else BoxState.None)
    }
    val position = remember(parent) {
        mutableStateOf(
            Offset(
                textBoxInfo.boxData.offsetRatioX * parent.width,
                textBoxInfo.boxData.offsetRatioY * parent.height
            )
        )
    }
    val size = remember(parent) {
        mutableStateOf(
            Size(
                textBoxInfo.boxData.widthRatio * parent.width,
                textBoxInfo.boxData.heightRatio * parent.height
            )
        )
    }
    val fontSize = remember(parent, textBoxInfo) {
        mutableStateOf(
            textBoxInfo.fontSizeRatio * textBoxInfo.boxData.widthRatio * parent.width
        )
    }
    val text = remember {
        mutableStateOf(
            textBoxInfo.text
        )
    }

    if(state.value == BoxState.InActive) {
        state.value = BoxState.None
        updateTextBoxInfo(
            TextBoxInfo(
                id = textBoxInfo.id,
                text = text.value,
                fontSizeRatio = fontSize.value / size.value.width,
                boxData = BoxData(
                    offsetRatioX = position.value.x / parent.width,
                    offsetRatioY = position.value.y / parent.height,
                    widthRatio = size.value.width / parent.width,
                    heightRatio = size.value.height / parent.height,
                )
            )
        )
    }

    BoxForEdit(
        boxState = state.value,
        inputPosition = position.value,
        inputSize = size.value,
        resizeType = ResizeType.Free,
        updatePosition = { newPosition ->
            position.value = newPosition
        },
        updateSize = { newSize ->
            size.value = newSize
        },
        onClickDelete = { onClickDelete() },
        innerContent = {

            BasicTextField(
                value = text.value,
                onValueChange = { newText ->
                    text.value = newText
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = (CORNER_SIZE / 2f).dp)
                    .pointerInput(state.value) {
                        detectTapGestures {
                            if (state.value == BoxState.None) { onClick(textBoxInfo.id) }
                        }
                    },
                enabled = state.value == BoxState.Active,
                textStyle = TextStyle(
                    fontSize = fontSize.value.sp
                )
            )
        }
    )
}

@Preview
@Composable
private fun PreviewTextBox() {

    val textBoxInfoL = remember() {
        mutableStateOf(
            TextBoxInfo(
                id = "abc",
                text = "LEFT",
                fontSizeRatio = 0.05f,
                boxData = BoxData(
                    offsetRatioX = 0.2f,
                    offsetRatioY = 0.1f,
                    widthRatio = 0.5f,
                    heightRatio = 0.3f
                )
            )
        )
    }
    val textBoxInfoR = remember() {
        mutableStateOf(
            TextBoxInfo(
                id = "abc",
                text = "RIGHT",
                fontSizeRatio = 0.05f,
                boxData = BoxData(
                    offsetRatioX = 0.2f,
                    offsetRatioY = 0.1f,
                    widthRatio = 0.5f,
                    heightRatio = 0.3f
                )
            )
        )
    }

    var parentL by rememberSaveable(stateSaver = RectSaver) {
        mutableStateOf(
            Rect(Offset.Zero, Size.Zero)
        )
    }
    var parentR by rememberSaveable(stateSaver = RectSaver) {
        mutableStateOf(
            Rect(Offset.Zero, Size.Zero)
        )
    }

    IgTheme {
        Row(
            Modifier
                .width(600.dp)
                .height(400.dp)
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .aspectRatio(2f / 3f)
                    .onGloballyPositioned { layoutCoordinates ->
                        parentL = layoutCoordinates.boundsInRoot()
                        Log.d("TEST - left", "${layoutCoordinates.boundsInRoot()}")
                    }
                    .clickable {
                        textBoxInfoL.value = textBoxInfoL.value.copy(
                            boxData = textBoxInfoL.value.boxData.copy()
                        )
                    }
            ) {
//                TextBoxForEdit(
//                    parent = parentL,
//                    textBoxInfo = textBoxInfoL.value,
//                    updateTextBoxInfo = { newTextBoxInfo ->
//                        textBoxInfoL.value = newTextBoxInfo
//                    },
//                    onClickDelete = {}
//                )
            }
            Box(
                Modifier
                    .weight(1f)
                    .aspectRatio(2f / 3f)
                    .onGloballyPositioned { layoutCoordinates ->
                        parentR = layoutCoordinates.boundsInRoot()
                        Log.d("TEST - right", "${layoutCoordinates.boundsInRoot()}")
                    }
                    .clickable {
                        textBoxInfoR.value = textBoxInfoR.value.copy(
                            boxData = textBoxInfoR.value.boxData.copy()
                        )
                    }
            ) {
//                TextBoxForEdit(
//                    parent = parentL,
//                    textBoxInfo = textBoxInfoR.value,
//                    updateTextBoxInfo = { newTextBoxInfo ->
//                        textBoxInfoR.value = newTextBoxInfo
//                    },
//                    onClickDelete = {}
//                )
            }

        }
    }
}

val RectSaver = Saver<Rect, Bundle>(
    save = { rect ->
        Bundle().apply {
            putFloat("left", rect.left)
            putFloat("top", rect.top)
            putFloat("right", rect.right)
            putFloat("bottom", rect.bottom)
        }
    },
    restore = { bundle ->
        Rect(
            bundle.getFloat("left"),
            bundle.getFloat("top"),
            bundle.getFloat("right"),
            bundle.getFloat("bottom")
        )
    }
)
