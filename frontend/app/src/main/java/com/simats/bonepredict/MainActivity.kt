package com.example.bonepredict

import android.os.Bundle
import android.net.Uri
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import android.graphics.Canvas
import android.content.ContentValues
import android.provider.MediaStore
import android.os.Environment
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bonepredict.R
import com.example.bonepredict.ui.theme.BonePredictTheme
import com.example.bonepredict.ui.theme.SplashBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bonepredict.data.local.BonePredictDatabase
import com.example.bonepredict.data.remote.FirebaseService
import com.example.bonepredict.data.repository.BoneRepository
import com.example.bonepredict.ui.MainViewModel
import com.example.bonepredict.ui.MainViewModelFactory
import coil.compose.rememberAsyncImagePainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = BonePredictDatabase.getDatabase(this)
        val repository = BoneRepository(
            database.patientDao(),
            database.clinicalDataDao(),
            database.predictionDao(),
            FirebaseService()
        )
        val viewModelFactory = MainViewModelFactory(repository)

        setContent {
            BonePredictTheme {
                val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") {
                            SplashScreen(navController)
                        }
                        composable("welcome") {
                            WelcomeScreen(navController)
                        }
                        composable("select_role") {
                            SelectRoleScreen(navController)
                        }
                        composable("signin") {
                            SignInScreen(navController, viewModel)
                        }
                        composable("signup") {
                            SignUpScreen(navController, viewModel)
                        }
                        composable("forgot_password") {
                            ForgotPasswordScreen(navController, viewModel)
                        }
                        composable("otp_verification/{email}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            OtpVerificationScreen(navController, viewModel, email)
                        }
                        composable("reset_password/{email}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""
                            ResetPasswordScreen(navController, viewModel, email)
                        }
                        composable("dashboard") {
                            DashboardScreen(navController, viewModel)
                        }
                        composable("profile") {
                            ProfileScreen(navController, viewModel)
                        }
                        composable("edit_profile") {
                            EditProfileScreen(navController, viewModel)
                        }
                        composable("add_patient") {
                            AddPatientScreen(navController, viewModel)
                        }
                        composable("patients") {
                            PatientsScreen(navController, viewModel)
                        }
                        composable("patient_history/{patientId}") { backStackEntry ->
                            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
                            PatientHistoryScreen(navController, viewModel, patientId)
                        }
                        composable("demographics") {
                            DemographicsScreen(navController, viewModel)
                        }
                        composable("peridontal_status") {
                            PeridontalStatusScreen(navController, viewModel)
                        }
                        composable("cbct") {
                            CBCTScreen(navController, viewModel)
                        }
                        composable("upload_success") {
                            UploadSuccessScreen(navController, viewModel)
                        }
                        composable("data_validation") {
                            DataValidationScreen(navController)
                        }
                        composable("feature_selection") {
                            FeatureSelectionScreen(navController)
                        }
                        composable("demographic_features") {
                            DemographicFeaturesScreen(navController)
                        }
                        composable("preprocessing") {
                            PreprocessingScreen(navController)
                        }
                        composable("select_ml") {
                            SelectMLScreen(navController)
                        }
                        composable("random_forest") {
                            RandomForestScreen(navController)
                        }
                        composable("training_model") {
                            TrainingModelScreen(navController)
                        }
                        composable("prediction_ready") {
                            PredictionReadyScreen(navController)
                        }
                        composable("risk_assessment") {
                            RiskAssessmentScreen(navController, viewModel)
                        }
                        composable("risk_explanation") {
                            RiskExplanationScreen(navController, viewModel)
                        }
                        composable("bone_loss_visual") {
                            BoneLossVisualScreen(navController)
                        }
                        composable("confidence_analysis") {
                            ConfidenceAnalysisScreen(navController)
                        }
                        composable("result_summary") {
                            ResultSummaryScreen(navController, viewModel)
                        }
                        composable("recommendations") {
                            RecommendationsScreen(navController)
                        }
                        composable("preventive_measures") {
                            PreventiveMeasuresScreen(navController)
                        }
                        composable("treatment_planning") {
                            TreatmentPlanningScreen(navController)
                        }
                        composable("generate_report") {
                            GenerateReportScreen(navController)
                        }
                        composable("report_preview") {
                            ReportPreviewScreen(navController, viewModel)
                        }
                        composable("export_report") {
                            ExportReportScreen(navController)
                        }
                        composable("settings") {
                            SettingsScreen(navController, viewModel)
                        }
                        composable("help_faq") {
                            HelpFaqScreen(navController)
                        }
                        composable("about") {
                            AboutScreen(navController)
                        }
                        composable("trend_analysis") {
                            TrendAnalysisScreen(navController)
                        }
                        composable("detailed_graphs") {
                            DetailedGraphsScreen(navController)
                        }
                        composable("detailed_metrics") {
                            DetailedMetricsScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

fun exportReportToDownloads(
    context: android.content.Context,
    prediction: com.example.bonepredict.data.model.Prediction?,
    patient: com.example.bonepredict.data.model.Patient?
) {
    if (prediction == null || patient == null) {
        Toast.makeText(context, "Error: No data to export", Toast.LENGTH_SHORT).show()
        return
    }

    val pdfDocument = PdfDocument()
    val paint = Paint()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // Header
    paint.setARGB(255, 74, 144, 226)
    paint.textSize = 24f
    paint.isFakeBoldText = true
    canvas.drawText("BonePredict AI - Clinical Report", 50f, 60f, paint)

    paint.setARGB(255, 148, 163, 184)
    paint.textSize = 12f
    paint.isFakeBoldText = false
    val date = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date())
    canvas.drawText("Generated on: $date", 50f, 85f, paint)

    // Divider
    paint.setARGB(255, 226, 232, 240)
    canvas.drawLine(50f, 100f, 545f, 100f, paint)

    // Patient Info
    paint.setARGB(255, 30, 41, 59)
    paint.textSize = 16f
    paint.isFakeBoldText = true
    canvas.drawText("Patient Information", 50f, 130f, paint)

    paint.textSize = 12f
    paint.isFakeBoldText = false
    canvas.drawText("Name: ${patient.firstName} ${patient.lastName}", 50f, 155f, paint)
    canvas.drawText("Patient ID: ${patient.id}", 50f, 175f, paint)
    canvas.drawText("Date of Birth: ${patient.dob}", 50f, 195f, paint)
    canvas.drawText("Gender: ${patient.gender}", 50f, 215f, paint)

    // Assessment Results
    paint.setARGB(255, 30, 41, 59)
    paint.textSize = 16f
    paint.isFakeBoldText = true
    canvas.drawText("Risk Assessment Results", 50f, 260f, paint)

    paint.textSize = 14f
    paint.isFakeBoldText = true
    val riskColor = if (prediction.riskCategory.contains("High", true)) {
        android.graphics.Color.RED
    } else if (prediction.riskCategory.contains("Moderate", true)) {
        android.graphics.Color.rgb(255, 160, 0) // Amber
    } else {
        android.graphics.Color.rgb(0, 191, 165) // Teal
    }
    paint.color = riskColor
    canvas.drawText("CATEGORY: ${prediction.riskCategory.uppercase()}", 50f, 290f, paint)
    
    paint.setARGB(255, 30, 41, 59)
    canvas.drawText("PROBABILITY: ${prediction.riskScore.toInt()}%", 50f, 315f, paint)

    // Summary
    paint.textSize = 12f
    paint.isFakeBoldText = false
    paint.setARGB(255, 100, 116, 139)
    canvas.drawText("Findings:", 50f, 350f, paint)
    
    val summaryLines = prediction.resultsSummary.chunked(70)
    var yPos = 370f
    summaryLines.forEach { line ->
        canvas.drawText(line, 60f, yPos, paint)
        yPos += 20f
    }

    // Recommendations
    yPos += 20f
    paint.setARGB(255, 30, 41, 59)
    paint.textSize = 16f
    paint.isFakeBoldText = true
    canvas.drawText("Clinical Recommendations", 50f, yPos, paint)
    
    paint.textSize = 12f
    paint.isFakeBoldText = false
    paint.setARGB(255, 100, 116, 139)
    yPos += 30f
    canvas.drawText("• Immediate ridge preservation protocol recommended.", 60f, yPos, paint)
    yPos += 20f
    canvas.drawText("• Follow-up period: 12 weeks post-procedure.", 60f, yPos, paint)
    yPos += 20f
    canvas.drawText("• Monitor trabecular density changes in adjacent teeth.", 60f, yPos, paint)

    // Footer
    paint.setARGB(255, 148, 163, 184)
    paint.textSize = 10f
    canvas.drawText("This report is generated by BonePredict AI assistance. Please confirm findings clinically.", 50f, 800f, paint)

    pdfDocument.finishPage(page)

    // Save to Downloads
    val fileName = "BonePredict_Report_${patient.id}_${System.currentTimeMillis()}.pdf"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

    if (uri != null) {
        try {
            resolver.openOutputStream(uri)?.use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }
            Toast.makeText(context, "Report exported to Downloads", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to export: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    } else {
        Toast.makeText(context, "Failed to create file", Toast.LENGTH_SHORT).show()
        pdfDocument.close()
    }
}

@Composable
fun SplashScreen(navController: NavController? = null) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController?.navigate("welcome") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SplashBlue)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Container
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_splash),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Name
            Text(
                text = "AlveoPredict AI",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = "Predictive Modeling of Alveolar\nRidge Resorption",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                ),
                modifier = Modifier.padding(horizontal = 48.dp)
            )

            Spacer(modifier = Modifier.height(100.dp))

            // Progress Indicators (Dots)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(if (index == 0) Color.White else Color.White.copy(alpha = 0.5f))
                    )
                }
            }
        }

        // Bottom Bar Indicator
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .width(120.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White.copy(alpha = 0.5f))
        )
    }
}

@Composable
fun WelcomeScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Large Graphic
        Image(
            painter = painterResource(id = R.drawable.welcome_graphic),
            contentDescription = "Welcome Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentScale = ContentScale.Fit
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            Text(
                text = "Welcome to AlveoPredict",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Advanced AI-powered tool for predicting alveolar ridge resorption risk and visualizing bone loss progression.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF74777F),
                    lineHeight = 24.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Get Started Button
            Button(
                onClick = { navController?.navigate("signin") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2)
                )
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "By continuing, you agree to our Terms & Privacy Policy",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = Color(0xFFC4C7C5)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SelectRoleScreen(navController: NavController? = null) {
    var selectedRole by remember { mutableStateOf("Doctor") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
    ) {
        // Header with Back Button and Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1A1C1E),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Select Role",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Who are you?",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "We'll customize the experience based on your role.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF74777F),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Role Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                tonalElevation = 2.dp,
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE8F1FF)), // Light blue for icon bg
                        contentAlignment = Alignment.Center
                    ) {
                        // Using a simple medical icon representation
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_search), // Fallback or placeholder icon
                            contentDescription = null,
                            tint = Color(0xFF4A90E2),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Doctor",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1C1E)
                            )
                        )
                        Text(
                            text = "Clinical Practice",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF74777F)
                            )
                        )
                    }

                    RadioButton(
                        selected = selectedRole == "Doctor",
                        onClick = { selectedRole = "Doctor" },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF4A90E2)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Continue Button
            Button(
                onClick = { navController?.navigate("dashboard") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF90C2F3) // Lighter blue as per image
                )
            ) {
                Text(
                    text = "Continue to Dashboard",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SignInScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    var passwordTouched by remember { mutableStateOf(false) }

    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = """^[A-Z](?=.*[0-9])(?=.*[^a-zA-Z0-9]).{7,}$""".toRegex().matches(password)
    
    val isButtonEnabled = email.isNotBlank() && isEmailValid && password.isNotBlank() && isPasswordValid && !isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        IconButton(
            onClick = { navController?.popBackStack() },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Color(0xFF1A1C1E),
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Sign In",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Enter your credentials to access your dashboard.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF74777F)
            )
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Error Message
        viewModel?.authError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = "Email Address",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1C1E)
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            shadowElevation = 0.dp
        ) {
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Enter email", color = Color(0xFFC4C7C5)) },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) emailTouched = true },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = emailTouched && email.isNotEmpty() && !isEmailValid
            )
        }
        if (emailTouched && email.isNotEmpty() && !isEmailValid) {
            Text(
                text = "Invalid email",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Password",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1C1E)
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Enter password", color = Color(0xFFC4C7C5)) },
                modifier = Modifier.fillMaxWidth().onFocusChanged { if (!it.isFocused) passwordTouched = true },
                visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility")
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = passwordTouched && password.isNotEmpty() && !isPasswordValid
            )
        }
        if (passwordTouched && password.isNotEmpty() && !isPasswordValid) {
            Text(
                text = "Invalid password",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = "Requirements: Capital start, Special char, Number, Min 8 chars",
            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF9E9E9E)),
            modifier = Modifier.padding(top = 4.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(
                onClick = { navController?.navigate("forgot_password") },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Forgot Password?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A90E2)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { 
                isLoading = true
                viewModel?.login(email, password, onComplete = { isLoading = false }) {
                    navController?.navigate("select_role") 
                }
            },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2),
                disabledContainerColor = Color(0xFFE0E0E0)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (isButtonEnabled) Color.White else Color(0xFF9E9E9E)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (isButtonEnabled) Color.White else Color(0xFF9E9E9E)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF74777F))
            )
            TextButton(onClick = { navController?.navigate("signup") }) {
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A90E2)
                    )
                )
            }
        }
    }
}

