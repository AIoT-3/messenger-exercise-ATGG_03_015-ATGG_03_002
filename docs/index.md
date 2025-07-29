# 메신저 애플리케이션 요구사항 문서

## 개요

본 문서는 Java와 TCP 소켓 기반의 클라이언트-서버 메신저 애플리케이션의 요구사항을 정의합니다.

## 프로젝트 구조
-  `Maven 멀티 모듈` 구성
- `messenger-common`: 공통 모듈 (메시지 모델, 유틸리티)
- `messenger-server`: 서버 애플리케이션 모듈
- `messenger-client`: 클라이언트 애플리케이션 모듈

## 기술 스택

- Java 21

- Jackson JSON 라이브러리
  - https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
  - https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310
  
- TCP 소켓 통신

## 주요 기능

- 사용자 로그인/로그아웃
- 사용자 목록 조회
- 채팅방 생성 및 입장
- 채팅방 목록 조회
- 채팅방 내 사용자 목록 조회
- 채팅 메시지 전송
- 귓속말 전송
- 파일 전송

---

## 비기능적 요구사항

- **보안**: 인증된 사용자만 접속 허용
- **파일 전송**: 최대 파일 크기 제한 (10MB)

---

## 테스트

- JUnit 기반 단위 테스트

---

## 확장 가능성 고려 사항

- 추가 메시지 타입 대응 방안

---

## 배포

- 서버: 독립 실행형 JAR 배포
  - shade-plugin 활용한 패키징
  - https://maven.apache.org/plugins/maven-shade-plugin/

---