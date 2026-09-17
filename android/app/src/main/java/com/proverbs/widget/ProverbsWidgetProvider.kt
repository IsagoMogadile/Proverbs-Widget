package com.proverbs.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews

class ProverbsWidgetProvider : AppWidgetProvider() {

    companion object {
        const val ACTION_REFRESH = "com.proverbs.widget.ACTION_REFRESH"

        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            options: Bundle? = null,
        ) {
            val verse = VerseRepository.randomVerse(context)
            val views = RemoteViews(context.packageName, R.layout.proverbs_widget)
            views.setTextViewText(R.id.widget_verse_text, "“${verse.text}”")
            views.setTextViewText(R.id.widget_verse_ref, "Proverbs ${verse.chapter}:${verse.verse}")

            applySizing(views, options ?: appWidgetManager.getAppWidgetOptions(appWidgetId))

            val refreshIntent = Intent(context, ProverbsWidgetProvider::class.java).apply {
                action = ACTION_REFRESH
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            val pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, refreshIntent, flags)
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun applySizing(views: RemoteViews, options: Bundle?) {
            val minWidth = options?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 250) ?: 250
            val minHeight = options?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, 120) ?: 120

            when {
                minHeight < 100 || minWidth < 160 -> {
                    // Compact: drop the title/refresh row, keep the quote tight.
                    views.setViewVisibility(R.id.widget_title, View.GONE)
                    views.setViewVisibility(R.id.widget_refresh, View.GONE)
                    views.setTextViewTextSize(R.id.widget_verse_text, TypedValue.COMPLEX_UNIT_SP, 12f)
                    views.setTextViewTextSize(R.id.widget_verse_ref, TypedValue.COMPLEX_UNIT_SP, 10f)
                    views.setInt(R.id.widget_verse_text, "setMaxLines", 3)
                }
                minHeight > 220 || minWidth > 320 -> {
                    // Large: roomier type, more of the verse visible.
                    views.setViewVisibility(R.id.widget_title, View.VISIBLE)
                    views.setViewVisibility(R.id.widget_refresh, View.VISIBLE)
                    views.setTextViewTextSize(R.id.widget_verse_text, TypedValue.COMPLEX_UNIT_SP, 19f)
                    views.setTextViewTextSize(R.id.widget_verse_ref, TypedValue.COMPLEX_UNIT_SP, 14f)
                    views.setInt(R.id.widget_verse_text, "setMaxLines", 12)
                }
                else -> {
                    // Regular.
                    views.setViewVisibility(R.id.widget_title, View.VISIBLE)
                    views.setViewVisibility(R.id.widget_refresh, View.VISIBLE)
                    views.setTextViewTextSize(R.id.widget_verse_text, TypedValue.COMPLEX_UNIT_SP, 15f)
                    views.setTextViewTextSize(R.id.widget_verse_ref, TypedValue.COMPLEX_UNIT_SP, 12f)
                    views.setInt(R.id.widget_verse_text, "setMaxLines", 6)
                }
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle,
    ) {
        // Fires when the user resizes the widget; re-render at the new size
        // without picking a new verse.
        val views = RemoteViews(context.packageName, R.layout.proverbs_widget)
        applySizing(views, newOptions)
        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_REFRESH) {
            val appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                updateWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }
}
