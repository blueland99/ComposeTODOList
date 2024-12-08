package com.blueland.todo.navigation

sealed class Route(val route: String) {
    object Main : Route("Main")
    object Setting : Route("Setting")
    object GroupSetting : Route("GroupSetting")
}