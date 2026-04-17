package com.calmvest.android.navigation

object Routes {
    const val SPLASH = "splash"
    const val SIGN_IN = "sign_in"
    const val SIGN_UP = "sign_up"
    const val ONBOARDING = "onboarding"
    const val GOAL_SETUP = "goal_setup"
    const val INVESTMENT_MODE = "investment_mode"
    const val DASHBOARD = "dashboard"
    const val GOALS = "goals"
    const val GOAL_DETAIL = "goal_detail/{goalId}"
    const val TRANSACTIONS = "transactions"
    const val PORTFOLIO = "portfolio"
    const val SETTINGS = "settings"
    const val ROUND_UP_RULE = "round_up_rule"

    fun goalDetail(goalId: String) = "goal_detail/$goalId"
}
