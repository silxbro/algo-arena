# 🧨 API 예외 처리

### [API 예외처리의 어려운 점]
- HandlerExceptionResolver의 경우 ModelAndView를 반환하나, 이것은 API 응답에는 필요하지 않다.
- HttpServletResponse에 직접 응답 데이터를 넣어주는 방식은 비효율적이다.
- 특정 컨트롤러에서만 발생하는 예외를 별도로 처리하기 어렵다.

@ExceptionHandler와 @ControllerAdvice를 조합하면 예외를 깔끔하게 해결할 수 있다.
<br/>
<br/>
## [1] @ExceptionHandler
스프링은 API 예외 처리 문제를 해결하기 위해 **@ExceptionHandler**라는 어노테이션을 사용하는 매우 편리한 예외 처리 기능을 제공하는데, 이것이 바로 **ExceptionHandlerExceptionResolver**이다. 스프링은
ExceptionHandlerExceptionResolver를 기본으로 제공하고, 기본으로 제공하는 ExceptionResolver 중 우선순위도 가장 높다. 실무에서 API 예외 처리는 대부분 이 기능을 사용한다.

### [@ExceptionHandler 예외 처리 방법]
@ExceptionHandler 애노테이션을 선언하고, 해당 컨트롤러에서 처리하고 싶은 예외를 지정해주면 된다. 해당 컨트롤러에서 예외가 발생하면 이 메서드가 호출된다.
참고로 지정한 예외 또는 그 예외의 자식 클래스는 모두 잡을 수 있다.

### [우선순위]
스프링의 우선순위는 항상 자세한 것이 우선권을 가진다. 예를 들어서 부모, 자식 클래스가 있고 다음과 같이 예외가 처리된다.

```java
@ExceptionHandler(부모예외.class)
public String 부모예외처리()(부모예외 e) {}

@ExceptionHandler(자식예외.class)
public String 자식예외처리()(자식예외 e) {}
```
@ExceptionHandler 에 지정한 부모 클래스는 자식 클래스까지 처리할 수 있다. 따라서 자식예외 가 발생하면 부모 예외처리(), 자식예외처리() 둘 다 호출 대상이 된다.
그런데 둘 중 더 자세한 것이 우선권을 가지므로 **자식예외처리()** 가 호출된다. 물론 부모예외가 호출되면 부모예외처리()만 호출 대상이 되므로 부모예외처리()가 호출된다.

### [다양한 예외]
다음과 같이 다양한 예외를 한번에 처리할 수 있다.

```java
@ExceptionHandler({AException.class, BException.class})
public String ex(Exception e) {
    log.info("exception e", e);
}
```

### [예외 생략]
@ExceptionHandler에 예외를 생략할 수 있다. 생략하면 메서드 파라미터의 예외가 지정된다.

```java
@ExceptionHandler
public ResponseEntity<ErrorResult> userExHandle(UserException e) {}
```

### [파리미터와 응답]
`@ExceptionHandle에는 마치 스프링의 컨트롤러의 파라미터 응답처럼 다양한 파라미터와 응답을 지정할 수 있다.
자세한 파라미터와 응답은 다음 공식 메뉴얼을 참고하자.

### [예시]

#### ErrorResult
- 예외가 발생했을 때 API 응답으로 사용하는 객체를 정의
```java
package hello.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {
    private String code;
    private String message;
}
```

#### ApiExceptionController
```java
package hello.exception.exhandler;

import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
  
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
```
<br/>

## [2] @ControllerAdvice
@ExceptionHandler를 사용해서 예외를 깔끔하게 처리할 수 있게 되었지만, 정상 코드와 예외 처리 코드가 하나의 컨트롤러에 섞여 있다.
@ControllerAdvice 또는 @RestControllerAdvice를 사용하면 둘을 분리할 수 있다.

- @ControllerAdvice는 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler, @InitBinder 기능을 부여해주는 역할을 한다.
- @ControllerAdvice에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다. (글로벌 적용)
- @RestControllerAdvice는 @ControllerAdvice와 같고, @ResponseBody가 추가되어 있다. @Controller, @RestController의 차이와 같다.

### [대상 컨트롤러 지정 방법]
- [스프링 공식 문서 참고](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-controller-advice)

```
// Target all Controllers annotated with @RestController
@ControllerAdvice(annotations = RestController.class)
public class ExampleAdvice1 {}

// Target all Controllers within specific packages
@ControllerAdvice("org.example.controllers")
public class ExampleAdvice2 {}

// Target all Controllers assignable to specific classes
@ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class})
public class ExampleAdvice3 {}
```
위와 같이 특정 애노테이션이 있는 컨트롤러를 지정할 수 있고, 특정 패키지를 직접 지정 할 수도 있다. 패키지 지정의 경우 해당 패키지와 그 하위에 있는 컨트롤러가 대상이 된다.
그리고 특정 클래스를 지정할 수도 있다. 대상 컨트롤러 지정을 생략하면 모든 컨트롤러에 적용된다.

### [예시]
#### ExControllerAdvice
```java
package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}
```