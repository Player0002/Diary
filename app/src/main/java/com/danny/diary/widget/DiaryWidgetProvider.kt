package com.danny.diary.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.util.Log
import android.util.TypedValue
import android.widget.RemoteViews
import com.danny.diary.R
import com.danny.diary.data.DiaryDao
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.core.MarkwonTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class DiaryWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var dao: DiaryDao

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private val typed = TypedValue()

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        scope.launch {
            dao.getAllDiary().collect {
                if (it.isNotEmpty()) {
                    val view = RemoteViews(context.packageName, R.layout.layout_diary_widget)
                    val diaryItem = it.first()

                    val calendar =
                        Instant.ofEpochMilli(diaryItem.time).atZone(ZoneId.systemDefault())

                    val timeHeader = calendar.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
                    val timeSub = calendar.format(DateTimeFormatter.ofPattern("a hh시 mm분 ss초"))

                    val content =
                        diaryItem.content.replace("\${today}", "## $timeHeader")
                            .replace("\${time}", "### $timeSub")

                    val mark = Markwon.builder(context).usePlugin(object : AbstractMarkwonPlugin() {
                        override fun configureTheme(builder: MarkwonTheme.Builder) {
                            builder.headingBreakColor(typed.data)
                        }
                    })
                        .usePlugin(SoftBreakAddsNewLinePlugin.create())
                        .build()

                    val span = mark.toMarkdown(content)
                    view.setTextViewText(R.id.viewer, span)
                    Log.d("LOG", content)
                    AppWidgetManager.getInstance(context).updateAppWidget(
                        ComponentName(context, DiaryWidgetProvider::class.java), view
                    )
                }
            }
        }
    }

    override fun onDisabled(context: Context?) {
        job.cancel()
        super.onDisabled(context)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { id ->
            val view = RemoteViews(context.packageName, R.layout.layout_diary_widget)
            view.setTextViewText(R.id.viewer, "Test")
            appWidgetManager.updateAppWidget(id, view)
        }
    }
}