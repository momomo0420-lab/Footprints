package com.example.footprints.work_manager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class LocationUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        //TODO
        return Result.success()
    }
}