@Composable
fun SignUpScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = """^[A-Z](?=.*[0-9])(?=.*[^a-zA-Z0-9]).{7,}$""".toRegex().matches(password)

    // Validation logic for Sign Up
    val isButtonEnabled = fullName.isNotBlank() && 
                         !fullName.any { it.isDigit() } &&
                         email.isNotBlank() && 
                         isEmailValid &&
                         password.isNotBlank() && 
                         isPasswordValid &&
                         confirmPassword.isNotBlank() &&
                         !isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Back Button
        IconButton(
            onClick = { navController?.popBackStack() },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = Color(0xFF1A1C1E),
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1C1E)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Join AlveoPredict to start analyzing patient data.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF74777F),
                lineHeight = 24.sp
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Error Message
        viewModel?.authError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Full Name
        Text(text = "Full Name", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1C1E)))
        Surface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), shape = RoundedCornerShape(12.dp), color = Color.White) {
            TextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = { Text("Enter Full Name", color = Color(0xFFC4C7C5)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                singleLine = true,
                isError = fullName.any { it.isDigit() }
            )
        }
        if (fullName.any { it.isDigit() }) {
            Text(
                text = "Invalid name",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Email Address
        Text(text = "Email Address", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1C1E)))
        Surface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), shape = RoundedCornerShape(12.dp), color = Color.White) {
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Enter Email Address", color = Color(0xFFC4C7C5)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = email.isNotEmpty() && !isEmailValid
            )
        }
        if (email.isNotEmpty() && !isEmailValid) {
            Text(
                text = "Invalid email",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        Text(text = "Password", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1C1E)))
        Text(
            text = "Min 8 chars, start with Capital, 1 special char, 1 number",
            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F)),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Surface(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), shape = RoundedCornerShape(12.dp), color = Color.White) {
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Create a password", color = Color(0xFFC4C7C5)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility")
                    }
                },
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = password.isNotEmpty() && !isPasswordValid
            )
        }
        if (password.isNotEmpty() && !isPasswordValid) {
            Text(
                text = "Invalid password",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password
        Text(text = "Confirm Password", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1C1E)))
        Surface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), shape = RoundedCornerShape(12.dp), color = Color.White) {
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Confirm password", color = Color(0xFFC4C7C5)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (confirmPasswordVisible) androidx.compose.ui.text.input.VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility")
                    }
                },
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
        }

        // Validate password equality
        if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
            Text(
                text = "Passwords do not match",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Create Account Button
        Button(
            onClick = { 
                if (password == confirmPassword) {
                    isLoading = true
                    viewModel?.register(fullName, email, password, onComplete = { isLoading = false }) {
                        navController?.navigate("signin") 
                    }
                }
            },
            enabled = isButtonEnabled && (password == confirmPassword),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2),
                disabledContainerColor = Color(0xFFE0E0E0)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = if (isButtonEnabled && password == confirmPassword) Color.White else Color(0xFF9E9E9E)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Create Account",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (isButtonEnabled && password == confirmPassword) Color.White else Color(0xFF9E9E9E)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Already have an account? ", style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF74777F)))
            TextButton(onClick = { navController?.navigate("signin") }) {
                Text(text = "Sign In", color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    val userName = viewModel?.currentUser ?: "Dr. John Doe"
    val initials = userName.split(" ").filter { it.isNotEmpty() }.map { it[0] }.take(2).joinToString("").uppercase()
    
    var isLoadingReport by remember { mutableStateOf(false) }
    
    LaunchedEffect(viewModel?.currentUserEmail) {
        viewModel?.fetchPatients()
    }
    
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.height(80.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavItem(icon = Icons.Default.Home, label = "Home", active = true)
                    BottomNavItem(icon = Icons.Default.Group, label = "Patients", onClick = { navController?.navigate("patients") })
                    BottomNavItem(icon = Icons.Default.ShowChart, label = "Analysis", onClick = { navController?.navigate("trend_analysis") })
                    BottomNavItem(icon = Icons.Default.Settings, label = "Settings", onClick = { navController?.navigate("settings") })
                }
            }
        },
        containerColor = Color(0xFFF8F9FB)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Custom Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .statusBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AlveoPredict AI",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C1E)
                        )
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(Color(0xFFE8F1FF))
                            .clickable { navController?.navigate("profile") },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials.ifEmpty { "JD" },
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color(0xFF4A90E2),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Hello, $userName",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C1E)
                        )
                    )
                    Text(
                        text = "Here's your practice overview today.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF74777F))
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Stats Row
                    val patients by viewModel?.patients?.collectAsState() ?: remember { mutableStateOf(emptyList<com.example.bonepredict.data.model.Patient>()) }
                    
                    val totalPatients = patients.size
                    // All patients have predictions (blank/Pending => High Risk in patients screen)
                    val predictions = totalPatients
                    val pending = patients.count { 
                        it.riskStatus.isNotBlank() && 
                        !it.riskStatus.equals("Pending", ignoreCase = true) && 
                        !it.riskStatus.contains("High", ignoreCase = true) &&
                        !it.riskStatus.contains("Low", ignoreCase = true) &&
                        !it.riskStatus.contains("Moderate", ignoreCase = true)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(label = "Patients", value = "$totalPatients", color = Color(0xFF4A90E2), modifier = Modifier.weight(1f))
                        StatCard(label = "Predictions", value = "$predictions", color = Color(0xFF00BFA5), modifier = Modifier.weight(1f))
                        StatCard(label = "Pending", value = "$pending", color = Color(0xFFFFA000), modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C1E)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Actions Grid
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            QuickActionCard(
                                title = "New\nPrediction",
                                subtitle = "Start analysis",
                                icon = Icons.Default.AddCircleOutline,
                                iconBg = Color(0xFFE8F1FF),
                                iconTint = Color(0xFF4A90E2),
                                modifier = Modifier.weight(1f),
                                onClick = { navController?.navigate("add_patient") }
                            )
                            QuickActionCard(
                                title = "Patients",
                                subtitle = "Manage records",
                                icon = Icons.Default.Group,
                                iconBg = Color(0xFFF1F0F5),
                                iconTint = Color(0xFF5F5E6B),
                                modifier = Modifier.weight(1f),
                                onClick = { navController?.navigate("patients") }
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            QuickActionCard(
                                title = "View\nReports",
                                subtitle = "All patient reports",
                                icon = Icons.Default.Description,
                                iconBg = Color(0xFFE7F6F1),
                                iconTint = Color(0xFF00BFA5),
                                modifier = Modifier.weight(1f),
                                onClick = { navController?.navigate("patients") }
                            )
                            QuickActionCard(
                                title = "ML\nModels",
                                subtitle = "Configure AI",
                                icon = Icons.Default.Psychology,
                                iconBg = Color(0xFFF1F0F5),
                                iconTint = Color(0xFF5F5E6B),
                                modifier = Modifier.weight(1f),
                                onClick = { navController?.navigate("select_ml") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Reports",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1C1E)
                            )
                        )
                        TextButton(onClick = { navController?.navigate("patients") }) {
                            Text(
                                text = "See All →",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = Color(0xFF4A90E2),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        val recentPatients = patients.sortedByDescending { it.createdAt }.take(3)
                        if (recentPatients.isNotEmpty()) {
                            recentPatients.forEach { p ->
                                RecentActivityItem(
                                    title = if (p.riskStatus.isNotBlank() && !p.riskStatus.equals("Pending", ignoreCase = true)) "Analysis Complete" else "Pending Analysis",
                                    subtitle = "${p.firstName} ${p.lastName} • ${if (p.riskStatus.isBlank() || p.riskStatus.equals("Pending", ignoreCase = true)) "High Risk" else p.riskStatus}",
                                    time = "Recent",
                                    icon = if (p.riskStatus.contains("High", ignoreCase = true)) Icons.Default.Warning else Icons.Default.TrendingUp,
                                    iconBg = if (p.riskStatus.contains("High", ignoreCase = true)) Color(0xFFFFEBEE) else Color(0xFFE7F6F1),
                                    iconTint = if (p.riskStatus.contains("High", ignoreCase = true)) Color(0xFFEF5350) else Color(0xFF00BFA5),
                                    onClick = {
                                        isLoadingReport = true
                                        viewModel?.loadPatientReport(p.id) {
                                            isLoadingReport = false
                                            navController?.navigate("result_summary")
                                        }
                                    }
                                )
                            }
                        } else {
                            Text(text = "No patients added yet. Tap 'New Prediction' to get started.", style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF74777F)))
                        }
                    }
                }
            }

            if (isLoadingReport) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(color = Color(0xFF4A90E2))
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Loading Report...", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.displaySmall.copy(
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F))
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color(0xFFC4C7C5),
                    modifier = Modifier.size(16.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E),
                        lineHeight = 20.sp
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F))
                )
            }
        }
    }
}

@Composable
fun RecentActivityItem(
    title: String,
    subtitle: String,
    time: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F))
                )
            }
            Text(
                text = time,
                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFC4C7C5))
            )
        }
    }
}

@Composable
fun BottomNavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, active: Boolean = false, onClick: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (active) Color(0xFF4A90E2) else Color(0xFF74777F),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (active) Color(0xFF4A90E2) else Color(0xFF74777F),
                fontWeight = if (active) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}


@Composable
fun ProfileScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    val userProfile = viewModel?.userProfile
    val userName = viewModel?.currentUser ?: "Dr. John Doe"
    val userEmail = viewModel?.currentUserEmail ?: "doctor@hospital.com"
    val initials = userName.split(" ").filter { it.isNotEmpty() }.map { it[0] }.take(2).joinToString("").uppercase()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1A1C1E)
                )
            }
            Text(
                text = "My Profile",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                ),
                modifier = Modifier.align(Alignment.Center)
            )
            Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                IconButton(onClick = { 
                    android.util.Log.d("BonePredict", "Navigating to Edit Profile from TopBar")
                    navController?.navigate("edit_profile") 
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color(0xFF4A90E2)
                    )
                }
                IconButton(onClick = { navController?.navigate("settings") }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color(0xFF1A1C1E)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Profile Avatar
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(Color(0xFFE8F1FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials.ifEmpty { "JD" },
                        style = MaterialTheme.typography.displayMedium.copy(
                            color = Color(0xFF4A90E2),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(Color(0xFF4A90E2))
                        .padding(8.dp)
                        .clickable { 
                            android.util.Log.d("BonePredict", "Navigating to Edit Profile from Avatar")
                            navController?.navigate("edit_profile") 
                         },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = userName,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
            )
            Text(
                text = userProfile?.specialty ?: "Periodontist",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF74777F))
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Stats Group
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column {
                    ProfileInfoRow(icon = Icons.Default.Email, label = "EMAIL ADDRESS", content = userEmail)
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F0F5))
                    ProfileInfoRow(icon = Icons.Default.Phone, label = "PHONE NUMBER", content = userProfile?.phone ?: "Not Provided")
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F0F5))
                    ProfileInfoRow(icon = Icons.Default.BusinessCenter, label = "INSTITUTION", content = userProfile?.institution ?: "Not Provided")
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F0F5))
                    ProfileInfoRow(icon = Icons.Default.Badge, label = "LICENSE NO", content = userProfile?.license_no ?: "Not Provided")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = { navController?.navigate("edit_profile") },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4A90E2))
            ) {
                Text(text = "Edit Profile", color = Color(0xFF4A90E2), fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.clickable { 
                    viewModel?.currentUser = null
                    viewModel?.currentUserEmail = null
                    viewModel?.userProfile = null
                    navController?.navigate("signin") { popUpTo("splash") { inclusive = true } } 
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, tint = Color(0xFFFA5252))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Sign Out", color = Color(0xFFFA5252), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val profile = viewModel?.userProfile
    var name by remember { mutableStateOf(profile?.name ?: viewModel?.currentUser ?: "") }
    var email by remember { mutableStateOf(profile?.email ?: viewModel?.currentUserEmail ?: "") }
    var phone by remember { mutableStateOf(profile?.phone ?: "") }
    var specialty by remember { mutableStateOf(profile?.specialty ?: "") }
    var institution by remember { mutableStateOf(profile?.institution ?: "") }
    var licenseNo by remember { mutableStateOf(profile?.license_no ?: "") }
    
    var isLoading by remember { mutableStateOf(false) }

    val initials = name.split(" ").filter { it.isNotEmpty() }.map { it[0] }.take(2).joinToString("").uppercase()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Profile", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8F9FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F1FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials.ifEmpty { "JD" },
                        style = MaterialTheme.typography.headlineLarge.copy(color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold)
                    )
                }
            }
            Text(
                text = "Tap camera to change photo",
                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFC4C7C5)),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            EditProfileField(label = "FULL NAME", value = name, onValueChange = { name = it }, placeholder = "Dr. Sunil")
            EditProfileField(label = "EMAIL ADDRESS", value = email, onValueChange = { email = it }, enabled = false, placeholder = "sunil@gmail.com")
            EditProfileField(label = "PHONE NUMBER", value = phone, onValueChange = { phone = it }, placeholder = "+91 9876543210")
            EditProfileField(label = "SPECIALTY", value = specialty, onValueChange = { specialty = it }, placeholder = "Periodontist")
            EditProfileField(label = "INSTITUTION", value = institution, onValueChange = { institution = it }, placeholder = "Dental College")
            EditProfileField(label = "LICENSE NO", value = licenseNo, onValueChange = { licenseNo = it }, placeholder = "LC-12345")

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    isLoading = true
                    viewModel?.updateProfile(
                        com.example.bonepredict.data.remote.UserProfile(
                            name = name,
                            email = email,
                            phone = phone,
                            specialty = specialty,
                            institution = institution,
                            license_no = licenseNo
                        )
                    ) {
                        isLoading = false
                        android.widget.Toast.makeText(context, "Profile saved successfully", android.widget.Toast.LENGTH_SHORT).show()
                        navController?.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Changes", fontWeight = FontWeight.SemiBold)
                }
            }

            TextButton(
                onClick = { navController?.popBackStack() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Cancel", color = Color(0xFF4A90E2))
            }
        }
    }
}

