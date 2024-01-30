package com.github.spigotbasics.core.storage

import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.google.gson.JsonObject
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.logging.Level

class NamespacedStorage(private val backend: StorageBackend, private val namespace: String) {

    private val logger = BasicsLoggerFactory.getStorageLogger(backend.type, namespace)
    private val futures: MutableList<CompletableFuture<*>> = Collections.synchronizedList(mutableListOf())

    private var isShutdown = false
    private var hasShutdown = false

    fun getJsonObject(user: String): CompletableFuture<JsonObject?> {
        if (hasShutdown) {
            throw IllegalStateException("Storage has been shutdown, not accepting new get requests")
        }
        if (isShutdown) {
            throw IllegalStateException("Storage is shutting down, not accepting new get requests")
        }
        return track(backend.getJsonObject(namespace, user))
    }

    fun setJsonObject(user: String, value: JsonObject?): CompletableFuture<Void?> {
        if (hasShutdown) {
            throw IllegalStateException("Storage has been shutdown, not accepting new set requests")
        }
        return track(backend.setJsonObject(namespace, user, value))
    }

    private fun <T> track(future: CompletableFuture<T>): CompletableFuture<T> {
        futures.add(future)
        future.whenComplete { _, _ -> futures.remove(future) }
        return future
    }

    fun shutdown(timeout: Long, unit: TimeUnit): CompletableFuture<Void?> {
        isShutdown = true
        return CompletableFuture.supplyAsync({
            synchronized(futures) {
                val executorService = Executors.newCachedThreadPool()
                futures.forEach { future ->
                    executorService.submit(Callable<Void?> {
                        future.get(timeout, unit)
                        return@Callable null
                    })
                }
                executorService.shutdown()
                try {
                    executorService.awaitTermination(timeout, unit)
                } catch (e: InterruptedException) {
                    logger.log(Level.WARNING, "Interrupted while waiting for tasks to complete", e)
                }

                futures.forEach { future ->
                    if (!future.isDone) {
                        future.completeExceptionally(RuntimeException("Forced completion due to shutdown"))
                        logger.warning("Forcefully completed future $future due to shutdown")
                    }
                }
                hasShutdown = true
                futures.clear()
            }
            return@supplyAsync null
        }, Executors.newSingleThreadExecutor()) // Execute the shutdown logic in a separate thread
    }

    //    fun shutdown() {
//        synchronized(futures) {
//            futures.forEach { future ->
//                if (!future.isDone) {
//                    // Handle incomplete futures, e.g., by completing them exceptionally
//                    // TODO: Give them some time to complete normally
//                    future.completeExceptionally(RuntimeException("Forced completion due to shutdown"))
//                }
//            }
//            futures.clear()
//        }
//    }
}