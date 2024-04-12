package hello.haha.watchers

import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher
import java.util.concurrent.locks.ReentrantLock


object HealthHandler : Watcher {
    private val mu = ReentrantLock()
    private var health = Health.Unknown

    override fun process(event: WatchedEvent) {
        mu.lock()
        if (event.state == Watcher.Event.KeeperState.SyncConnected) {
            println("zookeeper is healthy : $event")
            health = Health.Healthy
        } else {
            println("zookeeper is unhealthy : $event")
            health = Health.Unhealthy
        }
        mu.unlock()
    }
}

enum class Health {
    Healthy, Unknown, Unhealthy
}
