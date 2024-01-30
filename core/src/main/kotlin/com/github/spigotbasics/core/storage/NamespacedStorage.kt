package com.github.spigotbasics.core.storage

import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.google.gson.JsonObject
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.logging.Level

// TODO: add debug logs for get and set methods and whenComplete, print out parameters and time taken
class NamespacedStorage(private val backend: StorageBackend, val namespace: String) {

    private val logger = BasicsLoggerFactory.getStorageLogger(backend.type, namespace)
    private val futures: MutableList<CompletableFuture<*>> = Collections.synchronizedList(mutableListOf())

    private var isShutdown = false
    private var hasShutdown = false

    init {
        try {
            logger.info("Initializing backend storage ...")
            backend.setupNamespace(namespace)
            logger.info("Backend storage initialization complete")
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to setup backend storage, marking as shutdown:", e)
            hasShutdown = true
        }
    }

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

    // TODO: getAndSet method. Probably should take a Function<JsonObject?, JsonObject?> as parameter

    private fun <T> track(future: CompletableFuture<T>): CompletableFuture<T> {
        futures.add(future)
        future.thenRun { futures.remove(future) }
        return future
    }

    fun shutdown(timeout: Long, unit: TimeUnit): CompletableFuture<Void?> {
        isShutdown = true
        logger.info("Shutting down backend storage ...")
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
                logger.info("Backend storage shutdown complete")
                hasShutdown = true
                futures.clear()
            }
            return@supplyAsync null
        }, Executors.newSingleThreadExecutor()) // Execute the shutdown logic in a separate thread
    }
}