package algo_arena.common.exception.enums;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    // TODO ErrorType을 추가해주세요.
    // Token (1xxxx)
    TOKEN_NOT_EXIST(BAD_REQUEST, 10001, "token not exist", "JWT Token 이 존재하지 않습니다."),
    INVALID_TOKEN(BAD_REQUEST, 10002, "invalid token", "유효하지 않은 JWT Token 입니다."),
    ACCESS_TOKEN_EXPIRED(BAD_REQUEST, 10003, "access token expired", "만료된 Access Token 입니다."),
    ENCRYPTION_FAILURE(BAD_REQUEST, 10004, "encryption failure", "JWT Token 암호화에 실패하였습니다."),
    DECRYPTION_FAILURE(BAD_REQUEST,10005,"decryption failure", "JWT Token 복호화에 실패하였습니다."),

    // Member, Auth (2xxxx)
    MEMBER_NOT_FOUND(NOT_FOUND, 20001, "invalid access: member not found", "사용자를 찾을 수 없습니다."),
    NEED_TO_MEMBER_LOGIN(UNAUTHORIZED, 20002, "invalid access: need to authorization of member", "사용자 로그인이 필요합니다."),
    LOGIN_FAILED(UNAUTHORIZED, 20003, "login failed", "아이디 또는 비밀번호가 일치하지 않습니다."),
    INVALID_ROLE(FORBIDDEN, 20004, "invalid role", "해당 요청에 대한 권한이 없습니다. 관리자에게 문의하세요."),
    EMAIL_AUTH_NOT_COMPLETED(UNAUTHORIZED, 20005, "invalid email: not authorized", "인증되지 않은 이메일 입니다. 이메일 인증 후 회원가입이 가능합니다."),
    EMAIL_AUTH_NOT_FOUND(NOT_FOUND, 20006, "email auth not found(tried)", "해당 이메일에 대한 인증 요청 내역이 존재하지 않습니다."),
    WRONG_EMAIL_AUTH_CODE(UNAUTHORIZED, 20007, "wrong email auth code", "이메일 인증번호가 일치하지 않습니다."),
    EMAIL_AUTH_TIME_OUT(REQUEST_TIMEOUT, 20008, "email auth time out", "이메일 인증 유효 시간이 초과되었습니다."),
    EMAIL_ALREADY_REGISTERED(CONFLICT, 20009, "invalid email: already registered", "이미 등록된 이메일입니다."),
    EMAIL_NOT_EXIST(BAD_REQUEST, 20010, "invalid email: not exist", "존재하지 않는 이메일입니다. 유효한 이메일 주소를 입력해 주세요."),
    EMAIL_OUT_OF_FORM(BAD_REQUEST, 20011, "invalid email: out of form", "이메일 주소 형식이 올바르지 않습니다. 다시 입력해 주세요."),
    PASSWORD_OUT_OF_FORM(BAD_REQUEST, 20012, "invalid password: out of form", "비밀번호 형식이 올바르지 않습니다. 다시 입력해 주세요."),
    CONFIRM_PASSWORD_MISMATCH(BAD_REQUEST, 20013, "confirm password mismatch", "확인용 비밀번호가 일치하지 않습니다. 다시 입력해 주세요."),
    NAME_OUT_OF_FORM(BAD_REQUEST,20014,"invalid name: out of form", "닉네임 형식이 올바르지 않습니다. 다시 입력해 주세요."),
    NAME_DUPLICATED(CONFLICT, 20015, "invalid name: duplicated", "중복되는 닉네임입니다. 다시 입력해 주세요."),
    EMAIL_SENDING_ERROR(INTERNAL_SERVER_ERROR, 20016, "email sending error", "이메일 인증 관련 전송에 실패했습니다."),

    // Room (3xxxx)
    ROOM_NOT_FOUND(NOT_FOUND, 30001, "room not found", "해당 테스트방을 찾을 수 없습니다."),
    ALREADY_IN_ROOM(CONFLICT, 30002, "invalid access: member already in a room", "이미 해당 테스트방에 참가 중입니다."),
    NOT_IN_ROOM(CONFLICT, 30003, "invalid access: member not in this room", "해당 테스트 방의 참가자가 아니므로, 요청하신 작업을 수행할 권한이 없습니다."),
    ROOM_FULL(FORBIDDEN, 30004, "room entrance denied: full", "해당 테스트방은 정원 초과로 입장이 불가합니다."),
    ROOM_IN_PROGRESS(FORBIDDEN, 30005, "room entrance denied: in progress", "해당 테스트방은 진행중으로 입장이 불가합니다."),

    // Problem (4xxxx)
    PROBLEM_NOT_FOUND(NOT_FOUND, 400001, "problem not found", "해당 문제를 찾을 수 없습니다."),
    PROBLEM_NUMBER_EXCESS(BAD_REQUEST, 40002, "problem number excess", "문제 수가 초과되었습니다."),
    PROBLEM_INFO_MISSING(BAD_REQUEST, 40003, "problem info missing", "문제 정보 입력이 누락되었습니다."),
    PROBLEM_ALREADY_REGISTERED(CONFLICT, 40004, "problem already registered", "문제가 이미 등록되어 있습니다."),
    PROBLEM_REGISTRATION_FAILED(INTERNAL_SERVER_ERROR, 40005, "problem registration failed", "문제 등록에 실패하였습니다."),

     // Submission (5xxxx)
    SUBMISSION_NOT_FOUND(NOT_FOUND, 50001, "submission not found", "코드 제출 이력이 존재하지 않습니다."),
    SUBMISSION_DUPLICATED(CONFLICT, 50002, "submission duplicated", "이미 코드 제출이 완료되었습니다."),
    SUBMISSION_REVIEW_NOT_ALLOWED(FORBIDDEN, 50003, "submission review not allowed", "해당 코드 제출을 열람할 수 없습니다."),
    SUBMISSION_APPROVAL_NOT_ALLOWED(FORBIDDEN, 50004, "submission approval not allowed", "해당 코드 제출에 대한 승인 권한이 없습니다."),
    SUBMISSION_FAILED(INTERNAL_SERVER_ERROR, 50005, "submission failed", "코드 제출에 실패했습니다."),

    // General (9xxxx)
    INVALID_HTTP_METHOD(METHOD_NOT_ALLOWED, 90001, "invalid http method", "잘못된 Http Method 요청입니다."),
    INVALID_VALUE(BAD_REQUEST, 90002, "invalid value", "잘못된 입력값입니다."),
    SERVER_ERROR(INTERNAL_SERVER_ERROR, 90003, "internal server error", "서버 내부에 오류가 발생했습니다."),

    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String detail;
    private final String message;

}