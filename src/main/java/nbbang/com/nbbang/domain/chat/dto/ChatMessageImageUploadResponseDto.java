package nbbang.com.nbbang.domain.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageImageUploadResponseDto {
    private String imagePath;

    public static ChatMessageImageUploadResponseDto createMock() {
        return ChatMessageImageUploadResponseDto.builder()
                .imagePath("https://filestorage-nbbang.s3.ap-northeast-2.amazonaws.com/profile-images/5a327932-3fa4-443a-8dd4-d0bc41132632.png?response-content-disposition=inline&X-Amz-Security-Token=IQoJb3JpZ2luX2VjEAsaDmFwLW5vcnRoZWFzdC0yIkcwRQIgSfzJikuHYCGMm0dSuzhekiq4Q5NaUzFHalgSXAAtJ8oCIQC%2BsR33vVzSp4M3ooNEPDjyIhgEujWtonpUNZiMdwfuGSrkAghUEAAaDDgzMDYxMzc3ODE4OSIMhmGVbp6mw5eGkUFSKsECrZrJ5RttLEOBlsD8G3ON6KfKx4FR3rzgmtknlEI4hxAkWrDuQXTXC8xJ9FpIu25Y1yIKcqbuqGSzl9yZOROSA5lzASZxBIO4abpUOrNqaKjWMlKXDhYDtXaDuFCTbdF5RiaMS1xAf9Oz2XUHRQER3ea1ifGElcQfkRp43QuPRZ9pbGf3qS9GtI1FIiUbSSkGzV3dlqbex%2Bukr1GZAxRjTa5klrOPslQxyf%2BNdsEd%2Fl0O3H8C39nAyj09qHDH8uc55VqOC6ltaP4lL5JKtUDrNqiMQYLz8jIUB0TObbbLRMRBh1UOWWiRokGPFG074%2Bx32YJXRcupZTbKKWwThIC0KNK6LHro%2FhlCC0kCaEMp6PxEvXzRMKIvaebdZ4MDOSLu9cIAeiKvsGYWH5cnhnSyQWUJY%2FosTWX%2BhB5uJI5peBlJMN%2F0m5AGOrMCro7nm%2B8ShHfntSq03l7G4Av6UZxsqNOmG%2B6oASvud4%2BeMoYuF%2Frbx6NW%2BWFdCFNxWoD8noMWllOW6tVblYp9eFhwQmDHZkqKDU1nRt34jPUI%2FI4GY5ACRfQ37sTNrhGQl0nWY4H4MK3oLUt9hEqD3MvKG0dPyp7TTcxXGdVSqQ7Aa5bAZ51Tx0%2F18tSCtxLCQe3rSo%2Bqux6g2MIcexxekzjsZsQ3QlWbvGHdk2PVggvXp%2BL2i83OLBLZrYJo046j533SkprCiezL745qLFQvIJkn5mLNXe0OzI4Vp9jDpmuxK%2F4jit1PgF09Gs%2Fabs0sIc8BF8WLS8SBS%2B%2FYGqAliBU7x1OYDabULRinU7FfUcFsFcNuLoYoo3BPKou7wtJfOqrWKr6xeajkb7h5%2FdwsacDnOQ%3D%3D&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20220212T030420Z&X-Amz-SignedHeaders=host&X-Amz-Expires=604800&X-Amz-Credential=ASIA4CZDQN4G2O3VJ646%2F20220212%2Fap-northeast-2%2Fs3%2Faws4_request&X-Amz-Signature=5314d87ae3c8c62056e29f58fcf981779a83f20c5ba3b0b6bc076a011405267b")
                .build();
    }
}