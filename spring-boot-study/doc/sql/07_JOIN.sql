/*
여러 테이블을 좌우로 연결해서 하나의 결과를 만드는 방법
연결하는 역할을 하는게 외래키(FK)
*/

# 주문 + 고객 이름 조회
# INNER JOIN -> 두테이블 모두에서 매칭되는 값이 있는 ROW만 가져온다.
SELECT
	o.order_id,
    o.order_Date,
    o.customer_id,
    c.customer_name
FROM
	orders o
INNER JOIN customers c
		ON o.customer_id = c.customer_id; # 가져오는 데이터(조인) 조건
# ON과 WHERE의 차이
# ON은 조인조건으로 두 테이블을 합쳐서 새로운 가상테이블을 만드는 것
# WHERE은 테이블에 조건을 걸어 필터링

# 주문 + 주문 상세 + 상품이름
SELECT
	o.order_id,
    o.order_date,
	c.customer_id,
    c.customer_name,
    od.quantity,
    p.product_name
FROM
	orders o # orders 기준으로 LEFT JOIN -> 이미 주문한 고객 id들만 가지고 있음
INNER JOIN customers c
		ON o.customer_id = c.customer_id
INNER JOIN order_details od
		ON o.order_id = od.order_id
INNER JOIN product p
		ON od.product_id = p.product_id;
        
# LEFT JOIN - 왼쪽 테이블 기준으로 모두 가져오겠다.
# 왼쪽테이블 (FROM A LEFT JOIN B) : "A"
# 왼쪽 테이블 데이터는 다 출력되고, B는 매칭되는 것만 출력

# 고객이 주문을 했는지 여부
# customer LEFT JOIN orders -> 고객들 id가 모두 기재
# orders LEFT JOIN customers -> orders에는 주문한 고객id만 기재

SELECT # customer LEFT JOIN orders -> 전체 고객 기준에서 orders 데이터를 붙히겠다.
	c.customer_id,
	c.customer_name,
	o.order_id,
    o.order_date
FROM
    customers c # customers 테이블 데이터는 모두 포현되어야 한다. (LEFT JOIN)
LEFT JOIN orders o
		ON o.customer_id = c.customer_id;
        
# 실습) 모든 주문에 대해 (orders 테이블을 시작)
# order_id, customer_name, product_name, quantity를 조회

SELECT
	o.order_id,
    c.customer_name,
    p.product_name,
    od.quantity
FROM
	orders o
INNER JOIN customers c
		ON o.customer_id = c.customer_id
INNER JOIN product p
		ON o.product_id = p.product_id
INNER JOIN order_details od
		ON o.order_id = od.order_id;
        
        
INSERT INTO
	customers (customer_name, customer_phone, customer_address)
VALUES
	('권영훈', '010-1111-2222', '울산시 북구');

# 실습) 고객이 주문을 했는지 여부를 파악하고 싶다.
# LEFT JOIN을 하는 이유: INNER JOIN을 하면 주문이 있는 고객만 출력
# LEFT JOIN을 하면 모든 고객들의 데이터가 남아 있음 -> 주문 없음 상태를 출력 가능(NULL)
SELECT
	*
FROM
	customers c
LEFT JOIN orders o
	ON c.customer_id = o.customer_id
WHERE # order_id가 NULL인 경우 주문을 안했다.
	o.order_id IS NULL;

# 고객(FROM custromer)별 총 주문 금액(상품 금액 * 갯수)을 집계
# 고객 -> 주문 -> 주문상세(갯수) -> 상품(상품 가격)
SELECT
	c.customer_id,
    c.customer_name,
    SUM(p.product_price * od.quantity) AS 'total_order_price'
FROM 
	customers c
INNER JOIN orders o
	ON c.customer_id = o.customer_id
INNER JOIN order_details od
	ON o.order_id = od.order_id
INNER JOIN product p
	ON od.product_id = p.product_id
GROUP BY 
	c.customer_id, c.customer_name;
    
# 상품(FROM product)별 주문(order_detail)수 출력
# product LEFT JOIN order_detail -> 주문이 0번인 상품도 함께 보여야 하기 때문
SELECT
	p.product_id, 
    p.product_name,
    COUNT(od.order_id) AS 'order_count' # order_detail 한 ROW가 한번의 주문횟수를 의미
    # COUNT(*) -> NULL ROW도 카운팅한다.
    # COUNT(컬럼) -> NULL ROW는 카운팀 안한다.
FROM
	product p
LEFT JOIN order_details od
	ON p.product_id = od.product_id
GROUP BY
	p.product_id, 
    p.product_name;

# 실습) 주문이 한번도 없는 상품을 조회 

SELECT
	product_name
FROM
	product p
LEFT JOIN order_details od # od의 ROW 1개는 주문 1개에 해당
	ON p.product_id = od.product_id # JOIN 결과 오른쪽 테이블이 NULL이라는 것, 주문 0건
WHERE
	order_detail_id IS NULL; # NULL과 비교는 반드시 IS NULL, IS NOT NULL
    
# 실습) 가장 많이 팔린 상품을 조회
# FROM product, 팔린 갯수 정보 order_details.quantity
# INNER JOIN -> product INNER JOIN order_details 와 order_details INNER JOIN product의 결과는 동일하다.
SELECT
	p.product_id AS '상품번호',
	p.product_name AS '상품명',
    SUM(od.quantity) AS '판매량'
FROM
	product p
INNER JOIN order_details od
	ON od.product_id = p.product_id
GROUP BY
	p.product_id,
	p.product_name,
    od.quantity
ORDER BY 
	quantity DESC
LIMIT 1;

SELECT
    p.product_id AS '상품번호',
    p.product_name AS '상품명',
    SUM(od.quantity) AS '판매량'
FROM
    product p
INNER JOIN order_details od
    ON od.product_id = p.product_id
GROUP BY
    p.product_id,
    p.product_name
ORDER BY 
    SUM(od.quantity) DESC
LIMIT 1;

# 외래키 제약을 걸 수 있다.
# 1. RESTRICT: 부모 테이블의 행이 삭제되거나 수정될 때,
# 자식 테이블에 외래키가 존재하면 작업을 허용하지 않겠다. -> 권장
# Ex) order_id가 1인 ROW가 orders 테이블에서 삭제될 때, order_details에서 order_id가 1인 ROW가 있다면, 삭제를 제한한다.
# -> order_details에서 먼저 삭제하고, orders에 있는 ROW를 삭제해야한다.

# 2. CASCADE: 부모가 삭제, 업데이트 되면, 자식 외래키 값도 자동으로 삭제, 업데이트
# Ex) order_id가 1인 ROW가 orders 테이블에서 삭제되면, order_details의 order_id가 1인 ROW들도 삭제해라. -> 최후의 수단
# 서버에 주석으로 CASCADE 내용을 문서화하는것이 중요하다.

# 3. SET NULL: 부모가 삭제, 업데이트되면, 자식 외래키 값을 NULL로 바꾼다.
# Ex) order_id가 1인 ROW가 orders 테이블에서 삭제되면, order_details의 order_id가 1인 것들이 NULL로 값이 변경된다. (자식테이블에 NOT NULL이 아닌 경우)
