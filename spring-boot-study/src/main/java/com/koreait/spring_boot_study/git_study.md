# git?
- 코드 변경에 따른 버전 관리 툴

# git 명령어
1. git init: 명령어가 실행되는 경로에서 코드 변경을 추적을 시작하겠다.
2. git add .: 변경사항에 대해서 임시 저장 -> 스테이징 영역에 저장한다.
git add 경로/파일이름.java(정석적인 방법)
git add .: 모든 변경사항을 임시 저장하겠다.
3. git commit -m: 이때까지 add한 부분에 대해서 하나의 버전으로 저장한다.
4. git remote add origin 깃허브저장소 url
5. git push -u origin main: origin(원격저장소)와 main(로컬)를 동기화해서 전송하겠다. (최초 1회만 기입, 그 이후에는 git push만 입력)