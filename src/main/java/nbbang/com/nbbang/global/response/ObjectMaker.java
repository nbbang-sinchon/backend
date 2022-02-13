package nbbang.com.nbbang.global.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectMaker<T>{ // 얘를 어디에 둘지 고민해보기
    private T oneData;
}