@Composable
fun EditProfileField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String = "", enabled: Boolean = true) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
        Text(text = label, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1C1E)))
        Surface(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            shape = RoundedCornerShape(12.dp),
            color = if (enabled) Color.White else Color(0xFFF1F0F5)
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                placeholder = { Text(placeholder, color = Color(0xFFC4C7C5)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        }
    }
}

@Composable
fun ProfileInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, content: String) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFF8F9FB)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF74777F), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFC4C7C5)))
            Text(text = content, style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF1A1C1E), fontWeight = FontWeight.Medium))
        }
    }
}

@Composable
fun AddPatientScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val generatedId = remember { "P-${System.currentTimeMillis().toString().takeLast(6)}" }

    var firstNameTouched by remember { mutableStateOf(false) }
    var lastNameTouched by remember { mutableStateOf(false) }
    var contactTouched by remember { mutableStateOf(false) }

    val isFirstNameValid = !firstName.any { it.isDigit() }
    val isLastNameValid = !lastName.any { it.isDigit() }
    val isContactValid = contact.length == 10 && contact.all { it.isDigit() }

    val context = androidx.compose.ui.platform.LocalContext.current
    val calendar = java.util.Calendar.getInstance()
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dob = "${month + 1}/$dayOfMonth/$year"
        },
        calendar.get(java.util.Calendar.YEAR),
        calendar.get(java.util.Calendar.MONTH),
        calendar.get(java.util.Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1A1C1E)
                )
            }
            Text(
                text = "Add New Patient",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(modifier = Modifier.padding(24.dp)) {
            // Patient ID Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFE8F1FF).copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Patient ID", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F)))
                    Text(text = generatedId, style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Form
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                PatientField(
                    label = "First Name", 
                    value = firstName, 
                    onValueChange = { firstName = it; firstNameTouched = true }, 
                    placeholder = "Enter First Name", 
                    modifier = Modifier.weight(1f),
                    isError = firstNameTouched && !isFirstNameValid,
                    errorText = "Invalid name"
                )
                PatientField(
                    label = "Last Name", 
                    value = lastName, 
                    onValueChange = { lastName = it; lastNameTouched = true }, 
                    placeholder = "Enter Last Name", 
                    modifier = Modifier.weight(1f),
                    isError = lastNameTouched && !isLastNameValid,
                    errorText = "Invalid name"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            PatientField(
                label = "Date of Birth",
                value = dob,
                onValueChange = { dob = it },
                placeholder = "mm/dd/yyyy",
                trailingIcon = Icons.Default.CalendarMonth,
                readOnly = true,
                onClick = { datePickerDialog.show() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Gender", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1C1E)))
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                GenderButton(label = "Male", active = selectedGender == "Male", onClick = { selectedGender = "Male" }, modifier = Modifier.weight(1f))
                GenderButton(label = "Female", active = selectedGender == "Female", onClick = { selectedGender = "Female" }, modifier = Modifier.weight(1f))
                GenderButton(label = "Other", active = selectedGender == "Other", onClick = { selectedGender = "Other" }, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            PatientField(
                label = "Contact Number", 
                value = contact, 
                onValueChange = { contact = it; contactTouched = true }, 
                placeholder = "0000000000",
                keyboardType = KeyboardType.Phone,
                isError = contactTouched && contact.isNotEmpty() && !isContactValid,
                errorText = "Invalid contact (10 digits required)"
            )

            Spacer(modifier = Modifier.height(48.dp))

            val isButtonEnabled = firstName.isNotBlank() && isFirstNameValid && 
                                 lastName.isNotBlank() && isLastNameValid && 
                                 dob.isNotBlank() && 
                                 selectedGender.isNotBlank() &&
                                 contact.isNotBlank() && isContactValid && 
                                 !isLoading

            Button(
                onClick = {
                    isLoading = true
                    val patient = com.example.bonepredict.data.model.Patient(
                        firstName = firstName,
                        lastName = lastName,
                        dob = dob,
                        gender = selectedGender,
                        contactNumber = contact,
                        doctorId = viewModel?.currentDoctorId ?: "1" 
                    )
                    viewModel?.currentNewPatient = patient
                    viewModel?.addPatient(patient) {
                        isLoading = false
                        navController?.navigate("demographics")
                    }
                },
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2),
                    disabledContainerColor = Color(0xFFE0E0E0)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Continue", 
                        color = if (isButtonEnabled) Color.White else Color(0xFF9E9E9E), 
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun PatientField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    trailingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    isError: Boolean = false,
    errorText: String = "",
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1C1E)))
        Spacer(modifier = Modifier.height(8.dp))
        Box {
            Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color.White) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = { Text(placeholder, color = Color(0xFFC4C7C5)) },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = trailingIcon?.let { 
                        { 
                            if (onClick != null) {
                                IconButton(onClick = onClick) {
                                    Icon(imageVector = it, contentDescription = null, tint = Color(0xFF1A1C1E))
                                }
                            } else {
                                Icon(imageVector = it, contentDescription = null, tint = Color(0xFF1A1C1E))
                            }
                        } 
                    },
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                    isError = isError,
                    readOnly = readOnly,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
                )
            }
            if (onClick != null) {
                Box(modifier = Modifier.matchParentSize().clickable { onClick() })
            }
        }
        if (isError && errorText.isNotEmpty()) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun GenderButton(label: String, active: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = if (active) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1A1C1E)) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F0F5))
    ) {
        Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
            Text(
                text = label, 
                color = if (active) Color(0xFF1A1C1E) else Color(0xFF74777F), 
                fontWeight = if (active) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}


@Composable
fun PatientsScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    val patients by viewModel?.patients?.collectAsState() ?: remember { mutableStateOf(emptyList<com.example.bonepredict.data.model.Patient>()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    
    var isLoadingReport by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel?.currentUserEmail) {
        viewModel?.fetchPatients()
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.height(80.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavItem(icon = Icons.Default.Home, label = "Home", onClick = { navController?.navigate("dashboard") })
                    BottomNavItem(icon = Icons.Default.Group, label = "Patients", active = true)
                    BottomNavItem(icon = Icons.Default.ShowChart, label = "Analysis", onClick = { navController?.navigate("trend_analysis") })
                    BottomNavItem(icon = Icons.Default.Settings, label = "Settings", onClick = { navController?.navigate("settings") })
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController?.navigate("add_patient") },
                containerColor = Color(0xFF4A90E2),
                contentColor = Color.White,
                shape = androidx.compose.foundation.shape.CircleShape,
                modifier = Modifier.offset(y = (-20).dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Patient")
            }
        },
        containerColor = Color(0xFFF8F9FB)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .statusBarsPadding()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color(0xFF1A1C1E)
                        )
                    }
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Patients",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A1C1E)
                            )
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(if (viewModel?.isOnline == true) Color(0xFF00BFA5) else Color(0xFFFA5252))
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (viewModel?.isOnline == true) "Online" else "Offline",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (viewModel?.isOnline == true) Color(0xFF00BFA5) else Color(0xFFFA5252)
                                )
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    // Search Bar
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search by name or ID...", color = Color(0xFFC4C7C5)) },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFFC4C7C5)) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Filters
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChipItem(label = "All", active = selectedFilter == "All", onClick = { selectedFilter = "All" })
                        FilterChipItem(label = "Recent", active = selectedFilter == "Recent", onClick = { selectedFilter = "Recent" })
                        FilterChipItem(label = "High Risk", active = selectedFilter == "High Risk", onClick = { selectedFilter = "High Risk" })
                        FilterChipItem(label = "Pending", active = selectedFilter == "Pending", onClick = { selectedFilter = "Pending" })
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Patient List
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val filteredPatients = patients.filter { patient ->
                            val fullName = "${patient.firstName} ${patient.lastName}"
                            val matchesSearch = searchQuery.isBlank() || fullName.contains(searchQuery, ignoreCase = true) || patient.id.contains(searchQuery, ignoreCase = true)
                            val matchesFilter = when (selectedFilter) {
                                "High Risk" -> patient.riskStatus.contains("High", ignoreCase = true)
                                "Pending"   -> patient.riskStatus.isBlank() || patient.riskStatus.equals("Pending", ignoreCase = true)
                                "Recent"    -> true
                                else        -> true
                            }
                            matchesSearch && matchesFilter
                        }
                        if (filteredPatients.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                                Text(text = "No patients found", color = Color(0xFF74777F))
                            }
                        } else {
                            filteredPatients.forEach { patient ->
                                PatientListItem(
                                    initials = "${patient.firstName.getOrNull(0) ?: ""}${patient.lastName.getOrNull(0) ?: ""}",
                                    name = "${patient.firstName} ${patient.lastName}",
                                    id = patient.id,
                                    lastVisit = if (patient.riskStatus.isNotBlank() && !patient.riskStatus.equals("Pending", ignoreCase = true)) "Analyzed" else "New Patient",
                                    risk = if (patient.riskStatus.isBlank()) "Pending" else patient.riskStatus,
                                    onClick = {
                                        isLoadingReport = true
                                        viewModel?.loadPatientReport(patient.id) {
                                            isLoadingReport = false
                                            navController?.navigate("result_summary")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (isLoadingReport) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(color = Color(0xFF4A90E2))
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Loading Report...", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipItem(label: String, active: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (active) Color(0xFF1A1C1E) else Color(0xFFF1F0F5)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                color = if (active) Color.White else Color(0xFF74777F),
                fontWeight = if (active) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}

@Composable
fun PatientListItem(initials: String, name: String, id: String, lastVisit: String, risk: String, onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(Color(0xFFF1F0F5)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = initials, style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF74777F), fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
                )
                Text(
                    text = "ID: $id • Last Visit: $lastVisit",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F))
                )
            }
            RiskBadge(risk = risk)
        }
    }
}

