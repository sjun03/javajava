/*
서브쿼리란?
- SQL문(SELECT로 시작해서 ;로 끝나는 문장) 안에 포함된 또 다른 SQL문
1. SELECT안에 사용 - 스칼라 서브쿼리
2. WHERE절에 사용  - WEERE절 서브쿼리
3. FROM절에 사용 - 인라인 뷰
*/

CREATE TABLE orders(
	order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL, # -> 왜래키(다른 테이블의 PK)
    order_date DATETIME
);

INSERT INTO
	orders
VALUES
	(null, 1, '2024-01-10'),
	(null, 2, '2024-01-12'),
	(null, 1, '2024-01-15'),
	(null, 3, '2024-02-10'),
	(null, 2, '2024-01-11');

SELECT * FROM orders;

# SELECT 안에 사용하는 서브쿼리 - 스칼라 서브쿼리
# 각 상품의 주문 수를 같이 출력

SELECT
	p.product_name,
	p.product_price,
    ( # 이 상품이 orders 테이블에서 몇 개 주문되었는지 계산
		SELECT
			COUNT(*)
        FROM
			orders o
		WHERE
			O.product_id = p.product_id
	) AS 'order_count'
FROM
	product p;
    
# 평균가격을 스칼라 서브쿼리로 조회 - 전체 평균을 각 상품 옆에 출력

SELECT
	product_name,
	product_price,
    (
		SELECT
			AVG(product_price)
		from
			product
    ) AS 'avg_price'
FROM
	product;
    
# WHERE젛에 사용되는 서브쿼리
# 가장 최근에 주문된 상품을 조회

SELECT
	*
FROM
	product
WHERE # 서브쿼리 결과가 반드시 하나여야 한다. (LIMIT = 1)
	product_id = (
    SELECT
		product_id
	FROM
		orders
	ORDER BY
		order_date DESC
	LIMIT 1
);

# 주문이 존재하는 상품만 조회
SELECT
	*
FROM
	product
WHERE
	product_id IN ( # IN 연산자는 매칭되는 값이 여러개일때, 
		SELECT
			product_id
		FROM	
			orders
);

# 주문이 존재하지 않는 상품들만 조회

SELECT
	*
FROM
	product
WHERE
	product_id NOT IN ( # NOT IN을 사용하여 주문된 상품 id에 없는 상품만 필터링
    # 주문된 상품 id 목록 반환
		SELECT
			product_id
		FROM	
			orders
    );
    
# EXISTS 연산자
# 조건을 만족하는 첫번째 행을 발견하면 즉시 true를 반환하고 종료 -> 경우에 따라 빠름
# 주문된 내역이 있는 상품만 조회
SELECT
	*
FROM
	product p
WHERE	
	# 인스턴스의 id(product_id)가 주문테이블에 존재하는지 확인
    EXISTS ( # 행(row)의 존재여부만 판단
		SELECT
			1 # 목적이 존재여부이기 때문에 반환값은 의미가 없다.
		FROM
			orders o 
		WHERE
			p.product_id = o.product_id
);

# 실습) 1. WHERE 서브쿼리를 작성해서 2024년 1월에 주문된 상품들만 조회
SELECT
	product_id AS '상품 Id',
    product_name AS '상품명',
    product_price AS '상품 가격' 
FROM
	product
WHERE
	product_id IN (
		SELECT
			product_id 
		FROM
			orders
		WHERE
			# 1월에 주문 -> 2024-01-01 이상, 2024-02-01 미만
            # BETWEEN을 쓰면 날짜 계산에서 말일 계산이 까다롭다.
			# order_date >= '2024-01-01' AND order_date < '2024-02-01'
            # BERWEEN: '2025-01-01 00:00:00 ~ 2024-01-31 23:59:59' 값을 보겠다.
            # 23:59:59.9999초 -> 누락될 가능성 존재 
            # 날짜 계산시 ~ 이상, ~ 미만을 사용 권장
			order_date LIKE '2024-01%');
            
SELECT
	*
FROM
	product p
WHERE	
    EXISTS ( 
		SELECT
			1 
		FROM
			orders o
		WHERE
			p.product_id =  o.product_id
            AND order_date LIKE '2024-01%'
            # AND o.order_date >= '2024-01-01'
            # AND order_date < '2024-02-01'
);

# FROM절에 사용하는 서브쿼리 - 인라인뷰
# 서브쿼리의 결과를 하나의 테이블로 간주하겠다. (가상테이블)
SELECT
	*
FROM
	(SELECT
		product_id AS '상품 Id',
		product_name AS '상품명',
		product_price AS '상품 가격', 
		CASE
			WHEN product_price <= 30000 then '저가'
			WHEN product_price <= 100000 then '중가'
			ELSE '고가'
		END AS 'price_range'
	FROM
		product) AS view_table # 쿼리결과를 하나의 가상테이블로도 만들 수 있다. -> view(캐싱)
WHERE
	price_range = '중가'; # 인라인 뷰(서브쿼리결과)를 하나의 테이블로 간주하고 WHERE로 필터링
	
