# DML - 2. SELECT (조회)
# 테이블 전체 조회
SELECT 
	* # 전체 
FROM 
	product; # 테이블명

# 조건을 걸 수 있다. - WHERE
SELECT 
	*
FROM
	product
WHERE
	product_price > 50000; # JAVA의 stream - filter와 동일

# 연산자 우선순위
/*
1. ()
2. NOT
3. *, /, %
4. +, -
5. 비교연산 =, >, <, >=, =<, BETWEEN, IN, LIKE, IS NULL
6. 논리연산 AND, OR
*/

# IN
SELECT
	product_id, 
    product_name, 
	product_price 
FROM
	product
WHERE
	product_name In ("노트북", "키보드");
# FROM -> WHERE -> SELECT

# SELECT에서 DISTINCT를 걸면, 중복을 제거한 결과를 볼 수 있다.
# 상품명 중복 제거
SELECT DISTINCT
	product_name
FROM
	product;
    
# LIMIT & ORDER BY
SELECT 
	* # *은 실무할 때 사용 x
FROM
	product
ORDER BY
	product_price # 가격 오름차순으로 정렬
LIMIT 
	3; # 상위 3개만 출력
    
SELECT 
	* # *은 실무할 때 사용 x
FROM
	product
ORDER BY
	product_price DESC # 가격 내림차순으로 정렬
LIMIT 
	3; # 상위 3개만 출력
    
# 가격 비싼 순서로 4,5,6 뽑기 - OFFSET
SELECT 
	*
FROM
	product
ORDER BY
	product_price desc
LIMIT
	3
OFFSET
	3; # 3개를 건너뛰고 3개를 조회
# 한 게시판에서 게시글 20개를 보여줄 때 3페이지를 조회한다는 것은
# LIMIT 20 OFFSET 20 * (page - 1) - PAGENATION;

# 가격이 NULL인 상품을 조회 - IS NULL / IS NOT NULL
SELECT
	*
FROM
	product
WHERE
	product_price IS NULL;

SELECT
	*
FROM
	product
WHERE
	product_price IS NOT NULL;
    
# 숫자 범위 검색 - BETWEEN
SELECT
	*
FROM
	product
WHERE
	product_price BETWEEN 10000 AND 50000; # 10000 ~ 50000 범위

# 문자열 패턴검색 - LIKE
SELECT
	*
FROM
	product
WHERE
	product_name LIKE "마%"; # "마"로 시작하는 이름
# 마%: "마"로 시작하는 이름 -> fast(UNIQUE혹은 PRIMARY KEY가 걸려있어야한다.)
# %마: "마"로 끝나느 이름
# %마% : "마"가 포함된 이름
# 박%목% : "박"으로 시작하고, "목"을 포함하는 이름
# 김__: 김으로 시작하고, 3글자 -> fast(UNIQUE혹은 PRIMARY KEY가 걸려있어야한다.)
# 김_: 김으로 시작하고, 2글자
# _화_: 중간이 "화"인 3글자

# 실습1) 북으로 끝나는 상품을 조회해 주세요.
SELECT
	*
FROM
	product
WHERE
	product_name LIKE "%북";
    
# 실습2) 상품 가격이 30000 ~ 100000 사이 상품을 조회해 주세요.
SELECT
	*
FROM
	product
WHERE
	product_price BETWEEN 30000 AND 100000;
    
# 실습3) 50000 이상 상품 중 가격 기준으로 내림차순 조회해 주세요.
SELECT
	*
FROM
	product
WHERE
	product_price >= 50000
ORDER BY
	product_price DESC;
    
# 실습4) 가격이 높은 순으로 5 ~ 8등의 상품이름, 상품가격 조회 중복 제거
SELECT DISTINCT
	product_name,
    product_price
FROM
	product
ORDER BY
	product_price DESC
LIMIT 4 
OFFSET 4; 
# ORDER BY에 들어간 컬럼은 SELECT에 있는 것만 표준 -> MySQL은 허용하지만, DISTINCT를 걸면 반드시 있어야 한다.
# SELECT에서 product_name만 입력하면 가격순 5 ~ 8등을 보장하지 못함.

# 별칭기능 - AS

SELECT
	product_name AS `상품 이름`,
    product_price AS `상품 가격`,
    product_price * 1.1 AS `부가세 포함` # 부가세를 포함한 가격
FROM
	product;
    
# 그룹화 - GROUP BY
# CASE 문법
# 저가, 중가, 고가를 임의로 나누고, 각 카테고리에 상품들이 몇개씩 있는가?

# 집계함수
SELECT
	COUNT(*) # product에 등록된 상품의 전체 갯수(ROWs의 수)
    # COUNT(*) -> NULL도 카운팅한다.
    # COUNT(컬럼) -> 해당 컬럼의 값이 NULL이면 카운팅 안한다.
FROM
	product;

SELECT
	AVG(product_price) AS '상품가격 평균'# 평균
    # MAX(product_price) -> 상품가격 중 최대 가격
    # MIN(product_price) -> 상품가격 중 최소 가격
FROM
	product;

SELECT
	# CASE문(조건문)
    CASE # if문 시작
		WHEN product_price <= 50000 THEN '저가'
		WHEN product_price <= 100000 THEN '중가'
		ELSE '고가'
	END AS 'price_range', # if문 종료
	COUNT(*) AS 'counting'
FROM
	product
GROUP BY
	price_range # price_range의 결과가 같은 것들을 하나로 묶겠다.
HAVING
	COUNT(*) >= 3; # 각 그룹의 count 결과가 3이상인 것들만 -> 그룹조건

/*
SELECT 전체 실행 순서
: ROW(가로줄)를 먼저 제거하는게 우선순위가 높다.
SELCT, WHERE, FROM, GROUP BY, HAVING, ORDER BY
1. FROM: 어떤 테이블에서 데이터를 가져올지 결정
2. WHERE: 가져온 데이터 중 조건을 만족하는 ROW만 남김
3. GROUP BY: 행을 그룹으로 묶음
4. HAVING: 그룹 조건 -> 조건을 만족하는 그룹만 남김
5. SELECT: 어떤 컬럼(세로줄)을 출력할건지 결정
6. ORDER BY: 출력 순서 조정
7. LIMIT: 몇 개까지 출력할지 제한
*/

# 실습) CASE문과 GROUP BY를 사용하여 저가, 중가, 고가별 평균가격을 구해주세요.
SELECT
	CASE
		WHEN product_price <= 50000 THEN '저가'
		WHEN product_price <= 100000 THEN '중가'
		ELSE '고가'
        END AS 'price_range',
        AVG(product_price) AS '상품별 평균가격'
FROM
	product
GROUP BY
	price_range;