@Composable
fun RiskBadge(risk: String) {
    val (bgColor, textColor, icon) = when {
        risk.equals("Low", ignoreCase = true) -> Triple(Color(0xFFE7F6F1), Color(0xFF00BFA5), Icons.Default.CheckCircle)
        risk.equals("Moderate", ignoreCase = true) -> Triple(Color(0xFFFFF4E6), Color(0xFFFFA000), Icons.Default.Warning)
        risk.contains("High", ignoreCase = true) -> Triple(Color(0xFFFFEAEE), Color(0xFFFA5252), Icons.Default.Warning)
        else -> Triple(Color(0xFFFFEAEE), Color(0xFFFA5252), Icons.Default.Warning)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = textColor, modifier = Modifier.size(14.dp))
            Text(text = risk, style = MaterialTheme.typography.labelSmall.copy(color = textColor, fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun DemographicsScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    var selectedEthnicity by remember { mutableStateOf("") }
    var smokingStatus by remember { mutableStateOf("") }
    var diabetesStatus by remember { mutableStateOf("") }
    var osteoporosisHistory by remember { mutableStateOf("") }
    var showEthnicityMenu by remember { mutableStateOf(false) }
    
    val ethnicities = listOf("Caucasian", "African American", "Asian", "Hispanic", "Middle Eastern", "Other")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1A1C1E)
                )
            }
            Text(
                text = "Demographics",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(modifier = Modifier.padding(24.dp)) {
            // Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Data Collection Progress", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F)))
                Text(text = "20 %", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = 0.2f,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF4A90E2),
                trackColor = Color(0xFFE8F1FF)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Patient Summary Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Patient", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFC4C7C5)))
                        Text(text = "${viewModel?.currentNewPatient?.firstName ?: "Jane"} ${viewModel?.currentNewPatient?.lastName ?: "Doe"}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Age / Gender", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFC4C7C5)))
                        val dob = viewModel?.currentNewPatient?.dob ?: ""
                        Text(text = "${if (dob.length >= 4) dob.takeLast(4) else "N/A"} / ${viewModel?.currentNewPatient?.gender ?: "Female"}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ethnicity
            Text(text = "Ethnicity", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
            Spacer(modifier = Modifier.height(8.dp))
            Box {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showEthnicityMenu = true },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F0F5))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedEthnicity.isEmpty()) "Select Ethnicity" else selectedEthnicity,
                            color = if (selectedEthnicity.isEmpty()) Color(0xFFC4C7C5) else Color(0xFF1A1C1E)
                        )
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF74777F))
                    }
                }

                DropdownMenu(
                    expanded = showEthnicityMenu,
                    onDismissRequest = { showEthnicityMenu = false },
                    modifier = Modifier.fillMaxWidth(0.9f).background(Color.White)
                ) {
                    ethnicities.forEach { ethnicity ->
                        DropdownMenuItem(
                            text = { Text(ethnicity) },
                            onClick = {
                                selectedEthnicity = ethnicity
                                showEthnicityMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Smoking Status
            Text(text = "Smoking Status", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OptionButton(label = "Never", active = smokingStatus == "Never", onClick = { smokingStatus = "Never" }, modifier = Modifier.weight(1f))
                OptionButton(label = "Former", active = smokingStatus == "Former", onClick = { smokingStatus = "Former" }, modifier = Modifier.weight(1f))
                OptionButton(label = "Current", active = smokingStatus == "Current", onClick = { smokingStatus = "Current" }, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Diabetes
            Text(text = "Diabetes Status", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OptionButton(label = "No", active = diabetesStatus == "No", onClick = { diabetesStatus = "No" }, modifier = Modifier.weight(1f))
                OptionButton(label = "Yes (Type 1/2)", active = diabetesStatus == "Yes (Type 1/2)", onClick = { diabetesStatus = "Yes (Type 1/2)" }, modifier = Modifier.weight(1.8f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Osteoporosis
            Text(text = "Osteoporosis History", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OptionButton(label = "No History", active = osteoporosisHistory == "No History", onClick = { osteoporosisHistory = "No History" }, modifier = Modifier.weight(1f))
                OptionButton(label = "Diagnosed", active = osteoporosisHistory == "Diagnosed", onClick = { osteoporosisHistory = "Diagnosed" }, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(48.dp))

            val isButtonEnabled = selectedEthnicity.isNotBlank() && 
                                 smokingStatus.isNotBlank() && 
                                 diabetesStatus.isNotBlank() && 
                                 osteoporosisHistory.isNotBlank()

            Button(
                onClick = {
                    val currentPatient = viewModel?.currentNewPatient
                    if (currentPatient != null) {
                        val data = viewModel.currentClinicalData ?: com.example.bonepredict.data.model.ClinicalData(
                            patientId = currentPatient.id,
                            weight = 70f, // Placeholder if not in UI
                            smokingStatus = smokingStatus,
                            alcoholConsumption = "Occasional", // Placeholder
                            hasDiabetes = diabetesStatus != "No",
                            hasHypertension = false,
                            hasOsteoporosis = osteoporosisHistory != "No History",
                            probingDepth = 0f,
                            cal = 0f,
                            bleedingOnProbing = false
                        )
                        viewModel?.currentClinicalData = data
                    }
                    navController?.navigate("peridontal_status")
                },
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2),
                    disabledContainerColor = Color(0xFFE0E0E0)
                )
            ) {
                Text(
                    text = "Continue", 
                    color = if (isButtonEnabled) Color.White else Color(0xFF9E9E9E), 
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun OptionButton(label: String, active: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = if (active) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1A1C1E)) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F0F5))
    ) {
        Box(modifier = Modifier.padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = if (active) Color(0xFF1A1C1E) else Color(0xFF74777F),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (active) FontWeight.Bold else FontWeight.Normal),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun PeridontalStatusScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    var probingDepth by remember { mutableStateOf("") }
    var attachmentLevel by remember { mutableStateOf("") }
    var bleedingIndex by remember { mutableStateOf("") }
    var plaqueIndex by remember { mutableStateOf("") }
    var toothMobility by remember { mutableStateOf("") }
    var gingivalPhenotype by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1A1C1E)
                )
            }
            Text(
                text = "Peridontal Status",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(modifier = Modifier.padding(24.dp)) {
            // Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Data Collection Progress", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F)))
                Text(text = "60 %", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = 0.6f,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF4A90E2),
                trackColor = Color(0xFFE8F1FF)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tooth Visualization Placeholder
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F0F5))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFF1F0F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Simple custom tooth drawing with Box
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(width = 40.dp, height = 50.dp)
                                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                    .background(Color.White)
                                    .border(androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC4C7C5)), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            )
                            Box(
                                modifier = Modifier
                                    .size(width = 40.dp, height = 30.dp)
                                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                                    .background(Color(0xFFFFEAEE))
                                    .border(androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFA5252).copy(alpha = 0.3f)), RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                            )
                        }
                        // CAL indicator line
                        HorizontalDivider(
                            modifier = Modifier
                                .width(60.dp)
                                .offset(x = 20.dp),
                            color = Color(0xFF74777F),
                            thickness = 1.dp
                        )
                        Text(
                            text = "CAL",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F)),
                            modifier = Modifier.offset(x = 55.dp, y = (-10).dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Target Tooth: #36 (Mandibular Left First Molar)",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F))
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Input Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Probing Depth", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                    Spacer(modifier = Modifier.height(8.dp))
                    MeasurementField(value = probingDepth, onValueChange = { probingDepth = it }, placeholder = "mm")
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Attachment Level", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                    Spacer(modifier = Modifier.height(8.dp))
                    MeasurementField(value = attachmentLevel, onValueChange = { attachmentLevel = it }, placeholder = "mm")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Bleeding Index", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                    Spacer(modifier = Modifier.height(8.dp))
                    MeasurementField(value = bleedingIndex, onValueChange = { bleedingIndex = it }, placeholder = "%")
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Plaque Index", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                    Spacer(modifier = Modifier.height(8.dp))
                    MeasurementField(value = plaqueIndex, onValueChange = { plaqueIndex = it }, placeholder = "0-3")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tooth Mobility
            Text(text = "Tooth Mobility", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OptionButton(label = "0", active = toothMobility == "0", onClick = { toothMobility = "0" }, modifier = Modifier.weight(1f))
                OptionButton(label = "I", active = toothMobility == "I", onClick = { toothMobility = "I" }, modifier = Modifier.weight(1f))
                OptionButton(label = "II", active = toothMobility == "II", onClick = { toothMobility = "II" }, modifier = Modifier.weight(1f))
                OptionButton(label = "III", active = toothMobility == "III", onClick = { toothMobility = "III" }, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Gingival Phenotype
            Text(text = "Gingival Phenotype", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OptionButton(label = "Thin Scalloped", active = gingivalPhenotype == "Thin Scalloped", onClick = { gingivalPhenotype = "Thin Scalloped" }, modifier = Modifier.weight(1f))
                OptionButton(label = "Thick Flat", active = gingivalPhenotype == "Thick Flat", onClick = { gingivalPhenotype = "Thick Flat" }, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(48.dp))

            val isButtonEnabled = probingDepth.isNotBlank() && 
                                 attachmentLevel.isNotBlank() && 
                                 bleedingIndex.isNotBlank() && 
                                 plaqueIndex.isNotBlank() && 
                                 toothMobility.isNotBlank() && 
                                 gingivalPhenotype.isNotBlank()

            Button(
                onClick = {
                    val currentData = viewModel?.currentClinicalData
                    if (currentData != null) {
                        viewModel.currentClinicalData = currentData.copy(
                            probingDepth = probingDepth.toFloatOrNull() ?: 0f,
                            cal = attachmentLevel.toFloatOrNull() ?: 0f,
                            bleedingOnProbing = (bleedingIndex.toFloatOrNull() ?: 0f) > 10f,
                            bleedingIndex = bleedingIndex.toFloatOrNull() ?: 0f,
                            plaqueIndex = plaqueIndex.toFloatOrNull() ?: 0f,
                            toothMobility = toothMobility,
                            gingivalPhenotype = gingivalPhenotype
                        )
                    }
                    navController?.navigate("cbct")
                },
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4A90E2),
                    disabledContainerColor = Color(0xFFE0E0E0)
                )
            ) {
                Text(
                    text = "Continue", 
                    color = if (isButtonEnabled) Color.White else Color(0xFF9E9E9E), 
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun MeasurementField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F0F5))
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color(0xFFC4C7C5)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun HomeScreen() {
    DashboardScreen()
}

@Composable
fun CBCTScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    val selectedCbctUris = viewModel?.selectedCbctUris ?: remember { mutableStateListOf<Uri>() }
    
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            viewModel?.selectedCbctUris?.addAll(uris)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        // Handle camera photo if needed
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1A1C1E)
                )
            }
            Text(
                text = "CBCT Scan",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
            // Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Data Collection Progress", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F)))
                Text(text = "80 %", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = 0.8f,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF4A90E2),
                trackColor = Color(0xFFE8F1FF)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = if (selectedCbctUris.isEmpty()) "Upload CBCT Scan" else "Scan Selected",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
            )
            Text(
                text = if (selectedCbctUris.isEmpty()) "Take a photo or upload from gallery" else "Preview your scan before proceeding",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF74777F))
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (selectedCbctUris.isEmpty()) {
                // Upload Options
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    UploadOptionCard(
                        icon = Icons.Filled.CameraAlt,
                        label = "Camera",
                        onClick = { cameraLauncher.launch(null) },
                        modifier = Modifier.weight(1f)
                    )
                    UploadOptionCard(
                        icon = Icons.Filled.Collections, 
                        label = "Select Scans",
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                // Preview Area (List of scans)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFF1F0F5))
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(selectedCbctUris) { uri ->
                            Box(modifier = Modifier.size(180.dp, 220.dp)) {
                                Image(
                                    painter = rememberAsyncImagePainter(uri),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                IconButton(
                                    onClick = { viewModel?.selectedCbctUris?.remove(uri) },
                                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape).size(24.dp)
                                ) {
                                    Icon(Icons.Filled.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val currentData = viewModel?.currentClinicalData
                    if (currentData != null) {
                        viewModel.calculateRiskAssessment(currentData)
                    }
                    navController?.navigate("upload_success")
                },
                enabled = selectedCbctUris.isNotEmpty(),
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BFA5),
                    disabledContainerColor = Color(0xFFE0E0E0)
                )
            ) {
                Text(
                    text = "Continue to Analysis", 
                    color = if (selectedCbctUris.isNotEmpty()) Color.White else Color(0xFF9E9E9E),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF1F6FF)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.Info, contentDescription = null, tint = Color(0xFF4A90E2), modifier = Modifier.size(20.dp))
                    Text(
                        text = "Real-time AI analysis will start immediately after clicking continue.",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF4A90E2))
                    )
                }
            }
        }
    }
}

@Composable
fun UploadOptionCard(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(140.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF1F0F5)),
        shadowElevation = 2.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier.size(56.dp).background(Color(0xFFE8F1FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF4A90E2), modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = label, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E)))
        }
    }
}

