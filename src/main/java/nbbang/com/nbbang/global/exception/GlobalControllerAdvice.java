package nbbang.com.nbbang.global.exception;


import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.party.controller.ManyPartyResponseMessage;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.GlobalResponseMessage;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;

import static nbbang.com.nbbang.global.response.GlobalResponseMessage.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

/*    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;*/

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotFoundException.class)
    public DefaultResponse illegalExHandle(NotFoundException e) {
        // 사용 예시:Party party = partyRepository.findById(partyId)
        //                       .orElseThrow(() -> new NotFoundException("There is no party"));
        log.error("[exceptionHandle] NotFoundException: ", e);
        return new DefaultResponse(StatusCode.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public DefaultResponse illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] IllegalArgumentException: ", e);
        return new DefaultResponse(StatusCode.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public DefaultResponse userExHandle(UserException e) {
        log.error("[exceptionHandle] UserException: ", e);
        return new DefaultResponse(StatusCode.BAD_REQUEST, e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public DefaultResponse exHandle(Exception e) {
        log.error("[exceptionHandle] Exception: ", e);
        return new DefaultResponse(StatusCode.INTERNAL_SERVER_ERROR, GlobalResponseMessage.INTERNAL_SERVER_ERROR);
    }

    // 위치 정보: NONE, SINCHON, YEONHUI, CHANGCHEON 이외의 값
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalPlaceException.class)
    public DefaultResponse illegalExHandle(IllegalPlaceException e) {
        log.error("[exceptionHandle] IllegalPlaceException: ", GlobalResponseMessage.ILLEGAL_PLACE);
        return new DefaultResponse(StatusCode.BAD_REQUEST, ManyPartyResponseMessage.ILLEGAL_PLACE);
    }
}


/*
    */
/**
     * {@code 400 Bad Request}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.1">HTTP/1.1: Semantics and Content, section 6.5.1</a>
     *//*

    BAD_REQUEST(400,HttpStatus.Series.CLIENT_ERROR, "Bad Request"),
    */
/**
     * {@code 401 Unauthorized}.
     * @see <a href="https://tools.ietf.org/html/rfc7235#section-3.1">HTTP/1.1: Authentication, section 3.1</a>
     *//*

    UNAUTHORIZED(401,HttpStatus.Series.CLIENT_ERROR, "Unauthorized"),
    */
/**
     * {@code 402 Payment Required}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.2">HTTP/1.1: Semantics and Content, section 6.5.2</a>
     *//*

    PAYMENT_REQUIRED(402,HttpStatus.Series.CLIENT_ERROR, "Payment Required"),
    */
/**
     * {@code 403 Forbidden}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.3">HTTP/1.1: Semantics and Content, section 6.5.3</a>
     *//*

    FORBIDDEN(403,HttpStatus.Series.CLIENT_ERROR, "Forbidden"),
    */
/**
     * {@code 404 Not Found}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.4">HTTP/1.1: Semantics and Content, section 6.5.4</a>
     *//*

    NOT_FOUND(404,HttpStatus.Series.CLIENT_ERROR, "Not Found"),
*/
