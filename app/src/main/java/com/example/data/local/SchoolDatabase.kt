package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AcademicCalendarEvent
import com.example.data.model.AcademicReport
import com.example.data.model.AttendanceRecord
import com.example.data.model.ChatMessage
import com.example.data.model.ClassroomRoom
import com.example.data.model.DailyUniform
import com.example.data.model.DismissalAlert
import com.example.data.model.ExamSchedule
import com.example.data.model.ParentStudentAccount
import com.example.data.model.PickupQueue
import com.example.data.model.SavingTransaction
import com.example.data.model.SchoolActivity
import com.example.data.model.SchoolAnnouncement
import com.example.data.model.SchoolProfile
import com.example.data.model.Student
import com.example.data.model.TeacherAccount
import com.example.data.model.TimetableLesson

@Database(
  entities = [
    Student::class,
    DismissalAlert::class,
    PickupQueue::class,
    AcademicReport::class,
    SavingTransaction::class,
    AttendanceRecord::class,
    SchoolActivity::class,
    ChatMessage::class,
    ExamSchedule::class,
    AcademicCalendarEvent::class,
    SchoolAnnouncement::class,
    SchoolProfile::class,
    ClassroomRoom::class,
    TeacherAccount::class,
    ParentStudentAccount::class,
    TimetableLesson::class,
    DailyUniform::class
  ],
  version = 6,
  exportSchema = false
)
abstract class SchoolDatabase : RoomDatabase() {
  abstract fun schoolDao(): SchoolDao

  companion object {
    @Volatile
    private var INSTANCE: SchoolDatabase? = null

    fun getDatabase(context: Context): SchoolDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          SchoolDatabase::class.java,
          "sahabat_sekolah_db"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