@Composable
fun UploadSuccessScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    val selectedFiles = "${viewModel?.selectedCbctUris?.size ?: 0} CBCT Scans Selected"
    val isAnalyzing = viewModel?.isAnalyzing ?: false
    
    // Simulate AI analysis delay
    LaunchedEffect(Unit) {
        viewModel?.isAnalyzing = true
        delay(3000) // 3 seconds of AI background processing
        viewModel?.isAnalyzing = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = androidx.compose.foundation.shape.CircleShape,
            color = Color(0xFFE8F9F3),
            modifier = Modifier.size(100.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF00BFA5),
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Upload Successful!",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your CBCT scans have been securely\nuploaded and are ready for analysis.",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF74777F)),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F0F5))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE8F1FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Filled.Description, contentDescription = null, tint = Color(0xFF4A90E2))
                }
                Column {
                    Text(
                        text = if (selectedFiles.length > 30) selectedFiles.take(27) + "..." else selectedFiles,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(text = "Digital CBCT Scan • Just now", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F)))
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { navController?.navigate("data_validation") },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2)),
            enabled = !isAnalyzing
        ) {
            Text(
                text = if (isAnalyzing) "AI Analyzing Scans..." else "Continue to Report", 
                color = Color.White, 
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { navController?.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4A90E2))
        ) {
            Text(text = "Upload Another", color = Color(0xFF4A90E2), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun DataValidationScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1A1C1E),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Data Validation",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Success Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(Color(0xFFE8F1FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4A90E2),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Ready for Analysis",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Data has been validated. Minor issues found but can proceed.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF74777F)),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Validation Checklist",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Checklist Items
            ValidationItem("Patient Demographics", "Complete", true)
            ValidationItem("Medical History", "Verified", true)
            ValidationItem("Periodontal Data", "Within range", true)
            ValidationItem("CBCT Scan Quality", "Slight noise detected", false)
            ValidationItem("Data Completeness", "100%", true)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController?.navigate("feature_selection") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "Proceed to Analysis", color = Color.White, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { /* Fix Issues Action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4A90E2))
            ) {
                Text(text = "Fix Issues", color = Color(0xFF4A90E2), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun ValidationItem(title: String, subtitle: String, isSuccess: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(if (isSuccess) Color(0xFFE8F9F3) else Color(0xFFFFF7E6)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (isSuccess) Color(0xFF00BFA5) else Color(0xFFFFA000),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF74777F)))
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isSuccess) Color(0xFFE8F9F3) else Color(0xFFFFF7E6)
            ) {
                Text(
                    text = if (isSuccess) "Success" else "Warning",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (isSuccess) Color(0xFF00BFA5) else Color(0xFFFFA000),
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun FeatureSelectionScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1A1C1E),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Feature Selection",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select the feature categories to include in the predictive model. More features may increase accuracy but require more processing time.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF74777F)),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Feature Categories
            FeatureCategoryItem(Icons.Default.Person, "Demographic Features", "Age, Gender, Ethnicity, Habits (5 selected)", onClick = { navController?.navigate("demographic_features") })
            FeatureCategoryItem(Icons.Default.LocalHospital, "Clinical Features", "Medical History, Systemic Conditions (8 selected)")
            FeatureCategoryItem(Icons.Default.Description, "Radiographic Features", "Bone Density, Trabecular Pattern (6 selected)")
            FeatureCategoryItem(Icons.Default.Layers, "Periodontal Features", "Probing Depth, CAL, Bleeding (7 selected)")

            Spacer(modifier = Modifier.height(32.dp))

            // Summary Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFE8F1FF)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Features", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF4A90E2)))
                        Text(text = "26", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF4A90E2)))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = 0.7f,
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF4A90E2),
                        trackColor = Color(0xFFD0E1F9)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController?.navigate("preprocessing") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "Continue", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun PreprocessingScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1A1C1E),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Preprocessing",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(Color(0xFFE8F9F3)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color(0xFF00BFA5),
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Preprocessing Complete",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Preparing dataset for optimal model performance",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF74777F)),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Progress Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "100 %", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold))
                    Text(text = "Ready", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF4A90E2)))
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = 1f,
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                    color = Color(0xFF4A90E2),
                    trackColor = Color(0xFFE8F1FF)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Checklist
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PreprocessingItem("Data Normalization")
                PreprocessingItem("Missing Value Handling")
                PreprocessingItem("Feature Scaling")
                PreprocessingItem("Outlier Detection")
                PreprocessingItem("Data Augmentation")
            }

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = { navController?.navigate("select_ml") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "Continue to Model Selection", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun PreprocessingItem(label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF00BFA5),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
    }
}

@Composable
fun SelectMLScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1A1C1E),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Select ML Model",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Choose an algorithm to run the prediction.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF74777F))
            )

            Spacer(modifier = Modifier.height(24.dp))

            MLModelCard(
                icon = Icons.Default.Schema,
                title = "Random Forest",
                subtitle = "High Accuracy • Robust",
                accuracy = "94.2%",
                isRecommended = true,
                onClick = { navController?.navigate("random_forest") }
            )

            MLModelCard(
                icon = Icons.Default.Bolt,
                title = "XGBoost",
                subtitle = "Fast Processing • Efficient",
                accuracy = "93.8%"
            )

            MLModelCard(
                icon = Icons.Default.Memory,
                title = "Neural Network",
                subtitle = "Deep Learning • Complex",
                accuracy = "95.1%"
            )

            MLModelCard(
                icon = Icons.Default.Layers,
                title = "SVM",
                subtitle = "Robust Classification",
                accuracy = "89.5%"
            )

            MLModelCard(
                icon = Icons.Default.Hub,
                title = "Ensemble",
                subtitle = "Combined Approach",
                accuracy = "96.5%"
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { navController?.navigate("random_forest") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "Start Prediction", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun MLModelCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    accuracy: String,
    isRecommended: Boolean = false,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE8F1FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF4A90E2))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Text(text = subtitle, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF74777F)))
                }
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFC4C7C5))
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F0F5))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Accuracy", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F)))
                    Text(text = accuracy, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
                if (isRecommended) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFE8F1FF)
                    ) {
                        Text(
                            text = "Recommended",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureCategoryItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8F9FB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF74777F))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF74777F)), maxLines = 1)
            }

            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFC4C7C5))
        }
    }
}

@Composable
fun DemographicFeaturesScreen(navController: NavController? = null) {
    Scaffold(
        containerColor = Color(0xFFF8F9FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color(0xFF1A1C1E),
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "Demographic Features",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Features",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C1E)
                        )
                    )
                    TextButton(onClick = { /* Select All */ }) {
                        Text(
                            text = "Select All",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = Color(0xFF4A90E2),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Feature List
                DemographicFeatureItem("Age", 0.85f, true)
                DemographicFeatureItem("Gender", 0.45f, true)
                DemographicFeatureItem("Smoking Status", 0.92f, true)
                DemographicFeatureItem("Diabetes History", 0.88f, true)
                DemographicFeatureItem("Ethnicity", 0.30f, false)

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { navController?.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
                ) {
                    Text(
                        text = "Save Selection",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun DemographicFeatureItem(label: String, importance: Float, initiallyChecked: Boolean) {
    var isChecked by remember { mutableStateOf(initiallyChecked) }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1C1E)
                    )
                )
                Switch(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF4A90E2)
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Importance",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8)),
                    modifier = Modifier.width(80.dp)
                )
                LinearProgressIndicator(
                    progress = importance,
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF4DB6AC),
                    trackColor = Color(0xFFF1F5F9)
                )
                Text(
                    text = "${(importance * 100).toInt()} %",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}

@Composable
fun RandomForestScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1A1C1E),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Random Forest",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(modifier = Modifier.padding(24.dp)) {
            // Model Info Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE8F1FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Schema,
                        contentDescription = null,
                        tint = Color(0xFF4A90E2),
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text(
                        text = "Random Forest",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Ensemble Learning Method",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF74777F))
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // description card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF8F9FB),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F0F5))
            ) {
                Text(
                    text = "Constructs a multitude of decision trees at training time. Excellent for handling non-linear data and avoiding overfitting.",
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF44474F),
                        lineHeight = 24.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Performance Metrics
            Text(
                text = "Performance Metrics",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))

            MetricBar("Accuracy", "94.2%", 0.942f, Color(0xFF4A90E2))
            MetricBar("Precision", "92.8%", 0.928f, Color(0xFF32A89E))
            MetricBar("Recall", "91.5%", 0.915f, Color(0xFFF9A825))
            MetricBar("AUC-ROC", "0.96", 0.96f, Color(0xFF7C4DFF))

            Spacer(modifier = Modifier.height(32.dp))

            // Hyperparameters
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Hyperparameters",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFFC4C7C5),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                HyperparameterCard("n_estimators", "100", modifier = Modifier.weight(1f))
                HyperparameterCard("max_depth", "None", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = { navController?.navigate("training_model") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "Select This Model", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun MetricBar(label: String, value: String, progress: Float, color: Color) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF44474F)))
            Text(text = value, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun HyperparameterCard(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F0F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F)))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun TrainingModelScreen(navController: NavController? = null) {
    var progress by remember { mutableStateOf(0.79f) }
    var currentEpoch by remember { mutableStateOf(39) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Training Model",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Training Visualization (Placeholder for Chart)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF0F172A) // Dark slate for chart bg
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    // Grid lines simulation
                    Column(modifier = Modifier.fillMaxSize()) {
                        repeat(5) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.05f))
                        }
                    }
                    Row(modifier = Modifier.fillMaxSize()) {
                        repeat(8) {
                            VerticalDivider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.05f))
                        }
                    }
                    
                    // Simulated curves
                    Text(
                        text = "ACC:  0.9230    LOSS:   0.1367",
                        modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFFACC15), fontWeight = FontWeight.Bold)
                    )
                    
                    // Simple path representation
                    Icon(
                        imageVector = Icons.Default.AutoGraph,
                        contentDescription = null,
                        tint = Color(0xFF4A90E2),
                        modifier = Modifier.fillMaxSize().padding(32.dp).alpha(0.3f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Progress Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "Training in Progress",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Epoch  $currentEpoch /50",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF74777F))
                    )
                }
                Text(
                    text = "${(progress * 100).toInt()} %",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color(0xFF4A90E2),
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = Color(0xFF4A90E2),
                trackColor = Color(0xFFE8F1FF)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Stats Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                TrainingStatItem("Time Elapsed", "00:12")
                VerticalDivider(modifier = Modifier.height(40.dp).align(Alignment.CenterVertically), color = Color(0xFFF1F0F5))
                TrainingStatItem("Remaining", "00:08")
                VerticalDivider(modifier = Modifier.height(40.dp).align(Alignment.CenterVertically), color = Color(0xFFF1F0F5))
                TrainingStatItem("Speed", "12ms/step")
            }

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = { navController?.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF4D4D).copy(alpha = 0.3f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF4D4D))
            ) {
                Text(text = "Cancel Training", fontWeight = FontWeight.SemiBold)
            }
            
            // Temporary auto-navigation for demo purposes (wait 3s and go to next)
            LaunchedEffect(Unit) {
                delay(3000)
                navController?.navigate("prediction_ready")
            }
        }
    }
}

@Composable
fun TrainingStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF74777F)))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
fun PredictionReadyScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F6FF))
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(Color(0xFFE8F9F3)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF00BFA5),
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Prediction Ready!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "The model has successfully analyzed the patient data and generated a risk assessment.",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF64748B), lineHeight = 28.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Summary Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    PredictionInfoRow("Model Used", "Random Forest v2.1")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF1F5F9))
                    PredictionInfoRow("Patient ID", "P-2024-089")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFF1F5F9))
                    PredictionInfoRow("Processing Time", "1.4s")
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController?.navigate("risk_assessment") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "View Prediction Results", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { navController?.navigate("dashboard") { popUpTo("dashboard") { inclusive = true } } }) {
                Text(
                    text = "Return to Dashboard",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF4A90E2),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
fun PredictionInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B), fontWeight = FontWeight.Bold))
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold))
    }
}

@Composable
fun RiskAssessmentScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    val prediction = viewModel?.selectedPrediction
    val probability = prediction?.riskScore?.toInt() ?: 78 
    val riskLevel = prediction?.riskCategory ?: "High Risk"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Risk Assessment",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = 20.sp
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Probability Circle
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .border(2.dp, Color(0xFF1E293B), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$probability %",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    )
                    Text(
                        text = "Probability",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF64748B)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Risk Badge
            Surface(
                color = if (riskLevel.contains("High", ignoreCase = true)) Color(0xFFFFEBEE) else Color(0xFFE8F1FF),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = riskLevel,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = if (riskLevel.contains("High", ignoreCase = true)) Color(0xFFD32F2F) else Color(0xFF4A90E2),
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Warning Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFFFEBEE).copy(alpha = 0.5f),
                border = BorderStroke(1.dp, Color(0xFFFFEBEE))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$riskLevel Detected",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (riskLevel.contains("High", ignoreCase = true)) Color(0xFFB71C1C) else Color(0xFF1E88E5)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (riskLevel.contains("High", ignoreCase = true)) 
                            "Patient shows significant indicators for rapid alveolar ridge resorption within the next 12 months."
                            else "Patient shows stable bone levels with low indicators for rapid resorption.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (riskLevel.contains("High", ignoreCase = true)) Color(0xFFD32F2F) else Color(0xFF1E88E5),
                            lineHeight = 22.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Why this result? Card
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController?.navigate("risk_explanation") },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE8F1FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFF4A90E2),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Why this result?",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "View factors",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                        )
                    }
                }

                // Visuals Card
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController?.navigate("trend_analysis") },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE0F2F1)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color(0xFF00897B),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Visuals",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "See bone loss",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController?.navigate("result_summary") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "View Full Report", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RiskExplanationScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    val clinicalData = viewModel?.currentClinicalData
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Risk Explanation",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = 20.sp
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Key Contributing Factors",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "SHAP analysis showing which features influenced the model's prediction most.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B))
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Factors List
            clinicalData?.let { data ->
                if (data.smokingStatus != "Never") FactorItem("Smoking (Status: ${data.smokingStatus})", "+24%", true, "High Impact")
                if (data.hasDiabetes) FactorItem("Diabetes (Managed: No)", "+18%", true, "High Impact")
                if (data.hasOsteoporosis) FactorItem("Osteoporosis History", "+12%", true, "Medium Impact")
                if (!data.hasDiabetes && data.smokingStatus == "Never") FactorItem("No Major Risk Factors", "-15%", false, "Protective")
            } ?: run {
                FactorItem("Smoking History", "+24%", true, "High Impact")
                FactorItem("Diabetes (Type 2)", "+18%", true, "High Impact")
                FactorItem("Bone Density", "+12%", true, "Medium Impact")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Interpretation Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFE8F1FF).copy(alpha = 0.5f),
                border = BorderStroke(1.dp, Color(0xFFE8F1FF))
            ) {
                Row(modifier = Modifier.padding(20.dp)) {
                    Icon(
                        imageVector = Icons.Default.Info, // This one is fine
                        contentDescription = null,
                        tint = Color(0xFF4A90E2),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "How to interpret?",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Positive values (red) increase the risk probability, while negative values (green) decrease it. The magnitude indicates the strength of the influence.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), lineHeight = 18.sp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController?.navigate("bone_loss_visual") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "View Bone Loss Visuals", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FactorItem(label: String, value: String, isPositive: Boolean, impact: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = impact,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isPositive) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.ShowChart,
                    contentDescription = null,
                    tint = if (isPositive) Color(0xFFEF5350) else Color(0xFF66BB6A),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isPositive) Color(0xFFEF5350) else Color(0xFF66BB6A)
                    )
                )
            }
        }
    }
}

