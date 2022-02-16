package nbbang.com.nbbang.domain.party.controller;

import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.global.exception.IllegalPlaceException;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.response.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;

@Slf4j
@RestControllerAdvice
public class ManyPartyControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalPlaceException.class)
    public DefaultResponse illegalExHandle(IllegalPlaceException e) {
        log.error("[exceptionHandle] IllegalPlaceException: ", e);
        return new DefaultResponse(StatusCode.BAD_REQUEST, ManyPartyResponseMessage.ILLEGAL_PLACE);
    }

}
