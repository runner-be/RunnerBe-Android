# 네이밍 규칙
### 클래스/인터페이스
`PascalCase` (ex: ListAdapter)

### 함수/변수
`camelCase` (ex: getUserData)
- 클래스 프로퍼티의 prefix로 'm'을 사용하지 않는다

### 상수
`UPPER_SNAKE_CASE` (ex: ARG_USER_ID)

### UseCase (Domain 계층)
비즈니스적 관점에서 직관적으로 해석이 가능하도록 네이밍
- {행위} + {대상} + UseCase
- ex: WriteRunningLogUseCase


### Api (Data 계층)
API 구조와 목적을 한눈에 파악할 수 있도록 네이밍
- {Http 메소드} + {행위+대상 or 행위 or 대상} + API
- ex: PostApplyToPostAPI
- ex: PostMessageAPI
