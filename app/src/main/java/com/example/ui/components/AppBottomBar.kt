package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AvaBrass
import com.example.ui.theme.AvaTeal

enum class AppTab(val label: String, val icon: ImageVector, val tag: String) {
    HOME("Home", Icons.Default.Home, "tab_home"),
    BOOK("Cash Book", Icons.Default.AccountBalanceWallet, "tab_book"),
    CHEQUES("Cheques", Icons.Default.ReceiptLong, "tab_cheques"),
    FLEET("Fleet", Icons.Default.DirectionsBus, "tab_fleet"),
    REPORTS("Reports", Icons.Default.BarChart, "tab_reports"),
    SETTINGS("Settings", Icons.Default.Settings, "tab_settings")
}

@Composable
fun AppBottomBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .navigationBarsPadding()
            .testTag("app_bottom_bar")
    ) {
        AppTab.values().forEach { tab ->
            NavigationBarItem(
                selected = currentTab == tab,
                onClick = { onTabSelected(tab) },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label, fontSize = 10.sp, maxLines = 1) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = AvaBrass.copy(alpha = 0.25f),
                    selectedIconColor = AvaTeal,
                    selectedTextColor = AvaTeal
                ),
                modifier = Modifier.testTag(tab.tag)
            )
        }
    }
}
