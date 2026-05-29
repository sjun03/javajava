/*
Transaction: 여러 SQL문을 하나의 작업으로 묶는 문법
- "송금"이라는 것을 수행할 때, 
1. 나의 계좌에서 돈이 빠져나가야 한다. -> UPDATE
2. 상대방 계좌에서 돈이 들어와야한다. -> UPDATE
3. 내역이 저장되어야한다. -> INSERT
: INSERT, UPDATE, DELETE가 하나의 행위로 묶여 있을 수 있다.
가상 log에 기록을 해서 복원이 가능하게 만들 수 있다,
-> 1,2,3을 수행하는 도중 하나라도 실패하면, 전체 작업을 ROLLBACK할 수 있다.
*/

# orders 삭제하기 전에 참조하는 order_details 테이블의 ROW 먼저 삭제해야함
# -> 하나의 트랜잭션으로 묶을 수 있다.

START TRANSACTION;
# 주문상세(자식테이블) ROW 삭제
DELETE FROM
	order_details
WHERE
	order_id = 1;
    
# 주문(부모테이블) ROW 삭제
DELETE FROM
	orders
WHERE
	order_id = 1;
    
# 저장
COMMIT;

# 실패 시 원복
ROLLBACK;

/*
- 스프링부트에서 트랜잭션 쿼리를 직접 작성해서 전송하는가?
-> 아니다.
어노테이션으로 트랜잭션을 걸어줄 수 있다. (메서드에서)
-> 메서드가 정상적으로 리턴되면 COMMIT,
-> 메서드 실행 중 예외가 나타나면, ROLLBACK
*/