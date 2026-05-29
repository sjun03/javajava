# UPDATE 기본문법
UPDATE
	product
SET
	product_price = 1600000
WHERE
	product_id = 1;

# WHERE 조건
# WHERE 절 조건에 PRIMARY KEY 조건이 아니면,
# LIMIT 없이 UPDATE하는 경우
# UPDATE가 실행이 되지 않는다. -> 안전업데이트모드
# 참고) 안전업데이트 모드 ON/OFF
SET SQL_SAFE_UPDATES = 0; # 해제
SET SQL_SAFE_UPDATES = 1; # 작동

UPDATE 
	product
SET
	product_price = product_price + 5000
WHERE
	product_price <= 50000;

UPDATE
	product
    # CONCAT(a,b) ->  a 문자열과 b 문자열을 이어붙힌다.
    # DB에서 굳이 태그를 붙혀야 하나? -> 선택사항
    # 서버에서 태그처리를 하는게 좀 더 일반적
SET
	product_name = concat("[NEW] ", product_name)
WHERE
	product_id = 1;

UPDATE
	product
SET
	# SUBSTRING(문자열, INDEX) 7번 인데스부터 끝까지 읽겠다.
	product_name = SUBSTRING(product_name, 7)
WHERE
	product_id = 1;

select * from product;

/*
UPDATE를 할 때는, 반드시 SELECT로 확인하고 어떤 ROW가 업데이트 될지 확인하는게 권장

UPDATE
	product
SET
	product_price = product_price * 1.1
where
	product_name LIKE "%메모리%"

SELECT
	COUNT(*)
FROM
	product
WHERE
	product_name LIKE "%메모리%"
*/
