package com.vionavio.githubuser

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.vionavio.githubuser.view.MainActivity


class GithubUserWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val views = RemoteViews(context.packageName, R.layout.github_user_widget)
            views.setOnClickPendingIntent(R.id.appwidget_image, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

}
