/*
VIEW: 가상 테이블
1. JOIN을 많이 하게 되면, SELECT문이 상당히 길어진다.
2. 매번 똑같은 JOIN을 해야하는 경우가 생겨난다.
-> JOIN 결과를 미리 이름을 지어서 저장할 가상테이블이 필요하다.
진짜 테이블이 아니기 때문에, 실제 데이터를 저장하지 않는다.
*/

# orders + customers + order_details + product 의 INNER JOIN 결과를 VIEW로 저장

CREATE VIEW order_full_view AS
SELECT
	o.order_id,
    o.order_date,
    c.customer_name,
    c.customer_phone,
    c.customer_address,
    od.order_detail_id,
    od.product_id,
    od.quantity,
    p.product_name,
    p.product_price
FROM
	orders o
INNER JOIN customers c ON o.customer_id = c.customer_id
INNER JOIN order_details od ON o.order_id = od.order_id
INNER JOIN product p ON od.product_id = p.product_id;

# VIEW를 테이블처럼 사용 가능하다.
# 쿼리를 저장한 것이다. -> order full view를 SELECT하면, 
SELECT
	*
FROM
	order_full_view;

# view + group by


SELECT
	product_id AS '상품 번호',
	product_name AS '상품명',
    sum(quantity) AS '상품량'
FROM
	order_full_view
GROUP BY
	product_id,
    product_name;