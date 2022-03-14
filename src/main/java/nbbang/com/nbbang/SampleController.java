package nbbang.com.nbbang;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbbang.com.nbbang.domain.chat.service.ChatService;
import net.bytebuddy.pool.TypePool;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


//@Tag(name = "sample", description = "테스트 api")
//@Controller
@RestController
@Slf4j
@RequiredArgsConstructor
public class SampleController {

    private final ChatService chatService;

    @Operation(summary = "샘플 조회.", description = "id 를 이용하여 샘플을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "샘플 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SampleDto.class))),
            @ApiResponse(responseCode = "400", description = "샘플 조회 실패", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/sample/{id}")
    public SampleDto select(
            @Parameter(description = "조회할 Sample 의 id") @PathVariable("id") Long id
    ) {
        if (id == 0L) throw new IllegalStateException("존재하지 않는 샘플입니다.");
        return new SampleDto("BHC 뿌링클 8시", "오늘 저녁 연대서문 뿌링클 먹을 파티 구합니다. 배달비 엔빵 하실분, 사이드 가능입니다.");
    }

    @GetMapping("/test")
    public String hello() {
        chatService.readMessage(70L, 1L);
        return "test success";
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    static class SampleDto {
        private String title;
        private String content;
    }

    @Schema(description = "샘플 생성 폼")
    @Data
    static class PartyForm {

        @Schema(description = "샘플 제목", nullable = false, example = "BHC 뿌링클 오늘 7시", maxLength = 20)
        private String title;

        @Schema(description = "샘플 내용")
        private String content;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity notExistExceptionHandler(IllegalStateException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
