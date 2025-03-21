package algo_arena.common.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {

    /**
     * Common
     */
    INTERNAL_SERVER_ERROR(500, -1, "internal server error"),
    REQUEST_VALIDATION_ERROR(400, -2, "request validation error"),

    /**
     * Email
     */
    EMAIL_SENDING_ERROR(500, 9000, "email sending error"),

    /**
     * JWT
     */
    ENCRYPTION_FAILURE(400, 1040, "encryption failure"),
    DECRYPTION_FAILURE(400,1050,"decryption failure"),

    /**
     * Member, Auth
     */
    INVALID_ACCESS(403, 1000, "invalid access"),

    MEMBER_NOT_FOUND(404, 1001, "invalid access: member not found"),
    NEED_TO_MEMBER_AUTH(401, 1002, "invalid access: need to authorization of member"),
    INVALID_ROLE(403, 1003, "invalid role"),

    EMAIL_AUTH_NOT_COMPLETED(401, 1011, "invalid email: not authorized"),
    EMAIL_AUTH_NOT_FOUND(404, 1012, "email auth not found(tried)"),
    WRONG_EMAIL_AUTH_CODE(400, 1013, "wrong email auth code"),

    EMAIL_ALREADY_REGISTERED(400, 1021, "invalid email: already registered"),
    EMAIL_NOT_REGISTERED(404, 1022, "invalid email: not registered"),
    EMAIL_NOT_EXIST(404, 1023, "invalid email: not exist"),
    EMAIL_OUT_OF_FORM(400, 1024, "invalid email: out of form"),

    PASSWORD_OUT_OF_FORM(400, 1031, "invalid password: out of form"),
    PASSWORD_OUT_OF_LENGTH(400, 1032, "invalid password: out of length"),
    CONFIRM_PASSWORD_MISMATCH(400, 1033, "confirm password mismatch"),
    WRONG_PASSWORD(401, 1034, "wrong password"),

    NAME_OUT_OF_FORM(400,1041,"invalid name: out of form"),
    NAME_OUT_OF_LENGTH(400, 1042, "invalid name: out of length"),
    NAME_DUPLICATED(400, 1043, "invalid name: duplicated"),


    /**
     * Room
     */
    ROOM_NOT_FOUND(404, 2001, "room not found"),
    ALREADY_IN_ROOM(400, 2011, "invalid access: member already in a room"),
    NOT_IN_ROOM(400, 2012, "invalid access: member not in this room"),
    ROOM_FULL(400, 2021, "room entrance denied: full"),
    ROOM_EMPTY(400, 2022, "room empty error"),
    ROOM_CREATION_FAILED(500, 2050, "room creation failed"),

    /**
     * Problem
     */
    PROBLEM_NOT_FOUND(404, 3001, "problem not found"),
    PROBLEM_NUMBER_EXCESS(400, 3011, "problem number excess"),
    PROBLEM_INFO_MISSING(400, 3012, "problem info missing"),
    PROBLEM_ALREADY_REGISTERED(400, 3013, "problem already registered"),
    PROBLEM_REGISTRATION_FAILED(500, 3050, "problem registration failed"),

    /**
     * Submission
     */
    SUBMISSION_NOT_FOUND(404, 4001, "submission not found"),
    SUBMISSION_DUPLICATED(400, 4011, "submission duplicated"),
    SUBMISSION_REVIEW_NOT_ALLOWED(403, 4021, "submission review not allowed"),
    SUBMISSION_APPROVAL_NOT_ALLOWED(403, 4022, "submission approval not allowed"),
    SUBMISSION_FAILED(500, 4050, "submission failed"),

    ;

    private final int httpStatus;
    private final int status;
    private final String content;

}