@Composable
fun BoneLossVisualScreen(navController: NavController? = null) {
    var selectedTime by remember { mutableStateOf("+1yr") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Bone Loss Visual",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = 20.sp
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Visual Area
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1E293B)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(60.dp, 80.dp)
                                .background(Color.White, RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(80.dp)
                                .background(Color(0xFFEF5350).copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                .border(1.dp, Color(0xFFEF5350).copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(Color.White.copy(alpha = 0.5f))
                            )
                        }
                    }
                    
                    Text(
                        text = "Cross-section View",
                        modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.6f))
                    )
                    
                    Surface(
                        modifier = Modifier.align(Alignment.CenterEnd).padding(end = 40.dp),
                        color = Color(0xFFEF5350),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "-1.2 mm",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Time Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("Now", "+1yr", "+3yr", "+5yr").forEach { time ->
                    val isSelected = selectedTime == time
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTime = time },
                        color = if (isSelected) Color(0xFF1E293B) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = time,
                            modifier = Modifier.padding(vertical = 12.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color(0xFF64748B)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Predicted Loss", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B)))
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(text = "1.2", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFFEF5350), fontSize = 24.sp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "mm", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFEF5350), fontWeight = FontWeight.Bold))
                        }
                    }
                }
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Bone Density", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B)))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "-12%", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), fontSize = 24.sp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController?.navigate("confidence_analysis") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "View Confidence Metrics", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}

@Composable
fun ConfidenceAnalysisScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Confidence Analysis",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = 20.sp
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Confidence Circle
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .border(2.dp, Color(0xFF1E293B), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "85%",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    )
                    Text(
                        text = "Confidence",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF64748B)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Breakdown
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Breakdown",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                ConfidenceBar("Data Quality Score", 0.92f, "92/100")
                Spacer(modifier = Modifier.height(20.dp))
                ConfidenceBar("Model Certainty", 0.88f, "88/100")
                Spacer(modifier = Modifier.height(20.dp))
                ConfidenceBar("Feature Reliability", 0.75f, "75/100")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Uncertainty Factor
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFFF8E1).copy(alpha = 0.5f),
                border = BorderStroke(1.dp, Color(0xFFFFE082))
            ) {
                Row(modifier = Modifier.padding(20.dp)) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFFFA000),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Uncertainty Factor",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF795548))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "CBCT scan noise slightly reduced confidence in the bone density calculation.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF8D6E63), lineHeight = 18.sp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController?.navigate("result_summary") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "View Full Results", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ConfidenceBar(label: String, progress: Float, value: String) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B)))
            Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = Color(0xFF4A90E2),
            trackColor = Color(0xFFF1F5F9)
        )
    }
}

@Composable
fun ResultSummaryScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    val patient = viewModel?.currentNewPatient
    val prediction = viewModel?.selectedPrediction
    val probability = prediction?.riskScore?.toInt() ?: 78
    val riskLevel = prediction?.riskCategory ?: "High Risk"
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { navController?.navigate("dashboard") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Group, contentDescription = "Patients") },
                    label = { Text("Patients") },
                    selected = false,
                    onClick = { navController?.navigate("patients") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AutoGraph, contentDescription = "Analysis") },
                    label = { Text("Analysis") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { navController?.navigate("settings") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "Results Summary",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Patient Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${patient?.firstName ?: "Jane"} ${patient?.lastName ?: "Doe"}",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "ID: ${patient?.id ?: "P-2024-089"} • ${patient?.dob?.takeLast(4) ?: "1982"} ${patient?.gender ?: "Female"}",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                            )
                        }
                        Surface(
                            color = if (riskLevel.contains("High", ignoreCase = true)) Color(0xFFFFEBEE) else Color(0xFFE8F1FF),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(
                                text = riskLevel,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (riskLevel.contains("High", ignoreCase = true)) Color(0xFFD32F2F) else Color(0xFF4A90E2),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Surface
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Probability Circle Small
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .border(1.dp, Color(0xFF1E293B), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$probability %",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            SummaryStatItem("Bone Loss", "-1.2mm")
                            SummaryStatItem("Timeframe", "12 mo")
                            SummaryStatItem("Confidence", "85%")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Analysis Tools",
                    modifier = Modifier.align(Alignment.Start),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tools Grid
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        AnalysisToolCard("History", "Compare past", Icons.Default.History, Modifier.weight(1f))
                        AnalysisToolCard("Trends", "View projection", Icons.AutoMirrored.Filled.TrendingUp, Modifier.weight(1f), onClick = { navController?.navigate("trend_analysis") })
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        val context = androidx.compose.ui.platform.LocalContext.current
                        AnalysisToolCard("Report", "Generate PDF", Icons.Default.Description, Modifier.weight(1f), onClick = { 
                            exportReportToDownloads(context, prediction, patient)
                        })
                        AnalysisToolCard("Share", "Send to patient", Icons.Default.Hub, Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Recommendation Box
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF4A90E2)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "Clinical Recommendations",
                            style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "3 immediate actions suggested based on this analysis.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.8f))
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { navController?.navigate("recommendations") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "View Treatment Plan", color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val context = androidx.compose.ui.platform.LocalContext.current
                        OutlinedButton(
                            onClick = { 
                                prediction?.let { 
                                    viewModel?.savePrediction(it) {
                                        navController?.navigate("dashboard") {
                                            popUpTo("dashboard") { inclusive = true }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            border = BorderStroke(1.dp, Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Save and Complete", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SummaryStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B)))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
    }
}

@Composable
fun AnalysisToolCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text(text = subtitle, style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B)))
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun RecommendationsScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Recommendations",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = 20.sp
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "AI-generated clinical suggestions based on\nrisk profile and predictive analysis.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B), textAlign = TextAlign.Center),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            RecommendationCard(
                title = "Immediate Intervention",
                desc = "Consider ridge preservation procedure for site #36 to prevent further vertical bone loss.",
                tag = "URGENT",
                tagColor = Color(0xFFD32F2F),
                borderColor = Color(0xFFEF5350),
                onClick = { /* Navigate to details if needed */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RecommendationCard(
                title = "Short-term Management",
                desc = "Increase recall frequency to every 3 months. Monitor diabetic control with PCP.",
                tag = "3 MONTHS",
                tagColor = Color(0xFF795548),
                borderColor = Color(0xFFFFA726),
                onClick = { navController?.navigate("preventive_measures") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RecommendationCard(
                title = "Long-term Goals",
                desc = "Stabilize periodontal status and maintain bone levels within 5% of current baseline.",
                tag = "12 MONTHS",
                tagColor = Color(0xFF2E7D32),
                borderColor = Color(0xFF66BB6A),
                onClick = { /* Navigate to goals if needed */ }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController?.navigate("preventive_measures") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "Preventive Measures", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { navController?.navigate("treatment_planning") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF4A90E2)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4A90E2))
            ) {
                Text(text = "Treatment Planning", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RecommendationCard(
    title: String,
    desc: String,
    tag: String,
    tagColor: Color,
    borderColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(6.dp)
                    .background(borderColor, RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            )
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (tag == "URGENT") Icons.Default.Warning else Icons.Default.Timer,
                            contentDescription = null,
                            tint = borderColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Surface(
                        color = tagColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = tag,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(color = tagColor, fontWeight = FontWeight.Bold)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B), lineHeight = 20.sp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = if (tag == "3 MONTHS") "View preventive plan →" else if (tag == "URGENT") "View procedure details →" else "View details →",
                    style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun PreventiveMeasuresScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Preventive Measures",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = 20.sp
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Select measures to include in the patient's take-home plan.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B))
            )

            Spacer(modifier = Modifier.height(24.dp))

            PreventiveCategory("Oral Hygiene")
            MeasureItem("Use interdental brushes daily", false)
            MeasureItem("Prescription fluoride toothpaste", true)

            Spacer(modifier = Modifier.height(16.dp))

            PreventiveCategory("Dietary")
            MeasureItem("Increase Calcium & Vitamin D intake", false)

            Spacer(modifier = Modifier.height(16.dp))

            PreventiveCategory("Lifestyle")
            MeasureItem("Smoking cessation program referral", false)

            Spacer(modifier = Modifier.height(16.dp))

            PreventiveCategory("Monitoring")
            MeasureItem("3-month periodontal maintenance", true)

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController?.navigate("treatment_planning") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "View Treatment Plan", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun PreventiveCategory(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun MeasureItem(label: String, isChecked: Boolean) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isChecked) Color(0xFFE8F1FF) else Color.White,
        border = if (isChecked) BorderStroke(1.dp, Color(0xFF4A90E2)) else BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isChecked) Color(0xFF4A90E2) else Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                    color = if (isChecked) Color(0xFF1E293B) else Color(0xFF64748B)
                )
            )
        }
    }
}

@Composable
fun TreatmentPlanningScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Treatment Planning",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = 20.sp
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Recommended Procedures",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            )
            Text(
                text = "Based on high resorption risk prediction.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B))
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProcedureCard(
                title = "Ridge Preservation",
                desc = "Bone grafting immediately after extraction to minimize ridge collapse.",
                option = "Option A",
                isSelected = true,
                time = "60 min",
                success = "92% Succ.",
                cost = "$$"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProcedureCard(
                title = "Guided Bone Regeneration",
                desc = "Using barrier membranes to direct bone growth.",
                option = "Option B",
                isSelected = false,
                time = "90 min",
                success = "85% Succ.",
                cost = "$$$"
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController?.navigate("generate_report") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "Generate Full Report", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProcedureCard(
    title: String,
    desc: String,
    option: String,
    isSelected: Boolean,
    time: String,
    success: String,
    cost: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = if (isSelected) BorderStroke(1.dp, Color(0xFF4A90E2)) else null
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(4.dp)
                        .background(Color(0xFF4A90E2))
                )
            }
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Surface(
                        color = if (isSelected) Color(0xFFE8F1FF) else Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = option,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) Color(0xFF4A90E2) else Color(0xFF64748B),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B), lineHeight = 20.sp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProcedureStat(Icons.Default.Timer, time, Modifier.weight(1f))
                    ProcedureStat(Icons.Default.Check, success, Modifier.weight(1f))
                    ProcedureStat(Icons.Default.Bolt, cost, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ProcedureStat(icon: androidx.compose.ui.graphics.vector.ImageVector, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF8FAFC)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B)))
        }
    }
}

@Composable
fun ReportPreviewScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    val patient = viewModel?.currentNewPatient
    val prediction = viewModel?.currentPrediction
    val clinicalData = viewModel?.currentClinicalData
    val probability = prediction?.riskScore?.times(100)?.toInt() ?: 78
    val riskLevel = prediction?.resultsSummary ?: "High Risk"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Report Preview",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = 20.sp
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Simulated Paper Report
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                shape = RoundedCornerShape(4.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AlveoPredict AI",
                            style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date()),
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8))
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Text(
                        text = "Clinical Prediction Report",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Patient: ${patient?.firstName ?: "Jane"} ${patient?.lastName ?: "Doe"}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            Text(text = "ID: ${patient?.id ?: "P-2024-089"}", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B)))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "Risk Assessment", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B)))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFE2E8F0))
                    Text(text = "${riskLevel.uppercase()} ($probability%)", style = MaterialTheme.typography.bodySmall.copy(color = if (riskLevel.contains("High", ignoreCase = true)) Color.Red else Color(0xFF4A90E2), fontWeight = FontWeight.Bold))
                    Text(
                        text = if (riskLevel.contains("High", ignoreCase = true)) 
                            "Patient shows significant indicators for rapid alveolar ridge resorption. Predicted bone loss: 1.2mm / year."
                            else "Patient shows stable indicators. Periodic monitoring recommended.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 10.sp),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "Key Factors", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B)))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFE2E8F0))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        clinicalData?.let { data ->
                            if (data.smokingStatus != "Never") Text(text = "• Smoking (+24%)", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 10.sp))
                            if (data.hasDiabetes) Text(text = "• Diabetes (+18%)", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 10.sp))
                            if (data.hasOsteoporosis) Text(text = "• Osteoporosis (+12%)", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 10.sp))
                        } ?: run {
                            Text(text = "• Baseline demographics", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 10.sp))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "Recommendations", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B)))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = Color(0xFFE2E8F0))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "1. Immediate ridge preservation", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 10.sp))
                        Text(text = "2. 3-month recall interval", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 10.sp))
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    
                    Text(
                        text = "Page 1 of 3",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8)),
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }

        // Bottom Actions
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 16.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { navController?.popBackStack() },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF4A90E2)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4A90E2))
                ) {
                    Text(text = "Edit", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
                val context = androidx.compose.ui.platform.LocalContext.current
                Button(
                    onClick = { 
                        exportReportToDownloads(context, prediction, patient)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
                ) {
                    Text(text = "Export", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun ExportReportScreen(navController: NavController? = null) {
    var selectedFormat by remember { mutableStateOf("PDF Document") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Export Report",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = 20.sp
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Format",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExportFormatCard(
                title = "PDF Document",
                subtitle = "Best for sharing & printing",
                icon = Icons.Default.PictureAsPdf,
                isSelected = selectedFormat == "PDF Document",
                onClick = { selectedFormat = "PDF Document" }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExportFormatCard(
                title = "Word Document",
                subtitle = "Editable format",
                icon = Icons.Default.Description,
                isSelected = selectedFormat == "Word Document",
                onClick = { selectedFormat = "Word Document" }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Share via",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ShareOptionCard(
                    title = "Email",
                    icon = Icons.Default.Email,
                    modifier = Modifier.weight(1f)
                )
                ShareOptionCard(
                    title = "Print",
                    icon = Icons.Default.Print,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ShareOptionCard(
                    title = "Save to Files",
                    icon = Icons.Default.Save,
                    modifier = Modifier.weight(1f)
                )
                ShareOptionCard(
                    title = "More...",
                    icon = Icons.Default.Share,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController?.navigate("patients") {
                    popUpTo("dashboard") { inclusive = false }
                } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Export & View Reports", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ExportFormatCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, if (isSelected) Color(0xFF4A90E2) else Color(0xFFF1F5F9))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (title == "PDF Document") Color(0xFFE53935) else Color(0xFF1E88E5),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                Text(text = subtitle, style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8)))
            }
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4A90E2))
            )
        }
    }
}

@Composable
fun ShareOptionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF1E293B),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            )
        }
    }
}

