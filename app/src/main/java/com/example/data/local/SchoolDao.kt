package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {
  // Students
  @Query("SELECT * FROM students LIMIT 1")
  fun getMainStudent(): Flow<Student?>

  @Query("SELECT * FROM students")
  fun getAllStudents(): Flow<List<Student>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStudent(student: Student): Long

  // Dismissal Bell & Alerts
  @Query("SELECT * FROM dismissal_alerts ORDER BY id DESC LIMIT 1")
  fun getLatestDismissalAlert(): Flow<DismissalAlert?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDismissalAlert(alert: DismissalAlert): Long

  // Pickup Queues
  @Query("SELECT * FROM pickup_queues ORDER BY timestamp DESC")
  fun getPickupQueues(): Flow<List<PickupQueue>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPickupQueue(queue: PickupQueue): Long

  @Query("UPDATE pickup_queues SET status = :status WHERE id = :id")
  suspend fun updatePickupStatus(id: Long, status: String)

  // Academic Reports
  @Query("SELECT * FROM academic_reports ORDER BY id DESC")
  fun getAllAcademicReports(): Flow<List<AcademicReport>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAcademicReport(report: AcademicReport): Long

  // Savings
  @Query("SELECT * FROM saving_transactions ORDER BY id DESC")
  fun getAllSavingTransactions(): Flow<List<SavingTransaction>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSavingTransaction(transaction: SavingTransaction): Long

  // Attendance
  @Query("SELECT * FROM attendance_records ORDER BY id DESC")
  fun getAllAttendanceRecords(): Flow<List<AttendanceRecord>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAttendanceRecord(record: AttendanceRecord): Long

  // School Activities / Gallery
  @Query("SELECT * FROM school_activities ORDER BY id DESC")
  fun getAllSchoolActivities(): Flow<List<SchoolActivity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSchoolActivity(activity: SchoolActivity): Long

  @Query("UPDATE school_activities SET likes = likes + 1 WHERE id = :id")
  suspend fun likeActivity(id: Long)

  // Chat
  @Query("SELECT * FROM chat_messages ORDER BY id ASC")
  fun getAllChatMessages(): Flow<List<ChatMessage>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChatMessage(message: ChatMessage): Long

  // Exams
  @Query("SELECT * FROM exam_schedules ORDER BY id ASC")
  fun getAllExamSchedules(): Flow<List<ExamSchedule>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertExamSchedule(exam: ExamSchedule): Long

  // Academic Calendar & Activity Plan
  @Query("SELECT * FROM academic_calendar_events ORDER BY date ASC, timeRange ASC")
  fun getAllAcademicCalendarEvents(): Flow<List<AcademicCalendarEvent>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAcademicCalendarEvent(event: AcademicCalendarEvent): Long

  @Query("UPDATE academic_calendar_events SET rsvpStatus = :rsvpStatus WHERE id = :id")
  suspend fun updateEventRsvp(id: Long, rsvpStatus: String)

  @Query("UPDATE academic_calendar_events SET checkedItems = :checkedItems WHERE id = :id")
  suspend fun updateEventCheckedItems(id: Long, checkedItems: String)

  @Query("DELETE FROM academic_calendar_events WHERE id = :id")
  suspend fun deleteAcademicCalendarEvent(id: Long)

  // School Announcements / Mading Digital
  @Query("SELECT * FROM school_announcements ORDER BY isPinned DESC, id DESC")
  fun getAllAnnouncements(): Flow<List<SchoolAnnouncement>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAnnouncement(announcement: SchoolAnnouncement): Long

  @Query("UPDATE school_announcements SET isReadByParent = :isRead, readCount = CASE WHEN :isRead = 1 THEN readCount + 1 ELSE readCount - 1 END WHERE id = :id")
  suspend fun markAnnouncementAsRead(id: Long, isRead: Boolean)

  @Query("DELETE FROM school_announcements WHERE id = :id")
  suspend fun deleteAnnouncement(id: Long)

  // School Profile & Admin
  @Query("SELECT * FROM school_profiles LIMIT 1")
  fun getSchoolProfile(): Flow<SchoolProfile?>

  @Query("SELECT * FROM school_profiles ORDER BY id DESC")
  fun getAllSchoolProfiles(): Flow<List<SchoolProfile>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateSchoolProfile(profile: SchoolProfile): Long

  @Query("UPDATE school_profiles SET status = :status, isVerified = :isVerified, rejectionReason = :reason WHERE id = :id")
  suspend fun updateSchoolStatus(id: Long, status: String, isVerified: Boolean, reason: String)

  @Query("DELETE FROM school_profiles WHERE id = :id")
  suspend fun deleteSchoolProfile(id: Long)

  // Classroom Rooms
  @Query("SELECT * FROM classroom_rooms ORDER BY name ASC")
  fun getAllClassrooms(): Flow<List<ClassroomRoom>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertClassroom(classroom: ClassroomRoom): Long

  @Query("DELETE FROM classroom_rooms WHERE id = :id")
  suspend fun deleteClassroom(id: Long)

  // Teacher Accounts
  @Query("SELECT * FROM teacher_accounts ORDER BY fullName ASC")
  fun getAllTeachers(): Flow<List<TeacherAccount>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTeacher(teacher: TeacherAccount): Long

  @Query("UPDATE teacher_accounts SET assignedClass = :assignedClass WHERE id = :id")
  suspend fun updateTeacherAssignedClass(id: Long, assignedClass: String)

  @Query("DELETE FROM teacher_accounts WHERE id = :id")
  suspend fun deleteTeacher(id: Long)

  // Parent & Student Accounts
  @Query("SELECT * FROM parent_student_accounts ORDER BY studentName ASC")
  fun getAllParentStudentAccounts(): Flow<List<ParentStudentAccount>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertParentStudentAccount(account: ParentStudentAccount): Long

  @Query("DELETE FROM parent_student_accounts WHERE id = :id")
  suspend fun deleteParentStudentAccount(id: Long)

  // Timetable Lessons (Jadwal Pelajaran)
  @Query("SELECT * FROM timetable_lessons ORDER BY periodNumber ASC")
  fun getAllTimetableLessons(): Flow<List<TimetableLesson>>

  @Query("SELECT * FROM timetable_lessons WHERE dayOfWeek = :day ORDER BY periodNumber ASC")
  fun getTimetableLessonsByDay(day: String): Flow<List<TimetableLesson>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTimetableLesson(lesson: TimetableLesson): Long

  @Query("UPDATE timetable_lessons SET isCompletedByParent = :isCompleted WHERE id = :id")
  suspend fun updateLessonPackStatus(id: Long, isCompleted: Boolean)

  @Query("DELETE FROM timetable_lessons WHERE id = :id")
  suspend fun deleteTimetableLesson(id: Long)

  // Daily Uniforms (Kode Seragam Harian)
  @Query("SELECT * FROM daily_uniforms ORDER BY id ASC")
  fun getAllDailyUniforms(): Flow<List<DailyUniform>>

  @Query("SELECT * FROM daily_uniforms WHERE dayOfWeek = :day LIMIT 1")
  fun getDailyUniformByDay(day: String): Flow<DailyUniform?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDailyUniform(uniform: DailyUniform): Long

  @Query("UPDATE daily_uniforms SET uniformTitle = :title, description = :description, accessories = :accessories, shoesColor = :shoes WHERE id = :id")
  suspend fun updateDailyUniform(id: Long, title: String, description: String, accessories: String, shoes: String)
}
