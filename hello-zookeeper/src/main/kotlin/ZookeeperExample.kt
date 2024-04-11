package hello.haha

import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.ZooDefs
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.server.DumbWatcher

fun main() {
    val connectString = "localhost:2181"
    val path = "/services/hello"
    val data = "zookeeper".toByteArray()

    ZooKeeper(connectString, 3000, DumbWatcher()).use { zookeeper ->
        try {
            if (zookeeper.exists(path, false) == null) {
                zookeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT)
                println("Add data to $path")
            } else {
                println("Data already exists!")
            }
        } finally {
            zookeeper.delete(path, -1)
        }
    }
}
