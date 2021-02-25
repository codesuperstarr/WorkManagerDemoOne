package xyz.divineugorji.workmanagerdemoone

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class DownloadingWorker(context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {


    override fun doWork(): Result {

        try {
            val count: Int = inputData.getInt(MainActivity.KEY_CONSTANT_VALUE, 0)

            for (i: Int in 0..300){
                val TAG = "MYTAG"
                Log.i(TAG,"Downloading $i")

                val time = SimpleDateFormat("dd/M/yyy hh:mm:ss")
                val currentDate: String = time.format(Date())
                Log.i(TAG, "completed $currentDate")
            }
            return Result.success()
        }catch (e: Exception){
            return Result.failure()
        }
    }
}