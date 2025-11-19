package com.example.proyectologin005d.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectologin005d.login.LoginScreen
import com.example.proyectologin005d.ui.boleta.BoletaScreen
import com.example.proyectologin005d.ui.login.RegisterScreen
import com.example.proyectologin005d.ui.pages.*
import com.example.proyectologin005d.viewmodel.CartViewModel

sealed class Screen(val route: String, val label: String, val icon: @Composable () -> Unit) {
    object Index     : Screen("index",       "Inicio",   { Icon(Icons.Default.Home,         null) })
    object Nosotros  : Screen("nosotros",    "Nosotros", { Icon(Icons.Default.Info,         null) })
    object News      : Screen("news",        "Noticias", { Icon(Icons.Default.Article,      null) })
    object Contacto  : Screen("contactanos", "Contacto", { Icon(Icons.Default.Mail,         null) })
    object Productos : Screen("productos",   "Productos",{ Icon(Icons.Default.Cake,         null) })
    object Carrito   : Screen("carrito",     "Carrito",  { Icon(Icons.Default.ShoppingCart, null) })
    object Historial : Screen("historial",   "Historial",{ Icon(Icons.Default.History,      null) })
    object Perfil    : Screen("perfil",      "Perfil",   { Icon(Icons.Default.Person,       null) })
}

@Composable
fun AppNav(navController: NavHostController = rememberNavController()) {
    val cartViewModel: CartViewModel = viewModel()

    val screens = listOf(
        Screen.Index, Screen.Nosotros, Screen.News, Screen.Contacto, Screen.Productos, Screen.Carrito, Screen.Historial, Screen.Perfil
    )
    val screenRoutes = screens.map { it.route }.toSet()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in screenRoutes

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                BottomAppBar {
                    val currentDestination = backStackEntry?.destination
                    screens.forEach { screen ->
                        NavigationBarItem(
                            icon = screen.icon,
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding),
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
        ) {
            // Auth
            composable(
                "login",
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() }
            ) { LoginScreen(navController) }
            composable("register") { RegisterScreen(navController) }

            // Contenido con BottomBar
            composable("index")       { IndexScreen(navController) }
            composable("nosotros")    { Nosotros() }
            composable("news")        { NewsScreen(navController) }
            composable("contactanos") { ContactoScreen() }
            composable("productos")   { ProductosScreen(cartViewModel, navController) }
            composable("carrito")     { CarritoScreen(cartViewModel, navController) }
            composable("historial")   { HistorialScreen(navController) }
            composable("perfil")      { PerfilScreen(navController) }
            composable("payment")     { PaymentScreen(navController, cartViewModel) }

            composable(
                "productDetail/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId")
                if (productId != null) {
                    ProductDetailScreen(navController = navController, productId = productId)
                }
            }

            composable("boleta") {
                BoletaScreen(navController = navController, cartViewModel = cartViewModel)
            }
        }
    }
}
