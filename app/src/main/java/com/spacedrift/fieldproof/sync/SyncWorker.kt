package com.spacedrift.fieldproof.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.spacedrift.fieldproof.data.repository.SyncRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncRepository: SyncRepository
) : CoroutineWorker(
    context,
    workerParams
) {

    override suspend fun doWork(): Result {

        val operations =
            syncRepository.getQueuedOperations()

        if (operations.isEmpty()) {
            return Result.success()
        }

        for (operation in operations) {

            try {

                syncRepository.markUploading(operation)

                /*
                 * Backend upload will be connected here.
                 *
                 * For now we deliberately do NOT mark the
                 * operation as synced because there is no
                 * server upload yet.
                 */

                throw IllegalStateException(
                    "Backend sync is not connected yet"
                )

            } catch (exception: Exception) {

                syncRepository.markFailed(
                    operation = operation,
                    error = exception.message
                        ?: "Unknown sync error"
                )
            }
        }

        return Result.success()
    }
}