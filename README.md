# CinemaSeoul Backend

---
## 폴더 구조
```
├─sqls
└─src
    ├─main
    │  ├─java
    │  │  └─uos
    │  │      └─cineseoul
    │  │          ├─config
    │  │          ├─controller
    │  │          │  └─movie
    │  │          ├─dto
    │  │          │  ├─create
    │  │          │  ├─fix
    │  │          │  ├─insert
    │  │          │  ├─response
    │  │          │  └─update
    │  │          ├─entity
    │  │          │  ├─domain
    │  │          │  └─movie
    │  │          ├─exception
    │  │          ├─mapper
    │  │          ├─repository
    │  │          ├─service
    │  │          │  └─movie
    │  │          └─utils
    │  │              └─enums
    │  └─resources
    └─test
        └─java
            └─uos
                └─cineseoul
                    ├─repository
                    └─service

```
+ sqls
  + cine-seoul-dummy-account-data.sql
    + dummy 계좌 정보 데이터
  + cine-seoul-movie-data.sql
    + 영화 정보 데이터
    + 아래 테이블에 데이터 저장
      ```
      ACTOR
      DIRECTOR
      DISTRIBUTOR
      GENRE
      GRADE
      MOVIE
      MOVIE_ACTOR
      MOVIE_DIRECTOR
      MOVIE_GENRE
      ```
  + cine-seoul-screen-seat-data.sql
    + 상영관-좌석 데이터
    
## 코드
+ GRADE_CODE

  | GRADE_CODE | GRADE_NAME |
  |------------|------------|
  | 12	        | 12세 관람가    |
  | 15	        | 15세 관람가    |
  | 18	        | 청소년 관람불가   |
  | AL	        | 전체 관람가     |
  | LS	        | 제한 상영가     |
  | UN	        | 미정         |

+ GENRE_CODE

  |GENRE_CODE| GENRE_NAME |
  |---|------------|
  |01	|드라마|
  |02	|판타지|
  |03	|서부|
  |04	|공포|
  |05	|멜로/로맨스|
  |06	|모험|
  |07	|스릴러|
  |08	|느와르|
  |09	|컬트|
  |10	|다큐멘터리|
  |11	|코미디|
  |12	|가족|
  |13	|미스터리|
  |14	|전쟁|
  |15	|애니메이션|
  |16	|범죄|
  |17	|뮤지컬|
  |18	|SF|
  |19	|액션|
  |20	|무협|
  |21	|에로|
  |22	|서스펜스|
  |23	|서사|
  |24	|블랙코미디|
  |25	|실험|
  |26	|영화카툰|
  |27	|영화음악|
  |28	|영화패러디포스터|

## API 권한
+ 권한 종류

| 권한(역할)  | 이름       | 설명                                           |
|---------|----------|----------------------------------------------|
| A       | ADMIN    | role이 'A'여야만 요청 가능                           |
| M       | MEMBER   | role이 'M'여야만 요청 가능                           |
| N       | NOT MEMBER | role이 'N'여야만 요청 가능                           |
| AUTH    | AUTH     | JWT 토큰이 valid해야 요청 가능 , 실제 회원 role에는 존재하지 않음 |
| P       | PERMIT     | 무조건 허용 , 실제 회원 role에는 존재하지 않음    |

+ API 권한

| API               | 권한      | 비고                 |
|-------------------|---------|--------------------|
| /\*\*/admin/\*\*	 | A       | admin이 들어간 api     |
| /ticket/**        | AUTH | /ticket으로 시작하는 api |
| /payment/**       | AUTH | /payment 시작하는 api  |
| /user/*[0-9]      | AUTH | /user/{num} api    |
| anyRequest       | P | 위 api를 제외한 모든 api  |