package xyz.divineugorji.workmanagerdemoone

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import xyz.divineugorji.workmanagerdemoone.MainActivity
import java.lang.Exception

class CompressingWorker(context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {


    override fun doWork(): Result {

        try {
            val count: Int = inputData.getInt(MainActivity.KEY_CONSTANT_VALUE, 0)

            for (i: Int in 0..300){
                val TAG = "MYTAG"
                Log.i(TAG,"Compressing $i")
            }
            return Result.success()
        }catch (e: Exception){
            return Result.failure()
        }
    }
}