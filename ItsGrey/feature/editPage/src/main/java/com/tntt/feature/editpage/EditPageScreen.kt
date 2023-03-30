package com.tntt.feature.editpage

import android.graphics.Bitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tntt.designsystem.component.IgIconButton
import com.tntt.designsystem.component.IgTextButton
import com.tntt.designsystem.component.IgTopAppBar
import com.tntt.designsystem.icon.IgIcons
import com.tntt.model.ImageBoxInfo
import com.tntt.model.TextBoxInfo
import com.tntt.ui.PageForEdit


@Composable
internal fun EditPageRoute(
    viewModel: EditPageViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onImageClick: (String) -> Unit
) {
    val textBoxList by viewModel.textBoxList.collectAsStateWithLifecycle()
    val imageBox by viewModel.imageBox.collectAsStateWithLifecycle()
    val image by viewModel.image.collectAsStateWithLifecycle()
    val selectedBoxId by viewModel.selectedBoxId.collectAsStateWithLifecycle()

    EditPageScreen(
        textBoxList = textBoxList,
        imageBox = imageBox,
        image = image,
        selectedBoxId = selectedBoxId,
        onBackClick = onBackClick,
        onCreateTextBox = viewModel::createTextBox,
        onCreateImageBox = viewModel::createImageBox,
        updateTextBox = viewModel::updateTextBox,
        updateImageBox = viewModel::updateImageBox,
        onBoxSelected = viewModel::onBoxSelected,
        deleteBox = viewModel::deleteBox
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditPageScreen(
    textBoxList: List<TextBoxInfo>,
    imageBox: List<ImageBoxInfo>,
    image: ImageBitmap,
    selectedBoxId: String,
    onBackClick: () -> Unit,
    onCreateTextBox: () -> Unit,
    onCreateImageBox: () -> Unit,
    updateTextBox: (TextBoxInfo) -> Unit,
    updateImageBox: (ImageBoxInfo) -> Unit,
    onBoxSelected: (String) -> Unit,
    deleteBox: (String) -> Unit
) {

    Scaffold(
        topBar = {
            EditBookTopAppBar(
                onBackClick
            )
        },
    ) { paddingValues ->
        Column(
            Modifier.padding(paddingValues)
        ) {
            Row {
                CreateImageBoxButton(onCreateImageBox = onCreateImageBox)
                CreateTextBoxButton(onCreateTextBox = onCreateTextBox)
            }
            EditPageBox(
                textBoxList = textBoxList,
                imageBox = imageBox,
                image = image,
                selectedBoxId = selectedBoxId,
                updateTextBox = updateTextBox,
                updateImageBox = updateImageBox,
                onBoxSelected = onBoxSelected,
                deleteBox = deleteBox
            )
        }
    }
}

@Composable
fun EditPageBox(
    textBoxList: List<TextBoxInfo>,
    imageBox: List<ImageBoxInfo>,
    image: ImageBitmap,
    selectedBoxId: String,
    updateTextBox: (TextBoxInfo) -> Unit,
    updateImageBox: (ImageBoxInfo) -> Unit,
    onBoxSelected: (String) -> Unit,
    deleteBox: (String) -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .pointerInput(Unit) {
                detectTapGestures { onBoxSelected("") }
            },
        contentAlignment = Alignment.Center
    ) {
        PageForEdit(
            modifier = Modifier
                .padding(20.dp)
                .background(MaterialTheme.colorScheme.background),
            textBoxList = textBoxList,
            imageBox = imageBox,
            image = image,
            selectedBoxId = selectedBoxId,
            updateTextBox = updateTextBox,
            updateImageBox = updateImageBox,
            onBoxSelected = onBoxSelected,
            deleteBox = deleteBox,
        )
    }
}

@Composable
private fun CreateImageBoxButton(
    onCreateImageBox: () -> Unit
) {
    IgIconButton(
        onClick = { onCreateImageBox() },
        icon = {
            Icon(
                imageVector = IgIcons.AddImageBox,
                contentDescription = "AddImageBox"
            )
        },
    )
}

@Composable
private fun CreateTextBoxButton(
    onCreateTextBox: () -> Unit
) {
    IgIconButton(
        onClick = { onCreateTextBox() },
        icon = {
            Icon(
                imageVector = IgIcons.AddTextBox,
                contentDescription = "AddTextBox"
            )
        },
    )
}

@Composable
private fun OpenFontSizeDialogButton(fontSize: Int, openDialog: () -> Unit) {
    IgTextButton(
        onClick = { openDialog() },
        content = {
            Text(text = "$fontSize pt")
        }
    )
}

@Composable
private fun RegulateFontSizeDialog() {

    Dialog(onDismissRequest = { /*TODO*/ }) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookTopAppBar(
    onBackClick: () -> Unit
) {
    IgTopAppBar(
        title = "",
        navigationIcon = IgIcons.NavigateBefore,
        navigationIconContentDescription = "Back",
        onNavigationClick = { onBackClick() },
        actions = {
            IgIconButton(
                onClick = { /*TODO*/ },
                icon = {
                    Icon(
                        imageVector = IgIcons.Template,
                        contentDescription = "Preview",
                    )
                }
            )
            IgTextButton(
                onClick = { /*TODO*/ },
                text = { Text(text = "저장") }
            )
        }
    )
}