@Composable
fun GenerateReportScreen(navController: NavController? = null) {
    var selectedType by remember { mutableStateOf("Full Analysis") }
    val includeSections = remember {
        mutableStateListOf(
            "patient Info" to true,
            "prediction Results" to true,
            "risk Analysis" to true,
            "recommendations" to true,
            "graphs" to false
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Generate Report",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = 20.sp
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Report Type",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ReportTypeCard(
                    title = "Full Analysis",
                    icon = Icons.Default.Assignment,
                    isSelected = selectedType == "Full Analysis",
                    onClick = { selectedType = "Full Analysis" },
                    modifier = Modifier.weight(1f)
                )
                ReportTypeCard(
                    title = "Patient Summary",
                    icon = Icons.Default.ContentPaste,
                    isSelected = selectedType == "Patient Summary",
                    onClick = { selectedType = "Patient Summary" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Include Sections",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            )

            Spacer(modifier = Modifier.height(16.dp))

            includeSections.forEachIndexed { index, pair ->
                IncludeSectionItem(
                    label = pair.first,
                    isChecked = pair.second,
                    onCheckedChange = { includeSections[index] = pair.first to it }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController?.navigate("report_preview") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "Generate Preview", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ReportTypeCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, if (isSelected) Color(0xFF4A90E2) else Color(0xFFE2E8F0)),
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color(0xFFE8F1FF) else Color(0xFFF8FAFC)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) Color(0xFF4A90E2) else Color(0xFF94A3B8),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color(0xFF4A90E2) else Color(0xFF64748B)
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun IncludeSectionItem(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) },
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isChecked) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                contentDescription = null,
                tint = if (isChecked) Color(0xFF4A90E2) else Color(0xFFCBD5E1),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF1E293B))
            )
        }
    }
}

@Composable
fun SettingsScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.height(80.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavItem(icon = Icons.Default.Home, label = "Home", onClick = { navController?.navigate("dashboard") })
                    BottomNavItem(icon = Icons.Default.Group, label = "Patients", onClick = { navController?.navigate("patients") })
                    BottomNavItem(icon = Icons.Default.ShowChart, label = "Analysis", onClick = { navController?.navigate("trend_analysis") })
                    BottomNavItem(icon = Icons.Default.Settings, label = "Settings", active = true)
                }
            }
        },
        containerColor = Color(0xFFF8F9FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // General Section
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Column {
                        SettingSwitchItem(
                            icon = Icons.Default.Notifications,
                            label = "Notifications",
                            isChecked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it },
                            iconBg = Color(0xFFE8F1FF),
                            iconTint = Color(0xFF4A90E2)
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F0F5))
                        SettingSwitchItem(
                            icon = Icons.Default.DarkMode,
                            label = "Dark Mode",
                            isChecked = darkModeEnabled,
                            onCheckedChange = { darkModeEnabled = it },
                            iconBg = Color(0xFFF3E5F5),
                            iconTint = Color(0xFF9C27B0)
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F0F5))
                        SettingNavigationItem(
                            icon = Icons.Default.Language,
                            label = "Language",
                            value = "English",
                            iconBg = Color(0xFFE8F5E9),
                            iconTint = Color(0xFF4CAF50)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Data & Privacy",
                    style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Column {
                        SettingNavigationItem(
                            icon = Icons.Default.Shield,
                            label = "Privacy Settings",
                            iconBg = Color(0xFFFFF3E0),
                            iconTint = Color(0xFFFB8C00)
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F0F5))
                        SettingNavigationItem(
                            icon = Icons.Default.Storage,
                            label = "Data Management",
                            iconBg = Color(0xFFE0F2F1),
                            iconTint = Color(0xFF00BFA5)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Support",
                    style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Column {
                        SettingNavigationItem(
                            icon = Icons.Default.HelpOutline,
                            label = "Help & FAQ",
                            iconBg = Color(0xFFFCE4EC),
                            iconTint = Color(0xFFD81B60),
                            onClick = { navController?.navigate("help_faq") }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFF1F0F5))
                        SettingNavigationItem(
                            icon = Icons.Default.Info,
                            label = "About App",
                            iconBg = Color(0xFFF1F5F9),
                            iconTint = Color(0xFF64748B),
                            onClick = { navController?.navigate("about") }
                        )
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxWidth().clickable { 
                        viewModel?.logout()
                        navController?.navigate("signin") {
                            popUpTo("dashboard") { inclusive = true }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFFFEBEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Sign Out", style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFFD32F2F).copy(alpha = 0.5f))
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = "AlveoPredict AI v1.0.2 (Build 482)",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8))
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SettingSwitchItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    iconBg: Color,
    iconTint: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF1E293B)))
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4A90E2)
            )
        )
    }
}

@Composable
fun TrendAnalysisScreen(navController: NavController? = null) {
    var selectedTime by remember { mutableStateOf("1yr") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .statusBarsPadding()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color(0xFF1E293B),
                    modifier = Modifier.size(28.dp)
                )
            }
            Text(
                text = "Trend Analysis",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    fontSize = 20.sp
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Time Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("6mo", "1yr", "2yr", "All").forEach { time ->
                    val isSelected = selectedTime == time
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTime = time },
                        color = if (isSelected) Color.White else Color.Transparent,
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = if (isSelected) 2.dp else 0.dp
                    ) {
                        Text(
                            text = time,
                            modifier = Modifier.padding(vertical = 10.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color(0xFF1E293B) else Color(0xFF64748B)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFFFEBEE).copy(alpha = 0.6f)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingDown, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Bone\nDensity", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F)))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "-12%", style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E293B)))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Accelerated loss detected", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B)))
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFFFFDE7).copy(alpha = 0.8f)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFFFBC02D), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Risk Score", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFFFBC02D)))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "+15%", style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E293B)))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Increasing steadily", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B)))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Parameter Trends",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            )
            Spacer(modifier = Modifier.height(16.dp))

            TrendItem(label = "Probing Depth", value = "+1.2mm", status = "up", color = Color(0xFFEF5350))
            TrendItem(label = "Attachment Level", value = "+0.8mm", status = "up", color = Color(0xFFEF5350))
            TrendItem(label = "Plaque Index", value = "-0.5", status = "down", color = Color(0xFF4CAF50))
            TrendItem(label = "Mobility", value = "Stable", status = "stable", color = Color(0xFF94A3B8))

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController?.navigate("detailed_graphs") },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "View Detailed Graphs", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TrendItem(label: String, value: String, status: String, color: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF1E293B)))
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = when(status) {
                    "up" -> Icons.Default.TrendingUp
                    "down" -> Icons.Default.TrendingDown
                    else -> Icons.Default.HorizontalRule
                },
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = color))
        }
    }

}

@Composable
fun DetailedGraphsScreen(navController: NavController? = null) {
    var selectedTab by remember { mutableStateOf("Bone Loss") }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFC))
    ) {
        // Header
        Box(
            modifier = Modifier.fillMaxWidth().background(Color.White).statusBarsPadding().padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back", tint = Color(0xFF1E293B), modifier = Modifier.size(28.dp))
            }
            Text(text = "Detailed Graphs", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), fontSize = 20.sp), modifier = Modifier.align(Alignment.Center))
        }

        Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
            // Tabs
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                listOf("Bone Loss", "Risk", "Params").forEach { tab ->
                    val isSelected = selectedTab == tab
                    Column(
                        modifier = Modifier.weight(1f).clickable { selectedTab = tab },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = tab, style = MaterialTheme.typography.bodyLarge.copy(color = if (isSelected) Color(0xFF4A90E2) else Color(0xFF94A3B8), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal), modifier = Modifier.padding(vertical = 12.dp))
                        if (isSelected) {
                            Box(modifier = Modifier.fillMaxWidth().height(3.dp).background(Color(0xFF4A90E2), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                        } else {
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFE2E8F0)))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Graph Visualization (Simulated)
            Surface(
                modifier = Modifier.fillMaxWidth().aspectRatio(1.2f),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Box(modifier = Modifier.padding(24.dp)) {
                    IconButton(onClick = {}, modifier = Modifier.align(Alignment.TopEnd).size(32.dp).background(Color(0xFFF1F5F9), CircleShape)) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Zoom", tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                    }
                    
                    // Simple Line Simulation
                    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize().padding(top = 40.dp, bottom = 20.dp)) {
                        val path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(0f, size.height * 0.8f)
                            lineTo(size.width * 0.2f, size.height * 0.75f)
                            lineTo(size.width * 0.4f, size.height * 0.6f)
                            lineTo(size.width * 0.6f, size.height * 0.45f)
                            lineTo(size.width * 0.8f, size.height * 0.25f)
                            lineTo(size.width, size.height * 0.1f)
                        }
                        drawPath(path = path, color = Color(0xFF1E293B), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
                        
                        // Dots
                        val points = listOf(0f to 0.8f, 0.2f to 0.75f, 0.4f to 0.6f, 0.6f to 0.45f, 0.8f to 0.25f, 1f to 0.1f)
                        points.forEach { (x, y) ->
                            drawCircle(color = Color.White, radius = 8f, center = androidx.compose.ui.geometry.Offset(size.width * x, size.height * y))
                            drawCircle(color = Color(0xFF1E293B), radius = 8f, center = androidx.compose.ui.geometry.Offset(size.width * x, size.height * y), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f))
                        }
                    }

                    // Legend
                    Row(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(Color(0xFFEF5350)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Predicted", style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B)))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Historical", style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B)))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Analysis Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFE3F2FD).copy(alpha = 0.5f)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(text = "Analysis", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B)))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Graph indicates an exponential increase in bone resorption rate over the last 6 months, correlating with the onset of uncontrolled diabetes.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF475569), lineHeight = 22.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { navController?.navigate("detailed_metrics") },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "View Raw Metrics", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun DetailedMetricsScreen(navController: NavController? = null) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFC))
    ) {
        // Header
        Box(
            modifier = Modifier.fillMaxWidth().background(Color.White).statusBarsPadding().padding(vertical = 12.dp, horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back", tint = Color(0xFF1E293B), modifier = Modifier.size(28.dp))
            }
            Text(text = "Detailed Metrics", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), fontSize = 20.sp), modifier = Modifier.align(Alignment.Center))
        }

        Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.CloudDownload, contentDescription = null, tint = Color(0xFF4A90E2), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Export CSV", style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF4A90E2), fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Metrics Table
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header Row
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                        Text(text = "Metric", modifier = Modifier.weight(1.5f), style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold))
                        Text(text = "Current", modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold))
                        Text(text = "Prev", modifier = Modifier.weight(1f), textAlign = TextAlign.End, style = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold))
                    }
                    
                    val metrics = listOf(
                        MetricRowData("Alveolar Height", "12.4mm", "13.1mm", true),
                        MetricRowData("Bone Density (HU)", "450", "520", true),
                        MetricRowData("Cortical Thickness", "1.8mm", "1.9mm", false),
                        MetricRowData("Trabecular Space", "0.8mm", "0.6mm", true),
                        MetricRowData("PD Average", "4.2mm", "3.5mm", true),
                        MetricRowData("CAL Average", "5.1mm", "4.8mm", false)
                    )

                    metrics.forEachIndexed { index, data ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp)) {
                            Text(text = data.label, modifier = Modifier.weight(1.5f), style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF1E293B), fontWeight = FontWeight.Medium))
                            Text(text = data.current, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge.copy(color = if (data.isHighlighted) Color(0xFFEF5350) else Color(0xFF1E293B), fontWeight = FontWeight.Bold))
                            Text(text = data.prev, modifier = Modifier.weight(1f), textAlign = TextAlign.End, style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF64748B)))
                        }
                        if (index < metrics.size - 1) {
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF1F5F9)))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = { navController?.navigate("recommendations") },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
            ) {
                Text(text = "Get Recommendations", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun HelpFaqScreen(navController: NavController? = null) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFFF8F9FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "Help & FAQ",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Search Bar
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search for help...", color = Color(0xFF94A3B8)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF94A3B8))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // FAQ Items
                FaqItem(
                    question = "How accurate is the prediction model?",
                    answer = "Our Random Forest model has demonstrated 94.2% accuracy in clinical validation studies. However, it should be used as a support tool, not a replacement for clinical judgment.",
                    initiallyExpanded = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                FaqItem(
                    question = "What file formats are supported for CBCT?",
                    answer = "We currently support standard DICOM files and high-resolution JPEG/PNG exports from CBCT software."
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                FaqItem(
                    question = "Is patient data stored securely?",
                    answer = "Yes, all patient data is encrypted locally and only processed according to HIPAA-compliant protocols."
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                FaqItem(
                    question = "Can I export reports to my practice software?",
                    answer = "Yes, reports can be exported as PDF or CSV files for easy integration with standard practice management systems."
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(32.dp))

                // Contact Support Button
                Button(
                    onClick = { /* Contact Support */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
                ) {
                    Icon(imageVector = Icons.Default.ChatBubble, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Contact Support", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FaqItem(question: String, answer: String, initiallyExpanded: Boolean = false) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = question,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        lineHeight = 22.sp
                    )
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8)
                )
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = answer,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF64748B),
                        lineHeight = 22.sp
                    )
                )
            }
        }
    }
}

