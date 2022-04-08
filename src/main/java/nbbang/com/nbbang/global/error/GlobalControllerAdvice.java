package nbbang.com.nbbang.global.error;


import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.error.exception.*;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Hidden
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(value = NotFoundException.class)
    public ErrorResponse illegalExHandle(NotFoundException e) {
        // 사용 예시:Party party = partyRepository.findById(partyId)
        //                       .orElseThrow(() -> new NotFoundException("There is no party"));
        log.error("[ExceptionHandle] NotFoundException: ", e);
        return new ErrorResponse(StatusCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ErrorResponse maxFileSizeExHandle(MaxUploadSizeExceededException e) {
        log.error("[ExceptionHandle] MaxUploadSizeExceededException: ", e);
        return new ErrorResponse(StatusCode.BAD_REQUEST, GlobalErrorResponseMessage.MAX_FILE_SIZE_ERROR);
    }


    @ExceptionHandler(CustomIllegalArgumentException.class)
    public ErrorResponse illegalExHandle(CustomIllegalArgumentException e) {
        log.error("[ExceptionHandle] CustomIllegalArgumentException: ", e);
        BindingResult bindingResult = e.getBindingResult();
        List<FieldErrorInfo> fieldErrorInfo = new ArrayList<>();
        bindingResult.getFieldErrors().stream()
                .forEach(error-> fieldErrorInfo.add(new FieldErrorInfo(error.getField(), error.getDefaultMessage())));
        return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage(), fieldErrorInfo);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse illegalExHandle(IllegalArgumentException e) {
        log.error("[ExceptionHandle] IllegalArgumentException: ", e);
        return new ErrorResponse(StatusCode.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorResponse converterExHandle(HttpRequestMethodNotSupportedException e) {
        log.error("[ExceptionHandle] HttpRequestMethodNotSupportedException: ", e.getMessage());
        return ErrorResponse.res(StatusCode.METHOD_NOT_ALLOWED, GlobalErrorResponseMessage.REQUEST_METHOD_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorResponse notFoundExceptionHandle(NoHandlerFoundException e) {
        log.error("[ExceptionHandle] NoHandlerFoundException: ", e);
        return ErrorResponse.res(StatusCode.NOT_FOUND, GlobalErrorResponseMessage.REQUEST_URL_ERROR);
    }


    @ExceptionHandler(RequestRejectedException.class)
    public ErrorResponse requestRejectionExceptionHandle(RequestRejectedException e) {
        log.error("[ExceptionHandle] requestRejectionExceptionHandle: ", e);
        return ErrorResponse.res(StatusCode.NOT_FOUND, GlobalErrorResponseMessage.REQUEST_URL_ERROR);
    }


    @ExceptionHandler
    public ErrorResponse exHandle(Exception e) {
        log.error("[ExceptionHandle] Exception: ", e);
        return new ErrorResponse(StatusCode.INTERNAL_SERVER_ERROR, GlobalErrorResponseMessage.INTERNAL_SERVER_ERROR);
    }

   // @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse exHandle(MethodArgumentTypeMismatchException e) {
        log.error("[ExceptionHandle] Exception: ", e);
        return new ErrorResponse(StatusCode.INTERNAL_SERVER_ERROR, GlobalErrorResponseMessage.ILLEGAL_TYPE_CONVERSION_ERROR);
    }

    // @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotOwnerException.class)
    public ErrorResponse exHandle(NotOwnerException e) {
        log.error("[ExceptionHandle] Exception: ", e);
        return new ErrorResponse(StatusCode.FORBIDDEN, GlobalErrorResponseMessage.NOT_OWNER_ERROR);
    }

    // @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotPartyMemberException.class)
    public ErrorResponse exHandle(NotPartyMemberException e) {
        log.error("[ExceptionHandle] Exception: ", e);
        return new ErrorResponse(StatusCode.FORBIDDEN, GlobalErrorResponseMessage.NOT_PARTY_MEMBER_ERROR);
    }

    // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse exHandle(UnauthorizedException e) {
        log.error("[ExceptionHandle] Exception: ", e);
        return new ErrorResponse(StatusCode.UNAUTHORIZED, GlobalErrorResponseMessage.UNAUTHORIZED_ERROR);
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
