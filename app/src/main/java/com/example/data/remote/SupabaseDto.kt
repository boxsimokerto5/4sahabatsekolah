package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SupabaseStudent(
  @Json(name = "id") val id: Long? = null,
  @Json(name = "name") val name: String,
  @Json(name = "grade_class") val gradeClass: String,
  @Json(name = "student_number") val studentNumber: String,
  @Json(name = "parent_name") val parentName: String,
  @Json(name = "avatar_color_hex") val avatarColorHex: String,
  @Json(name = "target_savings") val targetSavings: Long = 250000L
)

@JsonClass(generateAdapter = true)
data class SupabaseDismissalAlert(
  @Json(name = "id") val id: Long? = null,
  @Json(name = "grade_class") val gradeClass: String,
  @Json(name = "is_dismissed") val isDismissed: Boolean,
  @Json(name = "dismissal_time") val dismissalTime: String,
  @Json(name = "title") val title: String,
  @Json(name = "message") val message: String,
  @Json(name = "announced_by") val announcedBy: String,
  @Json(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)

@JsonClass(generateAdapter = true)
data class SupabasePickupQueue(
  @Json(name = "id") val id: Long? = null,
  @Json(name = "student_name") val studentName: String,
  @Json(name = "parent_name") val parentName: String,
  @Json(name = "gate_location") val gateLocation: String,
  @Json(name = "status") val status: String,
  @Json(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)

@JsonClass(generateAdapter = true)
data class SupabaseAcademicReport(
  @Json(name = "id") val id: Long? = null,
  @Json(name = "student_id") val studentId: Long,
  @Json(name = "subject") val subject: String,
  @Json(name = "score") val score: Int,
  @Json(name = "category") val category: String,
  @Json(name = "teacher_feedback") val teacherFeedback: String,
  @Json(name = "badge") val badge: String,
  @Json(name = "date") val date: String
)

@JsonClass(generateAdapter = true)
data class SupabaseSavingTransaction(
  @Json(name = "id") val id: Long? = null,
  @Json(name = "student_id") val studentId: Long,
  @Json(name = "amount") val amount: Long,
  @Json(name = "type") val type: String,
  @Json(name = "note") val note: String,
  @Json(name = "timestamp") val timestamp: Long = System.currentTimeMillis(),
  @Json(name = "formatted_date") val formattedDate: String
)

@JsonClass(generateAdapter = true)
data class SupabaseAttendanceRecord(
  @Json(name = "id") val id: Long? = null,
  @Json(name = "student_id") val studentId: Long,
  @Json(name = "student_name") val studentName: String,
  @Json(name = "date") val date: String,
  @Json(name = "status") val status: String,
  @Json(name = "time") val time: String,
  @Json(name = "note") val note: String
)

@JsonClass(generateAdapter = true)
data class SupabaseSchoolActivity(
  @Json(name = "id") val id: Long? = null,
  @Json(name = "title") val title: String,
  @Json(name = "category") val category: String,
  @Json(name = "date") val date: String,
  @Json(name = "description") val description: String,
  @Json(name = "likes") val likes: Int = 12,
  @Json(name = "photo_type") val photoType: String = "hero_banner"
)

@JsonClass(generateAdapter = true)
data class SupabaseChatMessage(
  @Json(name = "id") val id: Long? = null,
  @Json(name = "sender_role") val senderRole: String,
  @Json(name = "sender_name") val senderName: String,
  @Json(name = "text") val text: String,
  @Json(name = "time") val time: String,
  @Json(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)

@JsonClass(generateAdapter = true)
data class SupabaseExamSchedule(
  @Json(name = "id") val id: Long? = null,
  @Json(name = "subject") val subject: String,
  @Json(name = "exam_date") val examDate: String,
  @Json(name = "time_range") val timeRange: String,
  @Json(name = "room") val room: String,
  @Json(name = "syllabus_summary") val syllabusSummary: String
)
