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

  @Test
  fun `database stores and retrieves timetable and daily uniform`() = kotlinx.coroutines.runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = com.example.data.local.SchoolDatabase.getDatabase(context)
    val dao = db.schoolDao()

    val uniform = com.example.data.model.DailyUniform(
      dayOfWeek = "Senin",
      uniformTitle = "Seragam Merah Putih Nasional",
      description = "Kemeja putih dan celana merah",
      accessories = "Topi upacara dan dasi",
      shoesColor = "Sepatu Hitam"
    )
    val uniformId = dao.insertDailyUniform(uniform)
    assertNotNull(uniformId)

    val lesson = com.example.data.model.TimetableLesson(
      dayOfWeek = "Senin",
      periodNumber = 1,
      timeRange = "07.00 - 07.45 WIB",
      subject = "Upacara Bendera",
      teacherName = "Wali Kelas",
      requiredItems = "Topi dan Dasi"
    )
    val lessonId = dao.insertTimetableLesson(lesson)
    assertNotNull(lessonId)
  }
}

