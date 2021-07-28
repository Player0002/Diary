package com.danny.diary

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.danny.diary.data.DiaryItem
import com.danny.diary.databinding.ActivityDiaryViewerBinding
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.core.MarkwonTheme
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DiaryViewerActivity : AppCompatActivity() {

    companion object {
        const val DIARY_ITEM = "DIARY_ITEM"
    }

    private val binding by lazy { ActivityDiaryViewerBinding.inflate(layoutInflater) }

    lateinit var diaryItem: DiaryItem

    val typed = TypedValue()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        theme.resolveAttribute(R.attr.colorPrimary, typed, true)

        diaryItem = intent?.extras?.get(DIARY_ITEM) as? DiaryItem
            ?: throw IllegalArgumentException("Diary Viewer without DiaryItem")

        val calendar = Instant.ofEpochMilli(diaryItem.time).atZone(ZoneId.systemDefault())

        val timeHeader = calendar.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
        val timeSub = calendar.format(DateTimeFormatter.ofPattern("a hh시 mm분 ss초"))

        val content =
            diaryItem.content.replace("\${today}", "## $timeHeader")
                .replace("\${time}", "### $timeSub")

        val mark = Markwon.builder(this).usePlugin(object : AbstractMarkwonPlugin() {
            override fun configureTheme(builder: MarkwonTheme.Builder) {
                builder.headingBreakColor(typed.data)
            }
        })
            .usePlugin(SoftBreakAddsNewLinePlugin.create())
            .build()

        mark.setMarkdown(binding.viewer, content)

        binding.materialToolbar.setOnMenuItemClickListener {
            getBitmapFromView(binding.viewer)?.let {
                val uri = saveToGallery(this, it, "Diary")
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/png"
                intent.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(intent, "Share"))
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
            }
            false
        }

        setContentView(binding.root)
    }


    private fun getBitmapFromView(view: View, defaultColor: Int = typed.data): Bitmap? {
        val fixedHeight = if (view.height < view.width) view.width else view.height
        val bitmap =
            Bitmap.createBitmap(view.width, fixedHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(defaultColor)
        view.draw(canvas)
        return bitmap
    }

    private fun saveToGallery(context: Context, bitmap: Bitmap, albumName: String): Uri {
        val dateInfo =
            Instant.ofEpochMilli(diaryItem.time).atZone(ZoneId.systemDefault()).format(
                DateTimeFormatter.ofPattern("yyyy년 MM월 dd일의 일기")
            )
        val filename = "$dateInfo.png"
        val write: (OutputStream) -> Boolean = {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "${Environment.DIRECTORY_DCIM}/$albumName"
                )
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return Uri.EMPTY
            context.contentResolver.openOutputStream(uri)?.let(write)
            return uri

        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString() + File.separator + albumName
            val file = File(imagesDir)
            if (!file.exists()) {
                file.mkdir()
            }
            val image = File(imagesDir, filename)
            write(FileOutputStream(image))
            return Uri.parse(imagesDir)
        }
    }
}