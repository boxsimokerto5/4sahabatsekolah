package com.example.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApi {

  // Students
  @GET("students?select=*")
  suspend fun getStudents(): Response<List<SupabaseStudent>>

  @Headers("Prefer: return=representation")
  @POST("students")
  suspend fun insertStudent(@Body student: SupabaseStudent): Response<List<SupabaseStudent>>

  // Dismissal
  @GET("dismissal_alerts?select=*&order=id.desc&limit=1")
  suspend fun getLatestDismissal(): Response<List<SupabaseDismissalAlert>>

  @Headers("Prefer: return=representation")
  @POST("dismissal_alerts")
  suspend fun insertDismissal(@Body alert: SupabaseDismissalAlert): Response<List<SupabaseDismissalAlert>>

  // Pickup Queues
  @GET("pickup_queues?select=*&order=timestamp.desc")
  suspend fun getPickupQueues(): Response<List<SupabasePickupQueue>>

  @Headers("Prefer: return=representation")
  @POST("pickup_queues")
  suspend fun insertPickupQueue(@Body queue: SupabasePickupQueue): Response<List<SupabasePickupQueue>>

  @Headers("Prefer: return=representation")
  @PATCH("pickup_queues")
  suspend fun updatePickupStatus(
    @Query("id") idFilter: String, // e.g. "eq.5"
    @Body body: Map<String, String>
  ): Response<List<SupabasePickupQueue>>

  // Academic Reports
  @GET("academic_reports?select=*&order=id.desc")
  suspend fun getAcademicReports(): Response<List<SupabaseAcademicReport>>

  @Headers("Prefer: return=representation")
  @POST("academic_reports")
  suspend fun insertAcademicReport(@Body report: SupabaseAcademicReport): Response<List<SupabaseAcademicReport>>

  // Savings
  @GET("saving_transactions?select=*&order=id.desc")
  suspend fun getSavingTransactions(): Response<List<SupabaseSavingTransaction>>

  @Headers("Prefer: return=representation")
  @POST("saving_transactions")
  suspend fun insertSavingTransaction(@Body transaction: SupabaseSavingTransaction): Response<List<SupabaseSavingTransaction>>

  // Attendance
  @GET("attendance_records?select=*&order=id.desc")
  suspend fun getAttendanceRecords(): Response<List<SupabaseAttendanceRecord>>

  @Headers("Prefer: return=representation")
  @POST("attendance_records")
  suspend fun insertAttendanceRecord(@Body record: SupabaseAttendanceRecord): Response<List<SupabaseAttendanceRecord>>

  // Gallery Activities
  @GET("school_activities?select=*&order=id.desc")
  suspend fun getSchoolActivities(): Response<List<SupabaseSchoolActivity>>

  @Headers("Prefer: return=representation")
  @POST("school_activities")
  suspend fun insertSchoolActivity(@Body activity: SupabaseSchoolActivity): Response<List<SupabaseSchoolActivity>>

  // Chat
  @GET("chat_messages?select=*&order=id.asc")
  suspend fun getChatMessages(): Response<List<SupabaseChatMessage>>

  @Headers("Prefer: return=representation")
  @POST("chat_messages")
  suspend fun insertChatMessage(@Body message: SupabaseChatMessage): Response<List<SupabaseChatMessage>>

  // Exams
  @GET("exam_schedules?select=*&order=id.asc")
  suspend fun getExamSchedules(): Response<List<SupabaseExamSchedule>>
}
