# Clone Ohouse Server Project
Clone coding for Ohouse Web Server

### 목적 및 의도
웹 서버 설계 및 구현 능력 향상, 웹 기술 숙련도 향상  
Rest API를 통한 프론트와 백엔드의 협업 경험

### 개발 기간
22.08 ~ 23.02

### 개발 환경
- JAVA 11
- Springboot 2.7.3
- JPA
- MySQL 8.0
- QueryDSL
- AWS EC2
- AWS RDS
- Swagger UI

### 팀 구성 및 역할
- 정헌 (백엔드)
- 지수 (벡엔드)
- 민아 (프론트)
- 승영 (프론트)

### 핵심 기능
- 상품 검색 및 조회 (정헌)
- 상품 결제 (정헌)
- 사진 게시글 (정헌)
- 회원 가입 (지수)


---
## 설계
### 핵심 요구사항
- 상품은 카테고리를 가짐
- 상품 게시글은 이미지, 제목, 가격으로 구성
- 다양한 조건에 따라 상품 검색 가능


### 전체 구조도

<img width="1838" alt="structure" src="https://user-images.githubusercontent.com/62828810/231026588-c85a5815-a2dc-4c20-8eb6-080678e8fa47.png">

### 구조
다음은 Layer 구조입니다.  
대부분의 경우, 기본 구조는 퍼사드 패턴을 사용한 Service Layer를 포함하는 MVC 구조를 따릅니다. 각 레이어 간에는 DTO를 이용해 데이터를 주고 받습니다.  

![layer](https://user-images.githubusercontent.com/62828810/231026697-5bc2b97b-c88b-433f-a9cd-4b943ec5bf18.svg)


---
### 상품 파트 설계

#### ERD
상품 파트의 ERD 설계  
![ERD drawio](https://user-images.githubusercontent.com/62828810/231026711-00114be2-58cc-48be-92bd-0fdebaeaf2d5.svg)


#### Sequence Diagram
상품 조회 핵심 파트 Sequence Diagram  
가장 중요한 상품 게시글 조회 로직에 대한 시퀸스 다이어그램입니다. 사용자는 GET ‘/store/category’ 를 이용해서 상품 게시글을을 조회합니다.  

![제목 없는 다이어그램 drawio](https://user-images.githubusercontent.com/62828810/231027110-b583d60f-94d2-45fc-b2d4-c3d6b044208a.svg)

Request에는 카테고리 정보와 상품에 대한 조건을 포함합니다. 이 카테고리와 상품 조건을 처리하기 위해 위와 같은 순서를 따릅니다.  
Category Searcher Object는 쿼리 파라미터인 (Category=20_22_20_20) 를 이용해 특정 Type Entity의 Type을 유추합니다.   
이후에 Controller는 Type을 이용해서  Query Condition Object를 만들어 동적 조회에 사용하게 됩니다.  


---
## 구현

### API Document
#### Store 파트 API
http://www.cloneohouse.shop/swagger-ui/index.html?urls.primaryName=store

#### Community 파트 API
http://www.cloneohouse.shop/swagger-ui/index.html

---
## 협업 방법
- 매주 목요일과 일요일 Zoom & Offline Meeting
- Notion
- Github Project 기능 활용
- Branch 전략 지정
- Commit Convention 지정 (Issue)
- Issue를 통한 매주 Scrum 진행

