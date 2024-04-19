package hello.haha

import com.datastax.oss.driver.api.core.CqlSession

fun main() {
    val session = CqlSession.builder()
        .withKeyspace("test")
        .withAuthCredentials("cassandra", "MeUhRHK0hv")
        .build()

    session.execute(
        """
            CREATE TABLE IF NOT EXISTS test.members
            (
                id   UUID PRIMARY KEY,
                name TEXT,
                age  INT
            );
    """.trimIndent()
    )

    session.execute(
        """
        INSERT INTO members (id, name, age)
        VALUES (uuid(), 'John', 30);
    """.trimIndent()
    )

    session.execute(
        """
        INSERT INTO members (id, name, age)
        VALUES (uuid(), 'Jane', 28);
    """.trimIndent()
    )

    // 모든 멤버 가져오기 쿼리
    val query = "SELECT * FROM members"

    // 쿼리 실행
    val resultSet = session.execute(query)

    // 결과 처리
    for (row in resultSet) {
        // 각 행에서 데이터 추출
        val memberId = row.getUuid("id")
        val memberName = row.getString("name")
        val memberAge = row.getInt("age")

        // 데이터 사용
        println("Member ID: $memberId, Member Name: $memberName, Member Age : $memberAge")
    }

    // 세션 닫기
    session.close()
}
