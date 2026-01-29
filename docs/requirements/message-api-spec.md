# 메시지 API 문서

## 보안 고려사항

* 모든 요청 메시지에는 서버에서 발급한 `sessionId`가 포함되어야 하며, 이 값은 UUID 형식으로 로그인 성공 시에만 발급됩니다.
* 클라이언트는 로그인 후 발급받은 `sessionId`를 모든 요청의 `header.sessionId`에 포함해야 하며, 서버는 이를 통해 인증 및 권한 검사를 수행합니다.
* `sessionId`는 클라이언트 연결이 끊겨 재접속할 경우에도 동일하게 사용될 수 있으며, 서버는 유효한 세션인지 검증합니다.

* 모든 실패 응답에는 `code`와 `message`가 포함되어 클라이언트가 원인을 정확히 파악할 수 있도록 설계되었습니다.

## 공통 메시지 구조

본 시스템은 TCP 소켓 기반으로 통신하며, 메시지의 경계를 구분하기 위해 **Text-based Length-Prefix** 방식을 사용합니다. 이는 HTTP의 `Content-Length` 방식과 유사하게, 첫 줄에 데이터의 길이를 명시하고 이후 실제 데이터를 전송하는 구조입니다.

모든 메시지는 다음의 구조로 전송됩니다.

1. **Length Line**: 뒤따르는 Payload(JSON 데이터)의 전체 바이트 길이를 `message-length: [길이]` 형식의 문자열로 표현하고 개행 문자(`\n`)로 끝납니다.
2. **Payload**: 실제 전송되는 JSON 데이터.

예시 (바이트 스트림 형태):
```text
message-length: 45
{"header":{"type":"LOGIN", ...},"data":{...}}
```
(첫 줄의 `message-length: 45`와 개행 문자를 읽어 길이를 파악한 후, 이후 45바이트를 Payload로 처리합니다.)

* 서버는 메시지를 수신하면 고유한 long 타입의 `messageId`를 생성하여 응답에 포함합니다. 클라이언트는 이를 통해 메시지 확인이나 추적을 할 수 있습니다.

모든 요청(Request) 메시지는 아래와 같은 구조를 가집니다.

```json
{
  "header": {
    "type": "MESSAGE-TYPE",
    "timestamp": "2024-01-09T12:00:00Z",
    "sessionId": "UUID"
  },
  "data": {}
}
```

모든 응답(Response) 메시지는 아래와 같은 구조를 가집니다. 응답의 성공 여부는 `header.success` 필드로 판단할 수 있습니다.

성공 응답:

```json
{
  "header": {
    "type": "MESSAGE-TYPE-SUCCESS",
    "timestamp": "2024-01-09T12:00:00Z",
    "success": true
  },
  "data": {}
}
```

실패 응답 (모든 실패는 일관된 형식으로 처리되며, code와 message를 포함):

```json
{
  "header": {
    "type": "ERROR",
    "success": false,
    "timestamp": "2024-01-09T12:00:00Z",
    "success": false
  },
  "data": {
    "code": "ERROR_CODE",
    "message": "에러에 대한 설명 메시지"
  }
}
```

> 기본 테스트 계정: userId: "marco", password: "nhnacademy123"

---

## 로그인 관련

### 로그인 (`LOGIN`)

**Request (클라이언트 → 서버):**

```json
{
  "header": {
    "type": "LOGIN",
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "userId": "marco",
    "password": "nhnacademy123"
  }
}
```

**Response (서버 → 클라이언트):**

성공:

```json
{
  "header": {
    "type": "LOGIN-SUCCESS",
    "success": true,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "userId": "marco",
    "sessionId": "550e8400-e29b-41d4-a716-446655440000",
    "message": "Welcome!"
  }
}
```

실패:

```json
{
  "header": {
    "type": "ERROR",
    "success": false,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "code": "AUTH.INVALID_CREDENTIALS",
    "message": "Invalid username or password"
  }
}
```

### 로그아웃 (`LOGOUT`)

**Request (클라이언트 → 서버):**

```json
{
  "header": {
    "type": "LOGOUT",
    "timestamp": "2024-01-09T12:00:00Z",
    "sessionId": "550e8400-e29b-41d4-a716-446655440000"
  }
}
```

**Response (서버 → 클라이언트):**

성공:

```json
{
  "header": {
    "type": "LOGOUT-SUCCESS",
    "success": true,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "message": "로그아웃 되었습니다."
  }
}
```

