package com.blueland.todo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.blueland.todo.R
import com.blueland.todo.data.local.TodoEntity
import com.blueland.todo.ui.theme.LocalColors
import com.blueland.todo.ui.theme.LocalTextStyles
import com.blueland.todo.ui.theme.ui.theme.LocalShapes
import com.blueland.todo.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val todos by viewModel.allTodos.collectAsState(initial = emptyList())

    Scaffold(
        containerColor = LocalColors.current.background,
//        topBar = {
//            TopAppBar(
//                title = { Text("Todo List") },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    titleContentColor = MaterialTheme.colorScheme.onPrimary
//                )
//            )
//        },
        floatingActionButton = {
            // 추가 버튼
            FloatingActionButton(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                containerColor = LocalColors.current.main,
                onClick = { viewModel.addTodo("할 일") }
            ) {
                Image(
                    painterResource(R.drawable.ic_add),
                    modifier = Modifier.padding(10.dp),
                    colorFilter = ColorFilter.tint(LocalColors.current.button1),
                    contentDescription = null
                )
            }
        },
        content = { paddingValues ->

            // 순환할 색상 리스트
            val colors = listOf(
                LocalColors.current.point1,
                LocalColors.current.point2,
                LocalColors.current.point3,
                LocalColors.current.point4
            )

            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(top = 32.dp, bottom = 90.dp, start = 16.dp, end = 16.dp)
            ) {
                itemsIndexed(todos) { index, item ->
                    val color = colors[index % colors.size]  // 색상을 순환하도록 설정
                    TodoItem(item = item, color = color)
                }
            }
        }
    )
}


@Composable
fun TodoItem(
    viewModel: MainViewModel = hiltViewModel(),
    item: TodoEntity,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = LocalShapes.large,
        elevation = CardDefaults.elevatedCardElevation(2.dp),

        colors = CardDefaults.cardColors(containerColor = LocalColors.current.background),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(if (item.isCompleted) R.drawable.ic_check_on else R.drawable.ic_check_off),
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            viewModel.updateTodo(item.copy(isCompleted = !item.isCompleted))
                        }
                    ),
                colorFilter = ColorFilter.tint(if (item.isCompleted) color else LocalColors.current.disable1),
                contentDescription = null
            )

            Text(
                item.title,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f),
                style = LocalTextStyles.current.mediumBodySm
            )

            // 삭제 버튼
            Image(
                painterResource(R.drawable.ic_delete),
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(16.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            viewModel.deleteTodo(item)
                        }
                    ),
                colorFilter = ColorFilter.tint(LocalColors.current.text1),
                contentDescription = null
            )
        }
    }
}