@Composable
fun AboutScreen(navController: NavController? = null) {
    Scaffold(
        containerColor = Color(0xFFF8F9FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .statusBarsPadding()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color(0xFF1E293B),
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                // App Logo
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .shadow(elevation = 12.dp, shape = RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF4A90E2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "AlveoPredict AI",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFE8F1FF)
                ) {
                    Text(
                        text = "Version 1.0.2",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color(0xFF4A90E2),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "AlveoPredict is an advanced clinical decision support system designed to predict alveolar ridge resorption risk using machine learning algorithms on patient demographic, clinical, and radiographic data.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF64748B),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Info Cards
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Clinically Validated", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B)))
                            Text(text = "Trained on over 10,000 patient records with 94% accuracy in validation studies.", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B)))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = Color(0xFFFB8C00), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = "Research Backed", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B)))
                            Text(text = "Developed in collaboration with leading periodontal research institutes.", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B)))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // Links
                TextButton(onClick = { /* Terms */ }) {
                    Text(text = "Terms of Service", style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF1E293B), fontWeight = FontWeight.Bold))
                }
                TextButton(onClick = { /* Privacy */ }) {
                    Text(text = "Privacy Policy", style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF1E293B), fontWeight = FontWeight.Bold))
                }
                TextButton(onClick = { /* Support */ }) {
                    Text(text = "Contact Support", style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF1E293B), fontWeight = FontWeight.Bold))
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "© 2024 AlveoPredict Inc. All rights reserved.",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8))
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

data class MetricRowData(val label: String, val current: String, val prev: String, val isHighlighted: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingNavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String? = null,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF1E293B)))
        Spacer(modifier = Modifier.weight(1f))
        if (value != null) {
            Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF94A3B8)), modifier = Modifier.padding(end = 8.dp))
        }
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    BonePredictTheme {
        SettingsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun RiskAssessmentPreview() {
    BonePredictTheme {
        RiskAssessmentScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun RiskExplanationPreview() {
    BonePredictTheme {
        RiskExplanationScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun BoneLossVisualPreview() {
    BonePredictTheme {
        BoneLossVisualScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ConfidenceAnalysisPreview() {
    BonePredictTheme {
        ConfidenceAnalysisScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ResultSummaryPreview() {
    BonePredictTheme {
        ResultSummaryScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendationsPreview() {
    BonePredictTheme {
        RecommendationsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreventiveMeasuresPreview() {
    BonePredictTheme {
        PreventiveMeasuresScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun TreatmentPlanningPreview() {
    BonePredictTheme {
        TreatmentPlanningScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    BonePredictTheme {
        SplashScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    BonePredictTheme {
        WelcomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    BonePredictTheme {
        SignInScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SelectRolePreview() {
    BonePredictTheme {
        SelectRoleScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    BonePredictTheme {
        SignUpScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    BonePredictTheme {
        DashboardScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    BonePredictTheme {
        ProfileScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun AddPatientPreview() {
    BonePredictTheme {
        AddPatientScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PatientsPreview() {
    BonePredictTheme {
        PatientsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DemographicsPreview() {
    BonePredictTheme {
        DemographicsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PeridontalStatusPreview() {
    BonePredictTheme {
        PeridontalStatusScreen()
    }
}


@Composable
fun ForgotPasswordScreen(navController: NavController? = null, viewModel: MainViewModel? = null) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        IconButton(onClick = { navController?.popBackStack() }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Back", tint = Color(0xFF1A1C1E), modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text("Forgot Password", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E)))
        Spacer(modifier = Modifier.height(12.dp))
        Text("Enter your email address to receive a verification code.", style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF74777F)))
        Spacer(modifier = Modifier.height(48.dp))
        Text("Email Address", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1C1E)), modifier = Modifier.padding(bottom = 8.dp))
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color.White) {
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("doctor@hospital.com", color = Color(0xFFC4C7C5)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )
        }

        if (viewModel?.authError != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = viewModel.authError!!,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Red),
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                isLoading = true
                viewModel?.sendOtp(email, onComplete = { isLoading = false }) {
                    navController?.navigate("otp_verification/$email")
                }
            },
            enabled = email.isNotBlank() && !isLoading,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2),
                disabledContainerColor = Color(0xFFE0E0E0)
            )
        ) {
            if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("Continue", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, color = Color.White))
        }
    }
}

@Composable
fun OtpVerificationScreen(navController: NavController? = null, viewModel: MainViewModel? = null, email: String) {
    var otpValues = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    var isLoading by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(59) }

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Back", tint = Color(0xFF1A1C1E), modifier = Modifier.size(32.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Shield Icon with Checkmark
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE6F7F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = Color(0xFF2DC4B6),
                modifier = Modifier.size(40.dp).alpha(0.1f) // Faded shield
            )
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF2DC4B6),
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text("Verify OTP", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E)))
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "We've sent a 6-digit verification code to your\nemail address.",
            style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF74777F)),
            textAlign = TextAlign.Center
        )
        Text(
            text = email,
            style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF4A90E2), fontWeight = FontWeight.Medium),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // 6-box OTP Input
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            otpValues.forEachIndexed { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { newVal ->
                        val cleanVal = newVal.filter { it.isDigit() }.take(1)
                        otpValues[index] = cleanVal
                        if (cleanVal.isNotEmpty() && index < 5) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(0.9f)
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown && 
                                keyEvent.key == Key.Backspace && 
                                otpValues[index].isEmpty() && 
                                index > 0) {
                                focusRequesters[index - 1].requestFocus()
                                true
                            } else {
                                false
                            }
                        },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4A90E2),
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        textAlign = TextAlign.Center, 
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Resend code in ", style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF74777F)))
            Text(
                text = String.format("00:%02d", timeLeft),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
        
        val isOtpComplete = otpValues.all { it.isNotEmpty() }
        
        Button(
            onClick = {
                isLoading = true
                val otpCode = otpValues.joinToString("")
                viewModel?.verifyOtp(email, otpCode, onComplete = { isLoading = false }) {
                    navController?.navigate("reset_password/$email")
                }
            },
            enabled = isOtpComplete && !isLoading,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A90E2),
                disabledContainerColor = Color(0xFF90C2F3).copy(alpha = 0.6f)
            )
        ) {
            Text("Verify & Continue", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, color = Color.White))
        }
    }
}

@Composable
fun ResetPasswordScreen(navController: NavController? = null, viewModel: MainViewModel? = null, email: String) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        IconButton(onClick = { navController?.popBackStack() }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Back", tint = Color(0xFF1A1C1E), modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text("Create New Password", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E)))
        Spacer(modifier = Modifier.height(12.dp))
        Text("Your new password must be different from previous used passwords.", style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF74777F)))
        Spacer(modifier = Modifier.height(48.dp))
        
        Text("New Password", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1C1E)), modifier = Modifier.padding(bottom = 8.dp))
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color.White) {
            TextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                placeholder = { Text("********", color = Color(0xFFC4C7C5)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Confirm Password", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF1A1C1E)), modifier = Modifier.padding(bottom = 8.dp))
        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color.White) {
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("********", color = Color(0xFFC4C7C5)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = {
                isLoading = true
                viewModel?.resetPassword(email, newPassword, onComplete = { isLoading = false }) {
                    // Navigate to sign in after successful reset
                    navController?.navigate("signin") {
                        popUpTo("signin") { inclusive = true }
                    }
                }
            },
            enabled = newPassword.isNotBlank() && newPassword == confirmPassword && !isLoading,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A90E2))
        ) {
            if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("Continue to Create Account", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, color = Color.White))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CBCTPreview() {
    BonePredictTheme {
        CBCTScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun UploadSuccessPreview() {
    BonePredictTheme {
        UploadSuccessScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DataValidationPreview() {
    BonePredictTheme {
        DataValidationScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DemographicFeaturesPreview() {
    BonePredictTheme {
        DemographicFeaturesScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun FeatureSelectionPreview() {
    BonePredictTheme {
        FeatureSelectionScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreprocessingPreview() {
    BonePredictTheme {
        PreprocessingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SelectMLPreview() {
    BonePredictTheme {
        SelectMLScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun RandomForestPreview() {
    BonePredictTheme {
        RandomForestScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun TrainingModelPreview() {
    BonePredictTheme {
        TrainingModelScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PredictionReadyPreview() {
    BonePredictTheme {
        PredictionReadyScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun TrendAnalysisPreview() {
    BonePredictTheme {
        TrendAnalysisScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DetailedGraphsPreview() {
    BonePredictTheme {
        DetailedGraphsScreen()
    }
}

@Composable
fun PatientHistoryScreen(navController: NavController? = null, viewModel: MainViewModel? = null, patientId: String) {
    val patients by viewModel?.patients?.collectAsState() ?: remember { mutableStateOf(emptyList<com.example.bonepredict.data.model.Patient>()) }
    val patient = patients.find { it.id == patientId }

    LaunchedEffect(patientId) {
        if (patient == null) {
            viewModel?.fetchPatients()
        }
    }

    var isLoadingReport by remember { mutableStateOf(false) }
    val authError = viewModel?.authError
    val snackbarHostState = remember { SnackbarHostState() }

    // Show error if it changes
    LaunchedEffect(authError) {
        if (!authError.isNullOrBlank()) {
            snackbarHostState.showSnackbar(authError)
            viewModel?.clearAuthError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF8F9FB)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .statusBarsPadding()
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color(0xFF1E293B),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(
                        text = "Patient History",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            fontSize = 20.sp
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 100.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    if (patient != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    isLoadingReport = true
                                    viewModel?.loadPatientReport(patient.id) {
                                        isLoadingReport = false
                                        navController?.navigate("result_summary")
                                    }
                                },
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Row(
                                modifier = Modifier.padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(64.dp),
                                    shape = CircleShape,
                                    color = Color(0xFFE0E7FF)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        val initials = "${patient.firstName.getOrNull(0) ?: ""}${patient.lastName.getOrNull(0) ?: ""}".uppercase()
                                        Text(
                                            text = initials,
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                color = Color(0xFF4F46E5),
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${patient.firstName} ${patient.lastName}",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E293B)
                                        )
                                    )
                                    Text(
                                        text = "DOB: ${patient.dob}",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B))
                                    )
                                    Text(
                                        text = "Gender: ${patient.gender}",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B))
                                    )
                                    Text(
                                        text = "Contact: ${patient.contactNumber}",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B))
                                    )
                                }

                                if (patient.riskStatus.isNotBlank() && !patient.riskStatus.equals("Pending", ignoreCase = true)) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (patient.riskStatus.contains("High", ignoreCase = true)) Color(0xFFFFEBEE) else Color(0xFFE7F6F1)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (patient.riskStatus.contains("High", ignoreCase = true)) Icons.Default.Warning else Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = if (patient.riskStatus.contains("High", ignoreCase = true)) Color(0xFFEF5350) else Color(0xFF00BFA5),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = patient.riskStatus,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (patient.riskStatus.contains("High", ignoreCase = true)) Color(0xFFEF5350) else Color(0xFF00BFA5)
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Clinical History",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth().height(120.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "No clinical examinations recorded yet.",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF94A3B8)),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF4A90E2))
                        }
                    }
                }
            }

            if (patient != null && !isLoadingReport) {
                Button(
                    onClick = {
                        isLoadingReport = true
                        viewModel?.loadPatientReport(patient.id) {
                            isLoadingReport = false
                            navController?.navigate("result_summary")
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
                ) {
                    Icon(imageVector = Icons.Default.Description, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "View Clinical Report",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
            }

            if (isLoadingReport) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black.copy(alpha = 0.5f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(color = Color(0xFF4A90E2))
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Loading Report...", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}