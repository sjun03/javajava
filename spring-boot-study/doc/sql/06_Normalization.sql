CREATE TABLE raw_table (
    order_id INT,
    customer_name VARCHAR(50),
    customer_phone VARCHAR(20),
    customer_address VARCHAR(100),
    product_names VARCHAR(200),
    product_prices VARCHAR(100),
    quantities VARCHAR(50),
    order_date DATETIME
);

INSERT INTO raw_table VALUES
(1, '김철수', '010-1234-5678', '서울시 강남구', '노트북, 마우스', '1500000, 30000', '1, 2', '2025-11-15'),
(2, '이영희', '010-2345-6789', '부산시 해운대구', '키보드', '80000', '1', '2025-11-16'),
(3, '김철수', '010-1234-5678', '서울시 강남구', '모니터', '400000', '1', '2025-11-17');


SELECT
	*
FROM
	raw_table;
# raw_table의 문제점
/*
1. 제1정규형 
- product_names, product_prices
-> 하나의 셀에 여러값이 기록되어 있음
- 검색 불가능: 마우스만 주문한 사람에 대한 쿼리문 작성 불가능
- 집계함수 불가능: SUM, AVG
-> 모든 칼럼에는 단일값이 들어가야한다. : 제1정규형

2. 제2정규형: 부분종속 제거
- PK가 2개 이상의 칼럼으로 이루어져 있을 때, 일부 칼럼에만 종속된 컬럼이 있으면 안된다.
(order_id _ customer_name) -> 식별가능
customer_phone, customer_address -> order_id와 상관이 없음
-> 상품과 주문에 관계가 없다. 오로지 customer_name과 관계가 있다.
해결방안:
고객 정보 테이블 분리
주문 정보 테이블 분리
주문별 상품 정 테이블 분리 

3. 제3정규형: 이행적 종속 제거
- 컬럼은 PK말고 다른 것에 의존하면 안된다.
order_id -> customer_id -> customer_phone, customer_address
customer_phone은 order_id에 이행적으로 종속되어 있다.
- A -> B -> C 일때, A -> B / B -> C로 분리해야한다.
*/

/*
customer TABLE
customer_id PK
customer_name
customer_phone
customer_address
*/

CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(50) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    customer_address VARCHAR(100) NOT NULL
);

/*
orders TABLE
order_id PK
customer_id FK(외래키 - 다른 테이블의 PK)
order_date
*/

/*
product TABLE
상품이름이 같을 수도 있음 -> product_name을 PK로 사용하지 않음
product_id PK
product_names
product_prices
*/

/*
order_detail
order_detail_id PK
order_id FK
product_id FK
quantity
-- 그냥 orders에 quantity 넣으면 안되나?
order_id가 1인 경우, 노트북 1개, 마우스 2개
order_id, customer_id product_id quantity
1			1			1			1 # 노트북 1개
1			1			2			2 # 마우스 2개 : order_id가 PK인데, 중복되게됨
*/

CREATE TABLE order_details (
	order_deorder_detailstail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL, 
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK(quantity > 0)
);

/* # raw_table -> 정규화 (1, 2, 3 정규화) 결과:
orders -> 누가 언제 주문했는가?
order_details -> 무슨 상품을 몇개 주문했는가?

- quantity가 order에 있으며너 안되는 이유: 
1. 실제로 한 주문에 여러 생품이 들어가기 때문
2. order_id가 중복이 되야한다. -> orders 테이블은 불가능하구나
3. order_details 테이블을 만들어서 분리하자

customer -> 고객 정보
product -> 상품 정보 */
 
# raw_table -> 정규화 (1, 2, 3 정규화) 
# 정규화된 테이블을 다시 비정규화된 TABLE을 조회하는 방법
# - JOIN (왜래키를 기준으로 실행한다.)

# 실습) 고객 테이블에 데이터를 넣어주세요(raw_table에 있는 고객 데이터)

INSERT INTO
	customers (customer_name, customer_phone, customer_address)
SELECT
	customer_name,
    customer_phone,
    customer_address
FROM
	raw_table; # SELECT 결과를 그대로 INSERT 하는 방법

# product 데이터 있음
# orders 데이터 있음
# order_detail 더미 데이터 INSERT

INSERT INTO order_details (order_id, product_id, quantity)
VALUES
    (1, 1, 1),  -- 1번 주문: 상품 1번 1개
    (1, 2, 2),  -- 1번 주문: 상품 2번 2개
    (2, 3, 1),  -- 2번 주문: 상품 3번 1개
    (3, 4, 1);  -- 3번 주문: 상품 4번 1개
    
