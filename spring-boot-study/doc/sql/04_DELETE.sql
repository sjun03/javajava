# DELETE
# 가장 주의해야함.

DELETE
FROM
	product
WHERE
	product_id = 2;

DELETE
FROM
	product
WHERE
	product_name in ("스피커", "프린터");
# WHERE절에 IN, LIKE, 비교연산을 작성하게 된다.
# 주의)  절대로 WHERE를 빼고 DELETE를 하지말자
# DELETE FORM product -> product 테이블 전체 삭제

/*
TRUNCATE VS DELETE
- DELETE
1. WHERE절 가능
2. 한 ROW씩 가능
3. 트랜잭션이 가능 -> 복구도 가능하다
4. AUTO_INCREMENT 값을 유지한다.

- TRUNCATE
1. WHERE 안됨
2. 매우 빠름
3. AUTO_INCREMENT 초기화됨.
-> 특정 조건 삭제라면 DELETE, 테이블을 초기화하려면 TRUNCATE

DELETE 안전 가이드
1. WHERE문을 작성
2. PRIMARY_KEY 기주으로 WHERE문을 작성하자
3. SELECT로 확인하자
4. 대량삭제는 반드시 TRANSACTION을 걸어주자
5. FOREIGN KEY가 걸린 테이블은 자식 테이블 먼저 삭제 -> 부모테이블 삭제 or CASCADE를 걸자
*/
# 실습) id가 1인 product의 product_name에 "[HOT]"을 붙힌다.
UPDATE
	product
SET
	product_name = concat("[HOT] ", product_name)
WHERE
	product_id = 1; 
# 실습2) product_name에 "[HOT]"이 포함된 상품을 삭제해 주세요.
DELETE
FROM
	product
WHERE
	product_name LIKE "%[HOT]%";
    
SELECT * FROM product;