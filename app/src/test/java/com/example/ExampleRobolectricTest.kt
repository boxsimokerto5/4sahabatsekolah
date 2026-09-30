package com.example

import android.app.NotificationManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.util.LocalNotificationService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SahabatSekolah", appName)
  }

  @Test
  fun `local notification service initializes channels`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val service = LocalNotificationService(context)
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val attendanceChannel = notificationManager.getNotificationChannel(LocalNotificationService.CHANNEL_ATTENDANCE_ID)
    assertNotNull(attendanceChannel)
    assertEquals("Presensi & Kehadiran Siswa", attendanceChannel.name)

    val announcementChannel = notificationManager.getNotificationChannel(LocalNotificationService.CHANNEL_ANNOUNCEMENT_ID)
    assertNotNull(announcementChannel)
    assertEquals("Pengumuman & Surat Edaran", announcementChannel.name)
  }

  @Test
  fun `send attendance and announcement notifications without error`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val service = LocalNotificationService(context)

    service.notifyAttendanceMarked(
      studentName = "Rafa Al-Ghifari",
      status = "HADIR",
      time = "06:55 WIB",
      note = "Suhu 36.4 C"
    )

    service.notifyAnnouncementPosted(
      title = "Pemberitahuan Ujian Tengah Semester",
      category = "EDARAN",
      author = "Kepala Sekolah",
      letterNumber = "048/SD-SS/2026"
    )
  }
}