실패:

```json
{
  "header": {
    "type": "ERROR",
    "success": false,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "code": "AUTH.INVALID_SESSION",
    "message": "유효하지 않은 세션입니다."
  }
}
```

---

## 사용자 관련

### 모든 사용자 목록 (`USER-LIST`)

**Request (클라이언트 → 서버):**

```json
{
  "header": {
    "type": "USER-LIST",
    "timestamp": "2024-01-09T12:00:00Z",
    "sessionId": "550e8400-e29b-41d4-a716-446655440000"
  }
}
```

**Response (서버 → 클라이언트):**

성공:

```json
{
  "header": {
    "type": "USER-LIST-SUCCESS",
    "success": true,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": [
    {
      "id": "alice",
      "name": "앨리스",
      "online": true
    },
    {
      "id": "bob",
      "name": "밥",
      "online": false
    },
    {
      "id": "marco",
      "name": "마르코",
      "online": true
    }
  ]
}
```

실패:

```json
{
  "header": {
    "type": "ERROR",
    "success": false,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "code": "AUTH.UNAUTHORIZED",
    "message": "세션이 유효하지 않거나 권한이 없습니다."
  }
}
```

---

## 채팅 기능

### 채팅 메시지 전송 (`CHAT-MESSAGE`)

> 클라이언트는 채팅방 ID를 기반으로 메시지를 전송하며, 서버는 메시지를 채팅방에 속한 전체 사용자에게 브로드캐스트합니다. 서버는 메시지를 수신하면 고유한 `messageId`(long)로 응답합니다.

**Request (클라이언트 → 서버):**

```json
{
  "header": {
    "type": "CHAT-MESSAGE",
    "timestamp": "2024-01-09T12:00:00Z",
    "sessionId": "550e8400-e29b-41d4-a716-446655440000"
  },
  "data": {
    "roomId": 123456789,
    "message": "Hello everyone!"
  }
}
```

**Response (서버 → 클라이언트):**

성공:

```json
{
  "header": {
    "type": "CHAT-MESSAGE-SUCCESS",
    "success": true,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "roomId": 123456789,
    "messageId": 1122334455
  }
}
```

실패:

```json
{
  "header": {
    "type": "ERROR",
    "success": false,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "code": "ROOM.NOT_FOUND",
    "message": "해당 채팅방을 찾을 수 없습니다."
  }
}
```

---

### 귓속말 전송 (`PRIVATE-MESSAGE`)

> 서버는 귓속말 메시지를 성공적으로 수신하면 고유한 `messageId`(long 타입 숫자 ID)를 발급하여 응답에 포함합니다. 클라이언트는 이 ID를 통해 전송 확인(ACK) 또는 수신 여부 확인 요청이 가능합니다.

**Request (클라이언트 → 서버):**

```json
{
  "header": {
    "type": "PRIVATE-MESSAGE",
    "timestamp": "2024-01-09T12:00:00Z",
    "sessionId": "550e8400-e29b-41d4-a716-446655440000"
  },
  "data": {
    "senderId": "marco",
    "receiverId": "bob",
    "message": "이건 비밀이야."
  }
}
```

**Response (서버 → 클라이언트):**

성공:

```json
{
  "header": {
    "type": "PRIVATE-MESSAGE-SUCCESS",
    "success": true,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "senderId": "marco",
    "receiverId": "bob",
    "message": "귓속말이 전송되었습니다.",
    "messageId": 987654321
  }
}
```

실패:

```json
{
  "header": {
    "type": "ERROR",
    "success": false,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "code": "USER.NOT_FOUND",
    "message": "수신자를 찾을 수 없습니다."
  }
}
```

---

### 채팅방 생성 (`CHAT-ROOM-CREATE`)

**Request (클라이언트 → 서버):**

```json
{
  "header": {
    "type": "CHAT-ROOM-CREATE",
    "timestamp": "2024-01-09T12:00:00Z",
    "sessionId": "550e8400-e29b-41d4-a716-446655440000"
  },
  "data": {
    "roomName": "General"
  }
}
```

**Response (서버 → 클라이언트):**

성공:

```json
{
  "header": {
    "type": "CHAT-ROOM-CREATE-SUCCESS",
    "success": true,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "roomId": 123456789,
    "roomName": "General"
  }
}
```

실패:

```json
{
  "header": {
    "type": "ERROR",
    "success": false,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "code": "ROOM.ALREADY_EXISTS",
    "message": "이미 존재하는 채팅방입니다."
  }
}
```

---

### 채팅방 목록 조회 (`CHAT-ROOM-LIST`)

**Request (클라이언트 → 서버):**

```json
{
  "header": {
    "type": "CHAT-ROOM-LIST",
    "timestamp": "2024-01-09T12:00:00Z",
    "sessionId": "550e8400-e29b-41d4-a716-446655440000"
  },
  "data": {}
}
```

**Response (서버 → 클라이언트):**

성공:

```json
{
  "header": {
    "type": "CHAT-ROOM-LIST-SUCCESS",
    "success": true,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "rooms": [
      {
        "roomId": 123456789,
        "roomName": "General",
        "userCount": 15
      },
      {
        "roomId": 987654321,
        "roomName": "개발자 모임",
        "userCount": 8
      }
    ]
  }
}
```

실패:

```json
{
  "header": {
    "type": "ERROR",
    "success": false,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "code": "AUTH.UNAUTHORIZED",
    "message": "권한이 없습니다."
  }
}
```

---

### 채팅방 입장 (`CHAT-ROOM-ENTER`)

**Request (클라이언트 → 서버):**

```json
{
  "header": {
    "type": "CHAT-ROOM-ENTER",
    "timestamp": "2024-01-09T12:00:00Z",
    "sessionId": "550e8400-e29b-41d4-a716-446655440000"
  },
  "data": {
    "roomId": 123456789
  }
}
```

**Response (서버 → 클라이언트):**

성공:

```json
{
  "header": {
    "type": "CHAT-ROOM-ENTER-SUCCESS",
    "success": true,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "roomId": 123456789,
    "users": ["alice", "bob"]
  }
}
```

실패:

```json
{
  "header": {
    "type": "ERROR",
    "success": false,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "code": "ROOM.NOT_FOUND",
    "message": "채팅방을 찾을 수 없습니다."
  }
}
```

---

### 채팅방 나가기 (`CHAT-ROOM-EXIT`)

**Request (클라이언트 → 서버):**

```json
{
  "header": {
    "type": "CHAT-ROOM-EXIT",
    "timestamp": "2024-01-09T12:00:00Z",
    "sessionId": "550e8400-e29b-41d4-a716-446655440000"
  },
  "data": {
    "roomId": 123456789
  }
}
```

**Response (서버 → 클라이언트):**

성공:

```json
{
  "header": {
    "type": "CHAT-ROOM-EXIT-SUCCESS",
    "success": true,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "roomId": 123456789,
    "message": "채팅방에서 나갔습니다."
  }
}
```

실패:

```json
{
  "header": {
    "type": "ERROR",
    "success": false,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "code": "ROOM.NOT_FOUND",
    "message": "채팅방을 찾을 수 없습니다."
  }
}
```

---

### 메시지 기록 조회 (`CHAT-MESSAGE-HISTORY`)

**Request (클라이언트 → 서버):**

```json
{
  "header": {
    "type": "CHAT-MESSAGE-HISTORY",
    "timestamp": "2024-01-09T12:00:00Z",
    "sessionId": "550e8400-e29b-41d4-a716-446655440000"
  },
  "data": {
    "roomId": 123456789,
    "limit": 50,
    "beforeMessageId": 9876543210
  }
}
```

**Response (서버 → 클라이언트):**

성공:

```json
{
  "header": {
    "type": "CHAT-MESSAGE-HISTORY-SUCCESS",
    "success": true,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "roomId": 123456789,
    "messages": [
      {
        "messageId": 9876543200,
        "senderId": "alice",
        "senderName": "앨리스",
        "timestamp": "2024-01-09T11:50:00Z",
        "content": "안녕하세요 여러분!"
      },
      {
        "messageId": 9876543199,
        "senderId": "bob",
        "senderName": "밥",
        "timestamp": "2024-01-09T11:49:30Z",
        "content": "만나서 반갑습니다."
      }
    ],
    "hasMore": true
  }
}
```

실패:

```json
{
  "header": {
    "type": "ERROR",
    "success": false,
    "timestamp": "2024-01-09T12:00:00Z"
  },
  "data": {
    "code": "ROOM.NOT_FOUND",
    "message": "채팅방을 찾을 수 없습니다."
  }
}
```

---