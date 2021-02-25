package xyz.divineugorji.workmanagerdemoone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.work.*
import xyz.divineugorji.workmanagerdemoone.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
companion object{
    const val KEY_CONSTANT_VALUE = "Key_count"
}
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.startButon.setOnClickListener {
          //  setOnetimeWorkRequest()
            setPeriodicWorkRequest()
        }
    }

    private fun setOnetimeWorkRequest(){
        val workManager: WorkManager = WorkManager.getInstance(applicationContext)
        //Sending input/output data to WorkManager
        val data: Data = Data.Builder()
                .putInt(KEY_CONSTANT_VALUE, 125)
                .build()
        //Setting constraints in WorkManager
        val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val uploadRequest: OneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
                .setConstraints(constraints)
                .setInputData(data)
            .build()
        val filteringRequest: OneTimeWorkRequest = OneTimeWorkRequest.Builder(FilteringWorker::class.java)
            .build()
        val compressingRequest: OneTimeWorkRequest = OneTimeWorkRequest.Builder(CompressingWorker::class.java)
            .build()
        val downloadRequest: OneTimeWorkRequest = OneTimeWorkRequest.Builder(DownloadingWorker::class.java)
            .build()
        //Chaining work requests with work manager(parallel)
       val parallelWorks: MutableList<OneTimeWorkRequest> = mutableListOf<OneTimeWorkRequest>()
        parallelWorks.add(downloadRequest)
        parallelWorks.add(filteringRequest)

        //Chaining work requests with work manager(sequential work request)
        workManager
            .beginWith(parallelWorks)
            .then(compressingRequest)
            .then(uploadRequest)
            .enqueue()

        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
                .observe(this, androidx.lifecycle.Observer {
                   binding.textView.text = it.state.name
                    if (it.state.isFinished){
                       val data: Data = it.outputData
                        val message: String? = data.getString(UploadWorker.KEY_WORKER)
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                    }

                })
    }

    private fun setPeriodicWorkRequest(){
        val periodicWorkRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(DownloadingWorker::class.java,16,TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
    }
}