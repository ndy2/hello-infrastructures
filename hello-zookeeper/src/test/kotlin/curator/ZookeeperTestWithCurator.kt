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

class ZookeeperTestWithCurator : FunSpec({

    val server = TestingServer()
    val port = server.port
    val connectString = "localhost:$port"
    val zk = ZooKeeper(connectString, 3000, DumbWatcher())

    beforeSpec {
        println("Starting ZookeeperTestWithCurator")
        println("connectString : $connectString")
    }

    test("[CreateMode.CONTAINER] create 를 통해 ZNode 를 만들고 exists 를 통해 확인 할 수 있다.") {
        // given
        val createMode = CreateMode.CONTAINER

        // when
        zk.create("/some_null", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode)

        // then
        zk.exists("/some_null", false) shouldNotBe null
    }

    test("[CreateMode.CONTAINER] Container 는 자식 노드가 아예 없으면 제거 되지 않는다.") {
        // given
        val createMode = CreateMode.CONTAINER

        // when
        zk.create("/some_container", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode)

        // then
        continually(30.seconds) {
            zk.exists("/some_container", false) shouldNotBe null
        }
    }

    test("[CreateMode.CONTAINER] Container 는 마지막 자식 노드가 제거 되면 삭제 대상이 된다.") {
        // given
        val createMode = CreateMode.CONTAINER

        // when
        zk.create("/other_container", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode)
        zk.create("/other_container/some_child", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT)
        zk.delete("/other_container/some_child", -1)

        // then
        eventually(30.seconds) {
            zk.exists("/other_container", false) shouldBe null
        }
    }
})
