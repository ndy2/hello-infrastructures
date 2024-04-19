package curator

import io.kotest.assertions.nondeterministic.continually
import io.kotest.assertions.nondeterministic.eventually
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.apache.curator.test.TestingServer
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.ZooDefs
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.server.DumbWatcher
import kotlin.time.Duration.Companion.seconds

class ZookeeperTestWithCurator2 : FunSpec({

    val server = TestingServer()
    val port = server.port
    val connectString = "localhost:$port"
    val zk = ZooKeeper(connectString, 3000, DumbWatcher())

    beforeSpec {
        println("Starting ZookeeperTestWithCurator2")
        println("connectString : $connectString")
    }

    test("[CreateMode.EPHEMERAL] create 를 통해 ZNode 를 만들고 세션을 종료해야지만 ZNode 가 제거된다.") {
        // given
        val createMode = CreateMode.EPHEMERAL

        // when
        zk.create("/some_ephemeral", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode)

        // then
        continually(30.seconds) { zk.exists("/some_ephemeral", false) shouldNotBe null }
        zk.close()

        val zk2 = ZooKeeper(connectString, 3000, DumbWatcher())
        eventually(30.seconds) { zk2.exists("/some_ephemeral", false) shouldBe null }
    }
})
