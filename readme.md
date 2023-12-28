
## Front-end 서버 Back-end 서버

분리된 두 Front-end 서버 Back-end 서버 간에 Back-end 서비스를 CORS 문제 없이 해결 할 수 있도록 Destination Service 구현

/src/org/starj/test/destination/FrontendDestinationService.java 참


### hosts 파일에 아래 두 도메인 추가

```
127.0.0.1       frontend-server.com backend-server.com
```


## J2EE CORS Filter 소스 예제 포함

/src/org/starj/test/cors/MyCorsFilter.java 참조

