package com.example.petcare.ui.navigation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
object Routes {
    const val PETS = "pets"
    const val ADD_PET = "add_pet"
    const val PET_DETAIL = "pet/{petId}"
    fun petDetail(petId: String) = "pet/$petId"
}
@Composable
fun PetCareNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.PETS) {
        composable(Routes.PETS) { Text("Pets list - Yasser") }
        composable(Routes.ADD_PET) { Text("Add pet - Yasser") }
        composable(
            route = Routes.PET_DETAIL,
            arguments = listOf(navArgument("petId") { type = NavType.StringType })
        ) {
            Text("Pet detail - Tony")
        }
    }
    }