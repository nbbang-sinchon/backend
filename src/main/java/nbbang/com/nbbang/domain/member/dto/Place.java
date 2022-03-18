package nbbang.com.nbbang.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public enum Place {
    NONE, SINCHON, YEONHUI, CHANGCHEON;

/*    @JsonCreator
    public static Place fromString(String key) {
        for(Place place : Place.values()) {
            if(place.name().equalsIgnoreCase(key)) {
                return place;
            }
        }
        throw new IllegalArgumentException(PLACE_ARGUMENT_ERROR);
        return null;
    }
    // https://blog.naver.com/PostView.naver?blogId=simpolor&logNo=221598980908*/
}
