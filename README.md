# CinemaSeoul Backend

---
## 폴더 구조
```aidl
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
  + cine-seoul-dummy-account-data.sql
    + dummy 계좌 정보 데이터
    
## 코드
+ GRADE_CODE
  ```aidl
    GENRE_CODE  NAME
    12	      12세 관람가
    15	      15세 관람가
    19	      청소년 관람불가
    AL	      전체 관람가
    UN	      미정
  ```

+ GENRE_CODE
  ```aidl
    GENRE_CODE  NAME
    WA	      전쟁
    TH	      스릴러
    FA	      가족
    AN          애니메이션
    DA          다큐멘터리
    CO	      코미디
    DR	      드라마
    HO	      공포
    CR	      범죄
    AD	      모험
    MY	      미스터리
    FT	      판타지
    AC	      액션
    MU	      뮤지컬
    SF	      SF
    RO	      멜로/로맨스
  ```