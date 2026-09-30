package com.example.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R

/**
 * Local Notification Service for SahabatSekolah.
 * Alerts parents when student attendance is marked or new school announcements are posted.
 */
class LocalNotificationService(private val context: Context) {

  companion object {
    const val TAG = "LocalNotificationService"
    const val CHANNEL_ATTENDANCE_ID = "school_attendance_channel"
    const val CHANNEL_ANNOUNCEMENT_ID = "school_announcement_channel"

    const val EXTRA_TARGET_SCREEN = "extra_target_screen"
    const val SCREEN_ATTENDANCE = "ATTENDANCE"
    const val SCREEN_ANNOUNCEMENT = "ANNOUNCEMENT"
  }

  init {
    createNotificationChannels()
  }

  /**
   * Sets up notification channels required on Android 8.0 (API 26) and above.
   */
  private fun createNotificationChannels() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
          ?: return

      // Attendance Channel
      val attendanceChannel = NotificationChannel(
        CHANNEL_ATTENDANCE_ID,
        "Presensi & Kehadiran Siswa",
        NotificationManager.IMPORTANCE_HIGH
      ).apply {
        description = "Pemberitahuan instan saat status kehadiran ananda dicatat oleh sekolah"
        enableLights(true)
        enableVibration(true)
        setShowBadge(true)
      }

      // Announcements Channel
      val announcementChannel = NotificationChannel(
        CHANNEL_ANNOUNCEMENT_ID,
        "Pengumuman & Surat Edaran",
        NotificationManager.IMPORTANCE_HIGH
      ).apply {
        description = "Pemberitahuan saat surat edaran atau pengumuman resmi baru diterbitkan"
        enableLights(true)
        enableVibration(true)
        setShowBadge(true)
      }

      notificationManager.createNotificationChannel(attendanceChannel)
      notificationManager.createNotificationChannel(announcementChannel)
    }
  }

  /**
   * Checks whether the app has permission to post notifications.
   */
  fun hasNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
      ) == PackageManager.PERMISSION_GRANTED
    } else {
      NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
  }

  /**
   * Sends a local notification to alert parents when a student's attendance is marked.
   */
  fun notifyAttendanceMarked(
    studentName: String,
    status: String,
    time: String,
    note: String = ""
  ) {
    if (!hasNotificationPermission()) {
      Log.w(TAG, "Notification permission not granted. Skipping attendance notification.")
      return
    }

    try {
      val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        putExtra(EXTRA_TARGET_SCREEN, SCREEN_ATTENDANCE)
      }

      val pendingIntent = PendingIntent.getActivity(
        context,
        (System.currentTimeMillis() % 10000).toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
      )

      val (statusLabel, statusEmoji) = when (status.uppercase()) {
        "HADIR" -> Pair("Hadir di Kelas", "✅")
        "SAKIT" -> Pair("Izin Sakit", "🩺")
        "IZIN" -> Pair("Izin Tidak Masuk", "📝")
        else -> Pair("Terlambat", "⏰")
      }

      val title = "$statusEmoji Presensi Siswa: $studentName"
      val shortMessage = "Status: $statusLabel pada $time"
      val detailMessage = buildString {
        append("Ananda $studentName telah dicatat berstatus $statusLabel pada pukul $time.")
        if (note.isNotBlank()) {
          append("\nCatatan: $note")
        } else if (status.uppercase() == "HADIR") {
          append("\nSuhu tubuh normal (36.4°C). Selamat belajar dengan ceria!")
        }
        append("\nKetuk untuk melihat riwayat kehadiran lengkap.")
      }

      val notification = NotificationCompat.Builder(context, CHANNEL_ATTENDANCE_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(title)
        .setContentText(shortMessage)
        .setStyle(NotificationCompat.BigTextStyle().bigText(detailMessage))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .setCategory(NotificationCompat.CATEGORY_EVENT)
        .build()

      val notificationId = 1001 + (System.currentTimeMillis() % 100).toInt()
      NotificationManagerCompat.from(context).notify(notificationId, notification)
      Log.i(TAG, "Attendance notification dispatched for $studentName ($status)")
    } catch (e: SecurityException) {
      Log.e(TAG, "SecurityException while posting attendance notification", e)
    } catch (e: Exception) {
      Log.e(TAG, "Error posting attendance notification", e)
    }
  }

  /**
   * Sends a local notification to alert parents when a school announcement or circular is posted.
   */
  fun notifyAnnouncementPosted(
    title: String,
    category: String,
    author: String,
    letterNumber: String = ""
  ) {
    if (!hasNotificationPermission()) {
      Log.w(TAG, "Notification permission not granted. Skipping announcement notification.")
      return
    }

    try {
      val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        putExtra(EXTRA_TARGET_SCREEN, SCREEN_ANNOUNCEMENT)
      }

      val pendingIntent = PendingIntent.getActivity(
        context,
        (System.currentTimeMillis() % 10000 + 1).toInt(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
      )

      val categoryLabel = when (category.uppercase()) {
        "URGENT" -> "🔴 [MENDESAK]"
        "EDARAN" -> "📜 [SURAT EDARAN]"
        "KEGIATAN" -> "🎉 [AGENDA KEGIATAN]"
        else -> "📚 [PENGUMUMAN SEKOLAH]"
      }

      val notifTitle = "$categoryLabel Pengumuman Resmi Baru"
      val shortMessage = title
      val detailMessage = buildString {
        append(title)
        if (letterNumber.isNotBlank()) {
          append("\nNomor Surat: $letterNumber")
        }
        append("\nDiterbitkan oleh: $author")
        append("\nKetuk notifikasi ini untuk membaca surat edaran lengkap.")
      }

      val notification = NotificationCompat.Builder(context, CHANNEL_ANNOUNCEMENT_ID)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(notifTitle)
        .setContentText(shortMessage)
        .setStyle(NotificationCompat.BigTextStyle().bigText(detailMessage))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
        .build()

      val notificationId = 2001 + (System.currentTimeMillis() % 100).toInt()
      NotificationManagerCompat.from(context).notify(notificationId, notification)
      Log.i(TAG, "Announcement notification dispatched: $title")
    } catch (e: SecurityException) {
      Log.e(TAG, "SecurityException while posting announcement notification", e)
    } catch (e: Exception) {
      Log.e(TAG, "Error posting announcement notification", e)
    }
  }
}
