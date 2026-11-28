package com.example.petcare.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.petcare.ui.pets.AddPetRoute
import com.example.petcare.ui.pets.PetsRoute
import com.example.petcare.ui.petdetail.PetDetailRoute
import com.example.petcare.ui.settings.SettingsRoute


object Routes {

    const val PETS = "pets"

    const val ADD_PET = "add_pet"

    const val PET_DETAIL = "pet/{petId}"

    const val EDIT_PET = "pet/{petId}/edit"

    const val SETTINGS = "settings"


    fun petDetail(petId: String) =
        "pet/$petId"


    fun editPet(petId: String) =
        "pet/$petId/edit"
}


@Composable
fun PetCareNavHost(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Routes.PETS
    ) {


        // Pets list

        composable(Routes.PETS) {

            PetsRoute(

                onAddPet = {
                    navController.navigate(Routes.ADD_PET)
                },

                onOpenPet = { petId ->

                    navController.navigate(
                        Routes.petDetail(petId)
                    )
                },

                onOpenSettings = {

                    navController.navigate(
                        Routes.SETTINGS
                    )
                }
            )
        }


        // Add pet

        composable(Routes.ADD_PET) {

            AddPetRoute(

                onDone = {
                    navController.popBackStack()
                }
            )
        }


        // Pet details

        composable(

            route = Routes.PET_DETAIL,

            arguments = listOf(

                navArgument("petId") {
                    type = NavType.StringType
                }
            ),

            deepLinks = listOf(

                navDeepLink {
                    uriPattern = "petcare://pet/{petId}"
                }
            )

        ) {

            PetDetailRoute(

                onBack = {
                    navController.popBackStack()
                },

                onEdit = { petId ->

                    navController.navigate(
                        Routes.editPet(petId)
                    )
                }
            )
        }


        // Edit pet

        composable(

            route = Routes.EDIT_PET,

            arguments = listOf(

                navArgument("petId") {
                    type = NavType.StringType
                }
            )

        ) {

            AddPetRoute(

                onDone = {
                    navController.popBackStack()
                }
            )
        }


        // Settings

        composable(Routes.SETTINGS) {

            SettingsRoute(

                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}