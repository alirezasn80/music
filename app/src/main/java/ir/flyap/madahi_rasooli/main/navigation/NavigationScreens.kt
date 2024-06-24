package ir.flyap.madahi_rasooli.main.navigation

import ir.flyap.madahi_rasooli.utill.Arg
import ir.flyap.madahi_rasooli.utill.argumentCount
import ir.flyap.madahi_rasooli.utill.arguments

sealed class Screen(val route: String) {

    fun route(vararg args: Any?): String {
        var safeRoute = route

        require(args.size == safeRoute.argumentCount) {
            "Provided ${args.count()} parameters, was expected ${safeRoute.argumentCount} parameters!"
        }

        safeRoute.arguments().forEachIndexed { index, matchResult ->
            safeRoute = safeRoute.replace(matchResult.value, args[index].toString())
        }

        return safeRoute
    }

    data object Splash : Screen("Splash")

    data object Offline : Screen("Offline")

    data object OnBoarding : Screen("OnBoarding")

    data object AboutSinger : Screen("AboutSinger")

    data object AboutUs : Screen("AboutUs")

    data object AboutFan : Screen("AboutFan/{${Arg.FAN_ID}}")

    data object Payment : Screen("Payment/{${Arg.Key}}")

    data object Stories : Screen("Stories/{${Arg.CATEGORY_ID}}/{${Arg.TITLE}}")

    data object Content : Screen("Content/{${Arg.CATEGORY_ID}}/{${Arg.CONTENT_ID}}")

    data object Home : Screen("Home")

    data object Detail : Screen("Detail")
}
