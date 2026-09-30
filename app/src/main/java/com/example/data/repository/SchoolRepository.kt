package com.example.data.repository

import android.util.Log
import com.example.data.local.SchoolDao
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
import com.example.data.remote.SupabaseAcademicReport
import com.example.data.remote.SupabaseAttendanceRecord
import com.example.data.remote.SupabaseChatMessage
import com.example.data.remote.SupabaseClientManager
import com.example.data.remote.SupabaseDismissalAlert
import com.example.data.remote.SupabasePickupQueue
import com.example.data.remote.SupabaseSavingTransaction
import com.example.data.remote.SupabaseSchoolActivity
import com.example.data.remote.SupabaseStudent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class SchoolRepository(
  private val dao: SchoolDao,
  val supabaseManager: SupabaseClientManager
) {

  val mainStudent: Flow<Student?> = dao.getMainStudent()
  val latestDismissal: Flow<DismissalAlert?> = dao.getLatestDismissalAlert()
  val pickupQueues: Flow<List<PickupQueue>> = dao.getPickupQueues()
  val academicReports: Flow<List<AcademicReport>> = dao.getAllAcademicReports()
  val savingTransactions: Flow<List<SavingTransaction>> = dao.getAllSavingTransactions()
  val attendanceRecords: Flow<List<AttendanceRecord>> = dao.getAllAttendanceRecords()
  val schoolActivities: Flow<List<SchoolActivity>> = dao.getAllSchoolActivities()
  val chatMessages: Flow<List<ChatMessage>> = dao.getAllChatMessages()
  val examSchedules: Flow<List<ExamSchedule>> = dao.getAllExamSchedules()
  val academicCalendarEvents: Flow<List<AcademicCalendarEvent>> = dao.getAllAcademicCalendarEvents()
  val announcements: Flow<List<SchoolAnnouncement>> = dao.getAllAnnouncements()
  val schoolProfile: Flow<SchoolProfile?> = dao.getSchoolProfile()
  val allSchoolProfiles: Flow<List<SchoolProfile>> = dao.getAllSchoolProfiles()
  val classrooms: Flow<List<ClassroomRoom>> = dao.getAllClassrooms()
  val teachers: Flow<List<TeacherAccount>> = dao.getAllTeachers()
  val parentStudentAccounts: Flow<List<ParentStudentAccount>> = dao.getAllParentStudentAccounts()
  val timetableLessons: Flow<List<TimetableLesson>> = dao.getAllTimetableLessons()
  val dailyUniforms: Flow<List<DailyUniform>> = dao.getAllDailyUniforms()

  suspend fun initializeDefaultDataIfEmpty() {
    val existingStudent = dao.getMainStudent().firstOrNull()
    if (existingStudent == null) {
      // Seed Student
      val studentId = dao.insertStudent(
        Student(
          name = "Rafa Al-Ghifari",
          gradeClass = "Kelas 2-B",
          studentNumber = "NISN 00928371",
          parentName = "Bunda Dina & Ayah Farhan",
          avatarColorHex = "#FF8A80",
          targetSavings = 250000L
        )
      )

      // Seed Dismissal Alert
      dao.insertDismissalAlert(
        DismissalAlert(
          gradeClass = "Kelas 2-B",
          isDismissed = true,
          dismissalTime = "12:30 WIB",
          title = "Bel Pulang Sekolah Berbunyi!",
          message = "Pelajaran hari ini telah selesai. Anak-anak sedang bersiap di Lobi Depan dengan pengawasan Wali Kelas.",
          announcedBy = "Bu Sarah, S.Pd (Wali Kelas 2B)"
        )
      )

      // Seed Pickup Queue
      dao.insertPickupQueue(
        PickupQueue(
          studentName = "Rafa Al-Ghifari",
          parentName = "Bunda Dina",
          gateLocation = "Gerbang Depan (Pintu A)",
          status = "MENUNGGU"
        )
      )

      // Seed Savings
      dao.insertSavingTransaction(
        SavingTransaction(
          studentId = studentId,
          amount = 50000L,
          type = "MASUK",
          note = "Setoran Tabungan Mingguan",
          formattedDate = "22 Sep 2026"
        )
      )
      dao.insertSavingTransaction(
        SavingTransaction(
          studentId = studentId,
          amount = 40000L,
          type = "MASUK",
          note = "Sisa Uang Saku & Tabungan Kebaikan",
          formattedDate = "24 Sep 2026"
        )
      )
      dao.insertSavingTransaction(
        SavingTransaction(
          studentId = studentId,
          amount = 15000L,
          type = "KELUAR",
          note = "Pembelian Buku Gambar di Koperasi",
          formattedDate = "25 Sep 2026"
        )
      )
      dao.insertSavingTransaction(
        SavingTransaction(
          studentId = studentId,
          amount = 75000L,
          type = "MASUK",
          note = "Hadiah Rapor Bagus dari Nenek",
          formattedDate = "26 Sep 2026"
        )
      )

      // Seed Academic Reports
      dao.insertAcademicReport(
        AcademicReport(
          studentId = studentId,
          subject = "Matematika",
          score = 95,
          category = "Ulangan Harian",
          teacherFeedback = "Sangat cepat dan teliti menyelesaikan penjumlahan bersusun 3 angka. Terus pertahankan ya Rafa!",
          badge = "⭐ Bintang Teliti",
          date = "26 Sep 2026"
        )
      )
      dao.insertAcademicReport(
        AcademicReport(
          studentId = studentId,
          subject = "Bahasa Indonesia",
          score = 90,
          category = "Tugas Membaca",
          teacherFeedback = "Artikulasi dan intonasi membaca fabel di depan kelas sangat percaya diri.",
          badge = "📚 Bintang Membaca",
          date = "24 Sep 2026"
        )
      )
      dao.insertAcademicReport(
        AcademicReport(
          studentId = studentId,
          subject = "Seni Budaya & Prakarya",
          score = 96,
          category = "Proyek Karya",
          teacherFeedback = "Karya kolase daun kering dan origami burung bangau sangat rapi dan estetik.",
          badge = "🎨 Juara Kreatif",
          date = "21 Sep 2026"
        )
      )
      dao.insertAcademicReport(
        AcademicReport(
          studentId = studentId,
          subject = "Pendidikan Agama",
          score = 92,
          category = "Praktik Ibadah",
          teacherFeedback = "Hafalan doa harian dan tata cara wudhu sangat baik dan tertib.",
          badge = "🌟 Bintang Budi Pekerti",
          date = "18 Sep 2026"
        )
      )

      // Seed Attendance
      dao.insertAttendanceRecord(
        AttendanceRecord(
          studentId = studentId,
          studentName = "Rafa Al-Ghifari",
          date = "Hari Ini (27 Sep)",
          status = "HADIR",
          time = "06:55 WIB",
          note = "Datang dengan ceria & langsung merapikan tas"
        )
      )
      dao.insertAttendanceRecord(
        AttendanceRecord(
          studentId = studentId,
          studentName = "Rafa Al-Ghifari",
          date = "26 Sep 2026",
          status = "HADIR",
          time = "07:02 WIB",
          note = "Tepat waktu"
        )
      )
      dao.insertAttendanceRecord(
        AttendanceRecord(
          studentId = studentId,
          studentName = "Rafa Al-Ghifari",
          date = "25 Sep 2026",
          status = "HADIR",
          time = "06:58 WIB",
          note = "Tepat waktu"
        )
      )

      // Seed School Activities (Gallery)
      dao.insertSchoolActivity(
        SchoolActivity(
          title = "Eksplorasi Sains Cilik: Warna & Gelembung Pelangi",
          category = "Prakarya",
          date = "26 Sep 2026",
          description = "Anak-anak kelas 2B belajar sifat air dan sabun di taman sekolah. Rafa dan teman-teman sangat antusias!",
          likes = 42,
          photoType = "hero_banner"
        )
      )
      dao.insertSchoolActivity(
        SchoolActivity(
          title = "Jumat Bersepeda & Senam Sehat Ria",
          category = "Olahraga",
          date = "25 Sep 2026",
          description = "Semangat pagi yang bugar! Anak-anak melakukan senam irama ceria bersama seluruh guru di lapangan utama.",
          likes = 38,
          photoType = "sports"
        )
      )
      dao.insertSchoolActivity(
        SchoolActivity(
          title = "Pentas Musik Angklung Nusantara",
          category = "Pentas",
          date = "23 Sep 2026",
          description = "Gladi resik pertunjukan angklung lagu 'Halo-Halo Bandung' untuk persiapan perayaan Hari Pahlawan.",
          likes = 51,
          photoType = "art"
        )
      )

      // Seed Chat Messages
      dao.insertChatMessage(
        ChatMessage(
          senderRole = "GURU",
          senderName = "Bu Sarah (Wali Kelas)",
          text = "Assalamu'alaikum Bunda Dina, mengingatkan besok hari Senin ada upacara bendera, mohon ananda Rafa memakai topi dan dasi lengkap ya.",
          time = "07:15 WIB"
        )
      )
      dao.insertChatMessage(
        ChatMessage(
          senderRole = "ORANG_TUA",
          senderName = "Bunda Dina",
          text = "Wa'alaikumsalam Bu Sarah. Siap Bu, seragam dan atribut topi sudah disiapkan di tas Rafa. Terima kasih banyak pengingatnya!",
          time = "07:22 WIB"
        )
      )

      // Seed Exam Schedules
      dao.insertExamSchedule(
        ExamSchedule(
          subject = "Matematika",
          examDate = "Senin, 05 Okt 2026",
          timeRange = "07:30 - 09:00 WIB",
          room = "Ruang Kelas 2B",
          syllabusSummary = "Penjumlahan & Pengurangan sampai 500, Nilai Tempat Ratusan, Soal Cerita Satuan Panjang (cm & m)"
        )
      )
      dao.insertExamSchedule(
        ExamSchedule(
          subject = "Bahasa Indonesia",
          examDate = "Selasa, 06 Okt 2026",
          timeRange = "07:30 - 09:00 WIB",
          room = "Ruang Kelas 2B",
          syllabusSummary = "Membaca Pemahaman Teks Pendek, Huruf Kapital & Tanda Titik, Kalimat Ajakan dan Permintaan Tolong"
        )
      )
      dao.insertExamSchedule(
        ExamSchedule(
          subject = "Pendidikan Pancasila & Agama",
          examDate = "Rabu, 07 Okt 2026",
          timeRange = "07:30 - 09:00 WIB",
          room = "Ruang Kelas 2B",
          syllabusSummary = "Simbol & Sila Pancasila dalam Keluarga, Adab Berteman, Doa Sebelum & Sesudah Kegiatan"
        )
      )
    }

    // Seed Academic Calendar Events if empty
    val existingEvents = dao.getAllAcademicCalendarEvents().firstOrNull()
    if (existingEvents.isNullOrEmpty()) {
      dao.insertAcademicCalendarEvent(
        AcademicCalendarEvent(
          title = "Kunjungan Edukasi Planetarium Jakarta",
          date = "2026-10-08",
          timeRange = "08:00 - 12:30 WIB",
          targetClass = "Kelas 2-B",
          category = "FIELD_TRIP",
          location = "Planetarium TIM Cikini, Jakarta Pusat",
          description = "Eksplorasi tata surya, rasi bintang, dan simulasi langit malam 3D untuk memperkaya materi IPAS tema Benda Langit.",
          requiredItems = "Buku catatan kecil & pensil, Botol minum air putih, Topi sekolah seragam, Jas hujan lipat kecil, Uang jajan secukupnya (maks. 15rb)",
          checkedItems = "Botol minum air putih, Topi sekolah seragam",
          rsvpStatus = "HADIR",
          createdBy = "Bu Sarah, S.Pd (Wali Kelas 2B)"
        )
      )
      dao.insertAcademicCalendarEvent(
        AcademicCalendarEvent(
          title = "Pekan PTS Ganjil: Matematika & Tematik",
          date = "2026-10-05",
          timeRange = "07:30 - 11:30 WIB",
          targetClass = "Semua Kelas",
          category = "UJIAN",
          location = "Ruang Kelas 2-B",
          description = "Penilaian Tengah Semester hari pertama mata pelajaran Matematika dan Pendidikan Karakter Pancasila.",
          requiredItems = "Pensil 2B (2 buah), Penghapus bersih, Penggaris 30cm, Kartu nomor peserta ujian",
          checkedItems = "Pensil 2B (2 buah), Penghapus bersih",
          rsvpStatus = "HADIR",
          createdBy = "Panitia PTS Sekolah"
        )
      )
      dao.insertAcademicCalendarEvent(
        AcademicCalendarEvent(
          title = "Peringatan Hari Batik & Fashion Show Cilik",
          date = "2026-10-02",
          timeRange = "08:00 - 10:30 WIB",
          targetClass = "Semua Kelas",
          category = "PENTAS_SENI",
          location = "Panggung Ceria Lapangan Sekolah",
          description = "Peringatan Hari Batik Nasional. Siswa mengenakan busana batik sopan rapi dan mengenal filosofi canting batik.",
          requiredItems = "Baju batik bebas rapi, Sepatu sekolah hitam, Kaos ganti cadangan",
          checkedItems = "Baju batik bebas rapi",
          rsvpStatus = "HADIR",
          createdBy = "Sie Kesiswaan & Budaya"
        )
      )
      dao.insertAcademicCalendarEvent(
        AcademicCalendarEvent(
          title = "Pertemuan Komite & Wali Murid Kelas 2",
          date = "2026-10-17",
          timeRange = "09:00 - 11:30 WIB",
          targetClass = "Kelas 2-B",
          category = "PERTEMUAN",
          location = "Aula Serbaguna Lantai 2",
          description = "Sosialisasi program semester genap, parenting pemantauan gawai ramah anak, dan evaluasi hasil belajar tengah semester.",
          requiredItems = "Buku penghubung siswa, Buku catatan pertanyaan wali murid",
          checkedItems = "",
          rsvpStatus = "BELUM_KONFIRMASI",
          createdBy = "Bu Sarah & Komite Kelas 2B"
        )
      )
      dao.insertAcademicCalendarEvent(
        AcademicCalendarEvent(
          title = "Bazar Kewirausahaan & Pojok Literasi Ceria",
          date = "2026-10-28",
          timeRange = "08:00 - 12:00 WIB",
          targetClass = "Semua Kelas",
          category = "PENTAS_SENI",
          location = "Area Selasar & Lapangan Upacara",
          description = "Peringatan Sumpah Pemuda. Melatih kemandirian berwirausaha cilik dan stan pojok dongeng anak.",
          requiredItems = "Produk prakarya daur ulang, Celemek mini, Kantong belanja ramah lingkungan",
          checkedItems = "",
          rsvpStatus = "BELUM_KONFIRMASI",
          createdBy = "Bu Sarah, S.Pd"
        )
      )
      dao.insertAcademicCalendarEvent(
        AcademicCalendarEvent(
          title = "Pemeriksaan Kesehatan Gigi & Tumbuh Kembang",
          date = "2026-11-12",
          timeRange = "08:30 - 11:00 WIB",
          targetClass = "Kelas 2-B",
          category = "FIELD_TRIP",
          location = "Ruang UKS & Kelas 2B",
          description = "Pemeriksaan berkala kesehatan gigi, penimbangan berat/tinggi badan bersama tim Puskesmas Kecamatan.",
          requiredItems = "Buku KMS/Kesehatan anak, Sikat gigi anak bersih",
          checkedItems = "",
          rsvpStatus = "BELUM_KONFIRMASI",
          createdBy = "Pembina UKS Sekolah"
        )
      )
      dao.insertAcademicCalendarEvent(
        AcademicCalendarEvent(
          title = "Libur Sekolah: Cuti Bersama Maulid Nabi",
          date = "2026-09-16",
          timeRange = "07:00 - 17:00 WIB",
          targetClass = "Semua Kelas",
          category = "LIBUR",
          location = "Di Rumah Masing-masing",
          description = "Libur resmi nasional memperingati Maulid Nabi Muhammad SAW. Pembelajaran ditiadakan.",
          requiredItems = "",
          checkedItems = "",
          rsvpStatus = "HADIR",
          createdBy = "Kepala Sekolah"
        )
      )
    }

    // Seed School Announcements if empty
    val existingAnnouncements = dao.getAllAnnouncements().firstOrNull()
    if (existingAnnouncements.isNullOrEmpty()) {
      dao.insertAnnouncement(
        SchoolAnnouncement(
          title = "Surat Edaran: Pelaksanaan Kunjungan Edukasi (Outing) Planetarium",
          letterNumber = "048/SD-SS/SE/X/2026",
          category = "URGENT",
          targetAudience = "Kelas 2-B",
          content = "Sehubungan dengan penguatan materi IPAS bertema Tata Surya, sekolah akan menyelenggarakan Kunjungan Edukasi ke Planetarium TIM Cikini Jakarta pada hari Kamis, 8 Oktober 2026.\n\nKetentuan siswa:\n1. Tiba di sekolah maksimal pukul 07.15 WIB mengenakan seragam olahraga rapi dan topi sekolah.\n2. Membawa bekal makanan sehat, air minum botol, serta jas hujan lipat kecil.\n3. Kepulangan diperkirakan pukul 12.30 WIB di Gerbang Depan.\n\nMohon orang tua mengisi konfirmasi tanda terima dan checklist perlengkapan di aplikasi.",
          author = "Bu Sarah, S.Pd & Kepala Sekolah",
          date = "28 Sep 2026",
          isPinned = true,
          isReadByParent = true,
          readCount = 26,
          totalRecipients = 28,
          attachmentTitle = "SE_Kunjungan_Planetarium_Oktober_2026.pdf"
        )
      )
      dao.insertAnnouncement(
        SchoolAnnouncement(
          title = "Kebijakan Busana & Apresiasi Peringatan Hari Batik Nasional 2026",
          letterNumber = "045/SD-SS/SE/IX/2026",
          category = "EDARAN",
          targetAudience = "Semua Wali Murid",
          content = "Dalam rangka memperingati Hari Batik Nasional pada hari Jumat, 2 Oktober 2026, seluruh siswa-siswi dan dewan guru dihimbau mengenakan pakaian batik nusantara bebas rapi dan bersepatu hitam.\n\nKegiatan diisi dengan parade busana cilik dan pengenalan filosofi motif batik nusantara di panggung ceria sekolah. Upacara bendera formal ditiadakan.",
          author = "Drs. H. Mulyono (Kepala Sekolah)",
          date = "25 Sep 2026",
          isPinned = true,
          isReadByParent = false,
          readCount = 22,
          totalRecipients = 28,
          attachmentTitle = "Edaran_Hari_Batik_Nasional_2026.pdf"
        )
      )
      dao.insertAnnouncement(
        SchoolAnnouncement(
          title = "Jadwal Pemeriksaan Berkala Kesehatan Gigi & Tumbuh Kembang Anak",
          letterNumber = "041/SD-SS/UKS/IX/2026",
          category = "AKADEMIK",
          targetAudience = "Kelas 2-B",
          content = "Pemeriksaan kesehatan berkala akan diselenggarakan pada Kamis, 12 November 2026 bekerjasama dengan tim medis Puskesmas Kecamatan. Pemeriksaan meliputi kesehatan gigi anak, kebersihan telinga, penimbangan berat/tinggi badan, serta visus mata.\n\nBuku catatan kesehatan / KMS anak mohon dibawa di tas sekolah.",
          author = "Pembina UKS Sekolah",
          date = "20 Sep 2026",
          isPinned = false,
          isReadByParent = false,
          readCount = 20,
          totalRecipients = 28,
          attachmentTitle = "Brosur_Pemeriksaan_UKS_2026.pdf"
        )
      )
      dao.insertAnnouncement(
        SchoolAnnouncement(
          title = "Layanan Pembayaran SPP & Donasi Pengayaan Pojok Baca Kelas",
          letterNumber = "038/SD-SS/TU/IX/2026",
          category = "KEGIATAN",
          targetAudience = "Semua Wali Murid",
          content = "Tata Usaha sekolah menginformasikan kemudahan pembayaran SPP bulan Oktober via Virtual Account Bank Mitra. Bersamaan dengan itu, Komite Sekolah membuka pengumpulan donasi buku bacaan ramah anak layak pakai untuk melengkapi pojok baca ruang kelas.",
          author = "Tata Usaha & Komite Sekolah",
          date = "15 Sep 2026",
          isPinned = false,
          isReadByParent = true,
          readCount = 28,
          totalRecipients = 28,
          attachmentTitle = ""
        )
      )
    }

    // Seed School Profile if empty
    val existingProfile = dao.getSchoolProfile().firstOrNull()
    if (existingProfile == null) {
      dao.insertOrUpdateSchoolProfile(
        SchoolProfile(
          schoolName = "SD Ceria Bangsa",
          npsn = "20104829",
          level = "Sekolah Dasar (SD)",
          accreditation = "A (Unggul)",
          address = "Jl. Cikini Raya No. 45, Menteng",
          city = "Jakarta Pusat, DKI Jakarta",
          phone = "021-3908271",
          email = "admin@ceriabangsa.sch.id",
          principalName = "Drs. H. Mulyono, M.Pd",
          applicantName = "Drs. H. Mulyono, M.Pd",
          applicantNik = "3171051203750001",
          applicantPhone = "081289001234",
          applicantRole = "Kepala Sekolah",
          applicantAddress = "Jl. Diponegoro No. 12, Menteng",
          assignmentLetterFileName = "SK_Penugasan_Kepala_Sekolah_2026.pdf",
          adminUsername = "admin",
          adminPassword = "admin",
          isVerified = true,
          status = "VERIFIED",
          registeredAt = "20 Agu 2026",
          totalClassrooms = 3,
          totalTeachers = 3,
          totalStudents = 86
        )
      )
      // Seed a pending school waiting for Superadmin approval
      dao.insertOrUpdateSchoolProfile(
        SchoolProfile(
          schoolName = "SDIT Bintang Madani",
          npsn = "20219483",
          level = "Sekolah Dasar (SD / MI)",
          accreditation = "B (Baik)",
          address = "Jl. Riau No. 108, Citarum",
          city = "Bandung, Jawa Barat",
          phone = "022-4209182",
          email = "tatausaha@bintangmadani.sch.id",
          principalName = "Hj. Siti Rahmah, S.Pd.I",
          applicantName = "Hj. Siti Rahmah, S.Pd.I",
          applicantNik = "3273016405820002",
          applicantPhone = "081398712345",
          applicantRole = "Kepala Sekolah",
          applicantAddress = "Jl. Dago Asri No. 14, Bandung",
          assignmentLetterFileName = "SK_Yayasan_Pendidikan_Bintang_Madani_2026.pdf",
          adminUsername = "bintangmadani",
          adminPassword = "123",
          isVerified = false,
          status = "PENDING",
          registeredAt = "28 Sep 2026",
          totalClassrooms = 0,
          totalTeachers = 0,
          totalStudents = 0
        )
      )
      // Seed a verified SMP school
      dao.insertOrUpdateSchoolProfile(
        SchoolProfile(
          schoolName = "SMP Teladan Nusantara",
          npsn = "20501923",
          level = "Sekolah Menengah Pertama (SMP)",
          accreditation = "A (Unggul)",
          address = "Jl. Pemuda No. 22, Genteng",
          city = "Surabaya, Jawa Timur",
          phone = "031-5348910",
          email = "info@teladannusantara.sch.id",
          principalName = "Dr. Hendra Wijaya, M.M.",
          applicantName = "Farid Ardiansyah, S.Kom",
          applicantNik = "3578021509900003",
          applicantPhone = "081230998877",
          applicantRole = "Operator IT / Dapodik",
          applicantAddress = "Jl. Darmo Permai No. 8, Surabaya",
          assignmentLetterFileName = "SK_Dinas_Pendidikan_Surabaya_2026.pdf",
          adminUsername = "teladan",
          adminPassword = "123",
          isVerified = true,
          status = "VERIFIED",
          registeredAt = "15 Agu 2026",
          totalClassrooms = 6,
          totalTeachers = 12,
          totalStudents = 180
        )
      )
    }

    // Seed Classrooms if empty
    val existingClassrooms = dao.getAllClassrooms().firstOrNull()
    if (existingClassrooms.isNullOrEmpty()) {
      dao.insertClassroom(ClassroomRoom(name = "Kelas 1-A", gradeLevel = "Kelas 1", academicYear = "2026/2027", homeroomTeacherName = "Bu Ratna, S.Pd", studentCount = 28, maxCapacity = 30))
      dao.insertClassroom(ClassroomRoom(name = "Kelas 2-B", gradeLevel = "Kelas 2", academicYear = "2026/2027", homeroomTeacherName = "Bu Sarah, S.Pd", studentCount = 28, maxCapacity = 30))
      dao.insertClassroom(ClassroomRoom(name = "Kelas 3-A", gradeLevel = "Kelas 3", academicYear = "2026/2027", homeroomTeacherName = "Pak Bambang, S.Pd", studentCount = 30, maxCapacity = 32))
    }

    // Seed Teachers if empty
    val existingTeachers = dao.getAllTeachers().firstOrNull()
    if (existingTeachers.isNullOrEmpty()) {
      dao.insertTeacher(TeacherAccount(fullName = "Bu Sarah, S.Pd", nip = "19850712 201001 2 003", phone = "081234567890", assignedClass = "Kelas 2-B", username = "guru", password = "guru"))
      dao.insertTeacher(TeacherAccount(fullName = "Bu Ratna, S.Pd", nip = "19880315 201402 2 001", phone = "081345678901", assignedClass = "Kelas 1-A", username = "ratna", password = "123"))
      dao.insertTeacher(TeacherAccount(fullName = "Pak Bambang, S.Pd", nip = "19821105 200801 1 004", phone = "081567890123", assignedClass = "Kelas 3-A", username = "bambang", password = "123"))
    }

    // Seed Parent-Student accounts if empty
    val existingParentAccounts = dao.getAllParentStudentAccounts().firstOrNull()
    if (existingParentAccounts.isNullOrEmpty()) {
      dao.insertParentStudentAccount(ParentStudentAccount(studentName = "Rafa Al-Ghifari", nisn = "00928371", studentClass = "Kelas 2-B", parentName = "Bunda Dina & Ayah Farhan", parentPhone = "081298765432", username = "ortu", password = "ortu"))
      dao.insertParentStudentAccount(ParentStudentAccount(studentName = "Alisha Zahra", nisn = "00928372", studentClass = "Kelas 2-B", parentName = "Ibu Nita", parentPhone = "081298765433", username = "nita", password = "123"))
      dao.insertParentStudentAccount(ParentStudentAccount(studentName = "Kenzo Arkan", nisn = "00928373", studentClass = "Kelas 2-B", parentName = "Ayah Dimas", parentPhone = "081298765434", username = "dimas", password = "123"))
    }

    // Seed Daily Uniforms if empty
    val existingUniforms = dao.getAllDailyUniforms().firstOrNull()
    if (existingUniforms.isNullOrEmpty()) {
      dao.insertDailyUniform(
        DailyUniform(
          dayOfWeek = "Senin",
          uniformTitle = "Seragam Merah Putih Nasional",
          description = "Kemeja putih pendek, celana/rok merah hati, dasi merah sekolah, kaos kaki putih polos, sabuk hitam sekolah berlogo.",
          accessories = "Topi Upacara Merah-Putih, Dasi Merah, Sabuk Sekolah Berlogo",
          shoesColor = "Sepatu Hitam Polos & Kaos Kaki Putih",
          badgeCategory = "Seragam Nasional (Upacara Bendera)",
          previewColorHex = "#DC2626"
        )
      )
      dao.insertDailyUniform(
        DailyUniform(
          dayOfWeek = "Selasa",
          uniformTitle = "Kemeja Kotak-Kotak Khas SD Ceria Bangsa",
          description = "Kemeja motif kotak-kotak oranye khas sekolah, celana/rok putih rapi, badge nama dada terpasang rapi.",
          accessories = "Badge Lokasi & Nama Dada Siswa, Sabuk Sekolah",
          shoesColor = "Sepatu Hitam / Dominan Gelap",
          badgeCategory = "Seragam Khusus Identitas Sekolah",
          previewColorHex = "#EA580C"
        )
      )
      dao.insertDailyUniform(
        DailyUniform(
          dayOfWeek = "Rabu",
          uniformTitle = "Batik Ceria Nusantara",
          description = "Kemeja batik bernuansa biru langit motif nusantara ceria, celana/rok putih, rompi biru muda.",
          accessories = "Ikat Pinggang Sekolah, Papan Nama Dada",
          shoesColor = "Sepatu Hitam Polos",
          badgeCategory = "Seragam Batik Budaya Sekolah",
          previewColorHex = "#0284C7"
        )
      )
      dao.insertDailyUniform(
        DailyUniform(
          dayOfWeek = "Kamis",
          uniformTitle = "Kaos Olahraga Ceria (PJOK)",
          description = "Kaos olahraga ceria kombinasi kuning-hijau tosca sekolah dan celana training panjang elastis.",
          accessories = "Handuk Kecil UKS, Botol Minum Tumbler Pribadi, Topi Pelindung Panas",
          shoesColor = "Sepatu Olahraga / Kets Nyaman Bertali atau Velcro",
          badgeCategory = "Seragam Olahraga & Kebugaran",
          previewColorHex = "#10B981"
        )
      )
      dao.insertDailyUniform(
        DailyUniform(
          dayOfWeek = "Jumat",
          uniformTitle = "Pramuka Siaga Lengkap",
          description = "Kemeja pramuka siaga cokelat muda, celana/rok cokelat tua, kacu/hasduk merah putih lengkap ring/kolong.",
          accessories = "Kacu Merah-Putih & Ring Kolong Siaga, Topi Baret / Bonet Cokelat, Tanda Barung",
          shoesColor = "Sepatu Hitam Polos & Kaos Kaki Hitam",
          badgeCategory = "Pramuka Siaga & Karakter Mandiri",
          previewColorHex = "#92400E"
        )
      )
    }

    // Seed Timetable Lessons if empty
    val existingLessons = dao.getAllTimetableLessons().firstOrNull()
    if (existingLessons.isNullOrEmpty()) {
      // Senin
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Senin", periodNumber = 1, timeRange = "07.00 - 07.45 WIB", subject = "Upacara Bendera Merah Putih", teacherName = "Wali Kelas & Dewan Guru", roomName = "Lapangan Upacara Utama", requiredItems = "Topi & Dasi Wajib Terpasang Rapi", colorHex = "#EF4444"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Senin", periodNumber = 2, timeRange = "07.45 - 09.00 WIB", subject = "Pendidikan Pancasila & Kewarganegaraan", teacherName = "Bu Sarah, S.Pd", roomName = "Kelas 2-B", requiredItems = "Buku Paket PPKn 2A, Buku Tulis, Tempat Pensil", colorHex = "#3B82F6"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Senin", periodNumber = 3, timeRange = "09.00 - 09.30 WIB", subject = "Istirahat Sehat & Snack Time", teacherName = "Guru Piket", roomName = "Kantin Sehat / Selasar", requiredItems = "Bekal Buah / Snack Sehat & Air Minum", colorHex = "#10B981"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Senin", periodNumber = 4, timeRange = "09.30 - 10.45 WIB", subject = "Bahasa Indonesia: Membaca Lancar", teacherName = "Bu Sarah, S.Pd", roomName = "Kelas 2-B", requiredItems = "Buku Tulis Garis Tiga, Buku Cerita Bergambar", colorHex = "#8B5CF6"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Senin", periodNumber = 5, timeRange = "10.45 - 11.45 WIB", subject = "Seni Budaya & Prakarya (SBdP)", teacherName = "Bu Endah, S.Pd", roomName = "Studio Kreatif / Kelas 2-B", requiredItems = "Buku Gambar A4, Krayon 24 Warna, Penggaris", colorHex = "#EC4899"))

      // Selasa
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Selasa", periodNumber = 1, timeRange = "07.15 - 08.30 WIB", subject = "Matematika Ceria: Operasi Hitung", teacherName = "Pak Bambang, M.Pd", roomName = "Kelas 2-B", requiredItems = "Buku Matematika Jilid 2A, Penggaris 30cm, Pensil 2B", colorHex = "#F59E0B"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Selasa", periodNumber = 2, timeRange = "08.30 - 09.30 WIB", subject = "Tematik Terpadu: Hidup Rukun", teacherName = "Bu Sarah, S.Pd", roomName = "Kelas 2-B", requiredItems = "Buku Tematik 2B, Catatan Harian Siswa", colorHex = "#3B82F6"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Selasa", periodNumber = 3, timeRange = "09.30 - 10.00 WIB", subject = "Istirahat & Bermain Bersama", teacherName = "Guru Piket", roomName = "Halaman Bermain Hijau", requiredItems = "Bekal Roti Sehat & Air Mineral", colorHex = "#10B981"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Selasa", periodNumber = 4, timeRange = "10.00 - 11.15 WIB", subject = "Bahasa Inggris Cilik: Daily Greetings", teacherName = "Ms. Amanda, B.A", roomName = "Lab Bahasa Ceria", requiredItems = "Buku My Next Words Grade 2, Kartu Kosakata", colorHex = "#06B6D4"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Selasa", periodNumber = 5, timeRange = "11.15 - 11.45 WIB", subject = "Literasi & Pojok Baca Ceria", teacherName = "Bu Sarah, S.Pd", roomName = "Pojok Baca Kelas 2-B", requiredItems = "Buku Cerita Fabel Pilihan", colorHex = "#6366F1"))

      // Rabu
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Rabu", periodNumber = 1, timeRange = "07.15 - 08.30 WIB", subject = "Pendidikan Agama & Budi Pekerti", teacherName = "Ustadz Fauzi, S.Ag", roomName = "Musholla Al-Ikhlas", requiredItems = "Buku PAI, Iqro / Juz Amma, Sajadah Lipat", colorHex = "#10B981"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Rabu", periodNumber = 2, timeRange = "08.30 - 09.30 WIB", subject = "Tematik: Menyayangi Hewan & Tumbuhan", teacherName = "Bu Sarah, S.Pd", roomName = "Kelas 2-B", requiredItems = "Buku Tematik 2C, Contoh Daun Kering", colorHex = "#14B8A6"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Rabu", periodNumber = 3, timeRange = "09.30 - 10.00 WIB", subject = "Istirahat & Snack Time", teacherName = "Guru Piket", roomName = "Selasar Kelas 2-B", requiredItems = "Bekal Buah Segar & Susu", colorHex = "#F97316"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Rabu", periodNumber = 4, timeRange = "10.00 - 11.15 WIB", subject = "Pendidikan Lingkungan Hidup (PLH)", teacherName = "Bu Sarah, S.Pd", roomName = "Taman Hidroponik Sekolah", requiredItems = "Sapu Tangan / Celemek Kebun Kecil", colorHex = "#84CC16"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Rabu", periodNumber = 5, timeRange = "11.15 - 12.00 WIB", subject = "Bimbingan Karakter & Sholat Dhuhur", teacherName = "Ustadz Fauzi & Bu Sarah", roomName = "Musholla Al-Ikhlas", requiredItems = "Peralatan Sholat Pribadi (Peci/Mukena)", colorHex = "#059669"))

      // Kamis
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Kamis", periodNumber = 1, timeRange = "07.00 - 08.30 WIB", subject = "PJOK: Gerak Dasar & Permainan Bola", teacherName = "Pak Dedi, S.Or", roomName = "Lapangan Olahraga", requiredItems = "Baju Olahraga Sekolah, Sepatu Kets, Handuk", colorHex = "#F97316"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Kamis", periodNumber = 2, timeRange = "08.30 - 09.00 WIB", subject = "Pendinginan, Cuci Tangan & Ganti Baju", teacherName = "Pak Dedi & Bu Sarah", roomName = "Ruang Ganti Siswa", requiredItems = "Baju Kemeja Ganti Bersih, Minyak Kayu Putih", colorHex = "#06B6D4"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Kamis", periodNumber = 3, timeRange = "09.00 - 09.30 WIB", subject = "Istirahat & Sarapan Bersama", teacherName = "Guru Piket", roomName = "Kantin Sehat Sekolah", requiredItems = "Bekal Nasi Sehat & Air Mineral", colorHex = "#10B981"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Kamis", periodNumber = 4, timeRange = "09.30 - 10.45 WIB", subject = "Matematika: Satuan Waktu & Jam", teacherName = "Pak Bambang, M.Pd", roomName = "Kelas 2-B", requiredItems = "Model Jam Kertas Buatan Sendiri, Buku Tulis", colorHex = "#F59E0B"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Kamis", periodNumber = 5, timeRange = "10.45 - 11.45 WIB", subject = "Bahasa Daerah / Cerita Rakyat", teacherName = "Bu Ningsih, S.Pd", roomName = "Kelas 2-B", requiredItems = "Buku Paket Bahasa Daerah Nusantara", colorHex = "#8B5CF6"))

      // Jumat
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Jumat", periodNumber = 1, timeRange = "07.00 - 07.45 WIB", subject = "Senam Irama & Operasi Semut Bersih", teacherName = "Seluruh Guru & Pembina", roomName = "Lapangan Sekolah Ceria", requiredItems = "Seragam Pramuka / Kaos Olahraga Ceria", colorHex = "#EC4899"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Jumat", periodNumber = 2, timeRange = "07.45 - 09.00 WIB", subject = "Tematik: Pengalaman Menyenangkan", teacherName = "Bu Sarah, S.Pd", roomName = "Kelas 2-B", requiredItems = "Buku Tematik 2D, Lem Kertas, Foto Kenangan", colorHex = "#3B82F6"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Jumat", periodNumber = 3, timeRange = "09.00 - 09.30 WIB", subject = "Istirahat & Jumat Berkah Berbagi", teacherName = "Guru Piket", roomName = "Taman Sekolah", requiredItems = "Snack Sehat untuk Dinikmati Bersama", colorHex = "#10B981"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Jumat", periodNumber = 4, timeRange = "09.30 - 10.45 WIB", subject = "Kepramukaan Siaga: Latihan Baris & Sandi", teacherName = "Kak Rangga & Bu Sarah", roomName = "Selasar Utama Sekolah", requiredItems = "Buku SKU Siaga, Peluit Bertali, Kacu Rapi", colorHex = "#92400E"))
      dao.insertTimetableLesson(TimetableLesson(dayOfWeek = "Jumat", periodNumber = 5, timeRange = "10.45 - 11.00 WIB", subject = "Refleksi Pekanan & Pulang Ceria", teacherName = "Bu Sarah, S.Pd", roomName = "Kelas 2-B", requiredItems = "Semua Buku dan Alat Tulis Masuk Tas", colorHex = "#6366F1"))
    }

    // Attempt cloud sync if configured
    if (supabaseManager.isConfigured) {
      syncWithSupabase()
    }
  }

  // Admin & School Management Operations
  suspend fun updateSchoolProfile(profile: SchoolProfile) {
    dao.insertOrUpdateSchoolProfile(profile)
  }

  suspend fun registerNewSchool(
    schoolName: String,
    npsn: String,
    level: String,
    address: String,
    city: String,
    phone: String,
    email: String,
    principalName: String,
    applicantName: String,
    applicantNik: String,
    applicantPhone: String,
    applicantRole: String,
    applicantAddress: String,
    assignmentLetterFileName: String,
    adminUsername: String,
    adminPassword: String
  ): Long {
    val newProfile = SchoolProfile(
      schoolName = schoolName,
      npsn = if (npsn.isBlank()) "20104829" else npsn,
      level = level,
      address = address,
      city = city,
      phone = phone,
      email = email,
      principalName = principalName,
      applicantName = applicantName,
      applicantNik = applicantNik,
      applicantPhone = applicantPhone,
      applicantRole = applicantRole,
      applicantAddress = applicantAddress,
      assignmentLetterFileName = assignmentLetterFileName,
      adminUsername = adminUsername,
      adminPassword = adminPassword,
      isVerified = false,
      status = "PENDING",
      registeredAt = "28 Sep 2026",
      totalClassrooms = 0,
      totalTeachers = 0,
      totalStudents = 0
    )
    return dao.insertOrUpdateSchoolProfile(newProfile)
  }

  // Superadmin Operations
  suspend fun approveSchoolRegistration(id: Long) {
    dao.updateSchoolStatus(id = id, status = "VERIFIED", isVerified = true, reason = "")
  }

  suspend fun rejectSchoolRegistration(id: Long, reason: String) {
    dao.updateSchoolStatus(id = id, status = "REJECTED", isVerified = false, reason = reason)
  }

  suspend fun toggleSchoolSuspension(id: Long, currentStatus: String) {
    val newStatus = if (currentStatus == "SUSPENDED") "VERIFIED" else "SUSPENDED"
    val isVerified = newStatus == "VERIFIED"
    dao.updateSchoolStatus(id = id, status = newStatus, isVerified = isVerified, reason = "")
  }

  suspend fun deleteSchoolProfile(id: Long) {
    dao.deleteSchoolProfile(id)
  }

  suspend fun addClassroom(name: String, gradeLevel: String, academicYear: String, teacherName: String, maxCap: Int): Long {
    val classroom = ClassroomRoom(
      name = name,
      gradeLevel = gradeLevel,
      academicYear = academicYear,
      homeroomTeacherName = teacherName,
      studentCount = 0,
      maxCapacity = maxCap
    )
    return dao.insertClassroom(classroom)
  }

  suspend fun deleteClassroom(id: Long) {
    dao.deleteClassroom(id)
  }

  suspend fun addTeacher(fullName: String, nip: String, phone: String, assignedClass: String, username: String, pass: String): Long {
    val teacher = TeacherAccount(
      fullName = fullName,
      nip = nip,
      phone = phone,
      assignedClass = assignedClass,
      username = username,
      password = pass
    )
    return dao.insertTeacher(teacher)
  }

  suspend fun updateTeacherAssignedClass(id: Long, assignedClass: String) {
    dao.updateTeacherAssignedClass(id, assignedClass)
  }

  suspend fun deleteTeacher(id: Long) {
    dao.deleteTeacher(id)
  }

  suspend fun addParentStudentAccount(
    studentName: String,
    nisn: String,
    studentClass: String,
    parentName: String,
    parentPhone: String,
    username: String,
    pass: String
  ): Long {
    val account = ParentStudentAccount(
      studentName = studentName,
      nisn = nisn,
      studentClass = studentClass,
      parentName = parentName,
      parentPhone = parentPhone,
      username = username,
      password = pass
    )
    return dao.insertParentStudentAccount(account)
  }

  suspend fun deleteParentStudentAccount(id: Long) {
    dao.deleteParentStudentAccount(id)
  }

  // Announcement Actions
  suspend fun addAnnouncement(
    title: String,
    letterNumber: String,
    category: String,
    targetAudience: String,
    content: String,
    author: String,
    date: String,
    isPinned: Boolean,
    attachmentTitle: String
  ): Long {
    val ann = SchoolAnnouncement(
      title = title,
      letterNumber = letterNumber,
      category = category,
      targetAudience = targetAudience,
      content = content,
      author = author,
      date = date,
      isPinned = isPinned,
      isReadByParent = false,
      readCount = 1,
      totalRecipients = 28,
      attachmentTitle = attachmentTitle
    )
    return dao.insertAnnouncement(ann)
  }

  suspend fun markAnnouncementAsRead(id: Long, isRead: Boolean) {
    dao.markAnnouncementAsRead(id, isRead)
  }

  suspend fun deleteAnnouncement(id: Long) {
    dao.deleteAnnouncement(id)
  }

  // Calendar Event Actions
  suspend fun addAcademicCalendarEvent(
    title: String,
    date: String,
    timeRange: String,
    targetClass: String,
    category: String,
    location: String,
    description: String,
    requiredItems: String,
    createdBy: String
  ): Long {
    val event = AcademicCalendarEvent(
      title = title,
      date = date,
      timeRange = timeRange,
      targetClass = targetClass,
      category = category,
      location = location,
      description = description,
      requiredItems = requiredItems,
      checkedItems = "",
      rsvpStatus = "BELUM_KONFIRMASI",
      createdBy = createdBy
    )
    return dao.insertAcademicCalendarEvent(event)
  }

  suspend fun updateEventRsvp(id: Long, rsvpStatus: String) {
    dao.updateEventRsvp(id, rsvpStatus)
  }

  suspend fun toggleEventCheckedItem(event: AcademicCalendarEvent, item: String) {
    val currentList = event.checkedItems.split(",")
      .map { it.trim() }
      .filter { it.isNotBlank() }
      .toMutableList()

    if (currentList.contains(item)) {
      currentList.remove(item)
    } else {
      currentList.add(item)
    }

    val updatedStr = currentList.joinToString(", ")
    dao.updateEventCheckedItems(event.id, updatedStr)
  }

  suspend fun deleteAcademicCalendarEvent(id: Long) {
    dao.deleteAcademicCalendarEvent(id)
  }

  // Dismissal broadcast
  suspend fun broadcastDismissal(isDismissed: Boolean, dismissalTime: String, message: String) {
    val alert = DismissalAlert(
      gradeClass = "Kelas 2-B",
      isDismissed = isDismissed,
      dismissalTime = dismissalTime,
      title = if (isDismissed) "Bel Pulang Berbunyi!" else "KBM Masih Berlangsung",
      message = message,
      announcedBy = "Bu Sarah, S.Pd (Wali Kelas)"
    )
    dao.insertDismissalAlert(alert)

    withContext(Dispatchers.IO) {
      try {
        val api = supabaseManager.getApi()
        api?.insertDismissal(
          SupabaseDismissalAlert(
            gradeClass = alert.gradeClass,
            isDismissed = alert.isDismissed,
            dismissalTime = alert.dismissalTime,
            title = alert.title,
            message = alert.message,
            announcedBy = alert.announcedBy,
            timestamp = alert.timestamp
          )
        )
      } catch (e: Exception) {
        Log.e("SupabaseSync", "Gagal sync dismissal ke Supabase: ${e.message}")
      }
    }
  }

  // Pickup trigger
  suspend fun notifyParentArrival(gate: String) {
    val queue = PickupQueue(
      studentName = "Rafa Al-Ghifari",
      parentName = "Bunda Dina",
      gateLocation = gate,
      status = "MENUNGGU"
    )
    val localId = dao.insertPickupQueue(queue)

    withContext(Dispatchers.IO) {
      try {
        val api = supabaseManager.getApi()
        api?.insertPickupQueue(
          SupabasePickupQueue(
            id = localId,
            studentName = queue.studentName,
            parentName = queue.parentName,
            gateLocation = queue.gateLocation,
            status = queue.status,
            timestamp = queue.timestamp
          )
        )
      } catch (e: Exception) {
        Log.e("SupabaseSync", "Gagal sync pickup ke Supabase: ${e.message}")
      }
    }
  }

  suspend fun updatePickupStatus(id: Long, status: String) {
    dao.updatePickupStatus(id, status)

    withContext(Dispatchers.IO) {
      try {
        val api = supabaseManager.getApi()
        api?.updatePickupStatus("eq.$id", mapOf("status" to status))
      } catch (e: Exception) {
        Log.e("SupabaseSync", "Gagal sync update pickup ke Supabase: ${e.message}")
      }
    }
  }

  // Add Savings
  suspend fun addSavings(amount: Long, type: String, note: String, date: String) {
    val tx = SavingTransaction(
      studentId = 1L,
      amount = amount,
      type = type,
      note = note,
      formattedDate = date
    )
    dao.insertSavingTransaction(tx)

    withContext(Dispatchers.IO) {
      try {
        val api = supabaseManager.getApi()
        api?.insertSavingTransaction(
          SupabaseSavingTransaction(
            studentId = tx.studentId,
            amount = tx.amount,
            type = tx.type,
            note = tx.note,
            timestamp = tx.timestamp,
            formattedDate = tx.formattedDate
          )
        )
      } catch (e: Exception) {
        Log.e("SupabaseSync", "Gagal sync savings ke Supabase: ${e.message}")
      }
    }
  }

  // Add Academic Report
  suspend fun addAcademicReport(subject: String, score: Int, category: String, feedback: String, badge: String, date: String) {
    val report = AcademicReport(
      studentId = 1L,
      subject = subject,
      score = score,
      category = category,
      teacherFeedback = feedback,
      badge = badge,
      date = date
    )
    dao.insertAcademicReport(report)

    withContext(Dispatchers.IO) {
      try {
        val api = supabaseManager.getApi()
        api?.insertAcademicReport(
          SupabaseAcademicReport(
            studentId = report.studentId,
            subject = report.subject,
            score = report.score,
            category = report.category,
            teacherFeedback = report.teacherFeedback,
            badge = report.badge,
            date = report.date
          )
        )
      } catch (e: Exception) {
        Log.e("SupabaseSync", "Gagal sync academic report ke Supabase: ${e.message}")
      }
    }
  }

  // Submit Attendance Permission
  suspend fun submitAttendancePermission(status: String, note: String, date: String) {
    val record = AttendanceRecord(
      studentId = 1L,
      studentName = "Rafa Al-Ghifari",
      date = date,
      status = status,
      time = "--:-- WIB",
      note = note
    )
    dao.insertAttendanceRecord(record)

    withContext(Dispatchers.IO) {
      try {
        val api = supabaseManager.getApi()
        api?.insertAttendanceRecord(
          SupabaseAttendanceRecord(
            studentId = record.studentId,
            studentName = record.studentName,
            date = record.date,
            status = record.status,
            time = record.time,
            note = record.note
          )
        )
      } catch (e: Exception) {
        Log.e("SupabaseSync", "Gagal sync attendance ke Supabase: ${e.message}")
      }
    }
  }

  // Like Activity
  suspend fun likeActivity(id: Long) {
    dao.likeActivity(id)
  }

  // Add School Activity
  suspend fun addActivity(title: String, category: String, description: String, date: String, photoType: String) {
    val activity = SchoolActivity(
      title = title,
      category = category,
      description = description,
      date = date,
      likes = 1,
      photoType = photoType
    )
    dao.insertSchoolActivity(activity)

    withContext(Dispatchers.IO) {
      try {
        val api = supabaseManager.getApi()
        api?.insertSchoolActivity(
          SupabaseSchoolActivity(
            title = activity.title,
            category = activity.category,
            date = activity.date,
            description = activity.description,
            likes = activity.likes,
            photoType = activity.photoType
          )
        )
      } catch (e: Exception) {
        Log.e("SupabaseSync", "Gagal sync activity ke Supabase: ${e.message}")
      }
    }
  }

  // Send Message
  suspend fun sendMessage(role: String, name: String, text: String, time: String) {
    val msg = ChatMessage(
      senderRole = role,
      senderName = name,
      text = text,
      time = time
    )
    dao.insertChatMessage(msg)

    withContext(Dispatchers.IO) {
      try {
        val api = supabaseManager.getApi()
        api?.insertChatMessage(
          SupabaseChatMessage(
            senderRole = msg.senderRole,
            senderName = msg.senderName,
            text = msg.text,
            time = msg.time,
            timestamp = msg.timestamp
          )
        )
      } catch (e: Exception) {
        Log.e("SupabaseSync", "Gagal sync chat ke Supabase: ${e.message}")
      }
    }
  }

  // Comprehensive Cloud Sync from Supabase into Local Room Cache
  suspend fun syncWithSupabase(): Result<String> = withContext(Dispatchers.IO) {
    val api = supabaseManager.getApi() ?: return@withContext Result.failure(Exception("Supabase belum dikonfigurasi"))
    try {
      // 1. Fetch Students
      val studentsResp = api.getStudents()
      if (studentsResp.isSuccessful) {
        studentsResp.body()?.forEach { s ->
          dao.insertStudent(
            Student(
              id = s.id ?: 0,
              name = s.name,
              gradeClass = s.gradeClass,
              studentNumber = s.studentNumber,
              parentName = s.parentName,
              avatarColorHex = s.avatarColorHex,
              targetSavings = s.targetSavings
            )
          )
        }
      }

      // 2. Fetch Dismissal
      val dismissalResp = api.getLatestDismissal()
      if (dismissalResp.isSuccessful) {
        dismissalResp.body()?.firstOrNull()?.let { d ->
          dao.insertDismissalAlert(
            DismissalAlert(
              id = d.id ?: 0,
              gradeClass = d.gradeClass,
              isDismissed = d.isDismissed,
              dismissalTime = d.dismissalTime,
              title = d.title,
              message = d.message,
              announcedBy = d.announcedBy,
              timestamp = d.timestamp
            )
          )
        }
      }

      // 3. Fetch Pickup Queues
      val queuesResp = api.getPickupQueues()
      if (queuesResp.isSuccessful) {
        queuesResp.body()?.forEach { q ->
          dao.insertPickupQueue(
            PickupQueue(
              id = q.id ?: 0,
              studentName = q.studentName,
              parentName = q.parentName,
              gateLocation = q.gateLocation,
              status = q.status,
              timestamp = q.timestamp
            )
          )
        }
      }

      // 4. Fetch Savings
      val savingsResp = api.getSavingTransactions()
      if (savingsResp.isSuccessful) {
        savingsResp.body()?.forEach { tx ->
          dao.insertSavingTransaction(
            SavingTransaction(
              id = tx.id ?: 0,
              studentId = tx.studentId,
              amount = tx.amount,
              type = tx.type,
              note = tx.note,
              timestamp = tx.timestamp,
              formattedDate = tx.formattedDate
            )
          )
        }
      }

      // 5. Fetch Chat
      val chatResp = api.getChatMessages()
      if (chatResp.isSuccessful) {
        chatResp.body()?.forEach { c ->
          dao.insertChatMessage(
            ChatMessage(
              id = c.id ?: 0,
              senderRole = c.senderRole,
              senderName = c.senderName,
              text = c.text,
              time = c.time,
              timestamp = c.timestamp
            )
          )
        }
      }

      Result.success("Sinkronisasi Supabase Cloud Berhasil! Data lokal diperbarui.")
    } catch (e: Exception) {
      Result.failure(Exception("Sinkronisasi cloud gagal: ${e.localizedMessage}"))
    }
  }

  // Timetable Operations
  suspend fun insertTimetableLesson(
    dayOfWeek: String,
    periodNumber: Int,
    timeRange: String,
    subject: String,
    teacherName: String,
    roomName: String,
    requiredItems: String,
    colorHex: String
  ) {
    val lesson = TimetableLesson(
      dayOfWeek = dayOfWeek,
      periodNumber = periodNumber,
      timeRange = timeRange,
      subject = subject,
      teacherName = teacherName,
      roomName = roomName,
      requiredItems = requiredItems,
      colorHex = colorHex
    )
    dao.insertTimetableLesson(lesson)
  }

  suspend fun toggleLessonPackStatus(id: Long, isCompleted: Boolean) {
    dao.updateLessonPackStatus(id, isCompleted)
  }

  suspend fun deleteTimetableLesson(id: Long) {
    dao.deleteTimetableLesson(id)
  }

  // Daily Uniform Operations
  suspend fun updateDailyUniform(
    id: Long,
    title: String,
    description: String,
    accessories: String,
    shoesColor: String
  ) {
    dao.updateDailyUniform(id, title, description, accessories, shoesColor)
  }
}
