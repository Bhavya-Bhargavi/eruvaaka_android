package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Model.Response.JobItem
import com.example.myapplication.ViewModel.JobViewModel

@Composable
fun JobsScreen(
    jobViewModel: JobViewModel
) {
    val jobsState by jobViewModel.jobsState.collectAsState()
    val selectedJob by jobViewModel.selectedJob.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        jobViewModel.fetchJobs()
    }

    val defaultJobs = listOf(
        JobItem(
            id = 1,
            title = "Agricultural Field Officer",
            company = "Eruvaaka Agri Tech",
            location = "Hyderabad / Telangana",
            salary = "₹25,000 - ₹35,000 / month",
            job_type = "Full Time",
            description = "Responsible for advising farmers on crop management, soil health, and pest control.",
            qualifications = "B.Sc Agriculture / Horticulture",
            posted_at = "2 days ago"
        ),
        JobItem(
            id = 2,
            title = "Crop Protection Specialist",
            company = "AgriCare Solutions",
            location = "Vijayawada / AP",
            salary = "₹30,000 - ₹45,000 / month",
            job_type = "Full Time",
            description = "Conduct field demonstrations, crop inspections, and recommend bio-pesticide treatments.",
            qualifications = "M.Sc Agriculture / Entomology",
            posted_at = "3 days ago"
        ),
        JobItem(
            id = 3,
            title = "Smart Irrigation Technician",
            company = "GreenDrop Hydro Systems",
            location = "Warangal / Telangana",
            salary = "₹20,000 - ₹28,000 / month",
            job_type = "Full Time",
            description = "Installation, maintenance, and troubleshooting of automated drip irrigation & sensor kits.",
            qualifications = "Diploma / B.Tech Agricultural Engineering",
            posted_at = "5 days ago"
        ),
        JobItem(
            id = 4,
            title = "Organic Farming Advisor",
            company = "Krishi Vikas Producer Co",
            location = "Guntur / AP",
            salary = "₹22,000 - ₹32,000 / month",
            job_type = "Full Time",
            description = "Guide Farmer Producer Organizations (FPOs) on organic certification and sustainable practices.",
            qualifications = "B.Sc Organic Agriculture / Rural Development",
            posted_at = "1 week ago"
        )
    )

    val result = jobsState
    val jobList = if (result?.isSuccess == true) {
        val apiData = result.getOrNull()?.getJobList()
        if (!apiData.isNullOrEmpty()) apiData else defaultJobs
    } else {
        defaultJobs
    }

    if (selectedJob != null) {
        JobDetailView(
            job = selectedJob!!,
            onBack = { jobViewModel.clearSelectedJob() },
            context = context
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Text(
                text = "Agri Jobs & Opportunities",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(jobList.size) { index ->
                    val job = jobList[index]
                    JobCard(
                        job = job,
                        onClick = { jobViewModel.selectJob(job) },
                        context = context
                    )
                }
            }
        }
    }
}

@Composable
fun JobCard(
    job: JobItem,
    onClick: () -> Unit,
    context: Context
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = job.title ?: "Agricultural Job Opening",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1B231B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (!job.job_type.isNullOrBlank()) {
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = job.job_type,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = job.company ?: "Eruvaaka Agri",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            if (!job.location.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = job.location,
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
            }

            if (!job.salary.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = job.salary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            }

            if (!job.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = job.description,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (!job.posted_at.isNullOrBlank()) "Posted: ${job.posted_at}" else "",
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Button(
                    onClick = {
                        if (!job.apply_link.isNullOrBlank()) {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(job.apply_link))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Cannot open apply link", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            onClick()
                        }
                    },
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text(text = "View Details", fontSize = 11.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun JobDetailView(
    job: JobItem,
    onBack: () -> Unit,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
    ) {
        TextButton(onClick = onBack) {
            Text(text = "← Back to Job Openings", fontSize = 14.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = job.title ?: "Agricultural Job Opening",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1B231B)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = job.company ?: "Eruvaaka Agri",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2E7D32)
                )

                if (!job.location.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "📍 Location: ${job.location}", fontSize = 13.sp, color = Color.DarkGray)
                }

                if (!job.salary.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "💰 Salary: ${job.salary}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }

                if (!job.job_type.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "⏰ Job Type: ${job.job_type}", fontSize = 13.sp, color = Color.DarkGray)
                }

                if (!job.qualifications.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Qualifications Required:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(text = job.qualifications, fontSize = 13.sp, color = Color.DarkGray, modifier = Modifier.padding(top = 2.dp))
                }

                if (!job.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Job Description:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(text = job.description, fontSize = 13.sp, color = Color.DarkGray, modifier = Modifier.padding(top = 2.dp))
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (!job.apply_link.isNullOrBlank()) {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(job.apply_link))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Cannot open apply link", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Application submitted for ${job.title}", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text(text = "Apply For This Job", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
