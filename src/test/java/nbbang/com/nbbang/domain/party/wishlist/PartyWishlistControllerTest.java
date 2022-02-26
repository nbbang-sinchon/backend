package nbbang.com.nbbang.domain.party.wishlist;

import nbbang.com.nbbang.domain.party.controller.PartyWishlistController;
import nbbang.com.nbbang.domain.party.service.PartyWishlistService;
import nbbang.com.nbbang.domain.support.ControllerTestUtil;
import nbbang.com.nbbang.global.error.ErrorResponse;
import nbbang.com.nbbang.global.error.exception.UserException;
import nbbang.com.nbbang.global.interceptor.CurrentMember;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.security.CustomOAuth2MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.webjars.NotFoundException;


import static nbbang.com.nbbang.domain.member.controller.MemberResponseMessage.MEMBER_NOT_FOUND;
import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.*;
import static nbbang.com.nbbang.global.response.StatusCode.*;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(PartyWishlistController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(ControllerTestUtil.class)
class PartyWishlistControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ControllerTestUtil controllerTestUtil;

    @MockBean private CustomOAuth2MemberService customOAuth2MemberService;
    @MockBean private CurrentMember currentMember;
    @MockBean private PartyWishlistService partyWishlistService;

    @Test
    public void postPartyWishlistTest1() throws Exception {
        doNothing().when(partyWishlistService).addWishlistIfNotDuplicate(isA(Long.class), isA(Long.class));
        DefaultResponse res = controllerTestUtil.expectDefaultResponseObject(post("/parties/1/wishlist"));
        assertThat(res.getStatusCode() == OK);
        assertThat(res.getMessage().equals(WISHLIST_ADD_SUCCESS));
    }

    @Test
    public void postPartyWishlistTest2() throws Exception {
        doThrow(new UserException(WISHLIST_DUPLICATE_ADD_ERROR)).when(partyWishlistService).addWishlistIfNotDuplicate(isA(Long.class), isA(Long.class));
        ErrorResponse res = controllerTestUtil.expectErrorResponseObject(post("/parties/1/wishlist"));
        assertThat(res.getStatusCode() == BAD_REQUEST);
        assertThat(res.getMessage().equals(WISHLIST_DUPLICATE_ADD_ERROR));
    }

    @Test
    public void postPartyWishlistTest3() throws Exception {
        doThrow(new NotFoundException(PARTY_NOT_FOUND)).when(partyWishlistService).addWishlistIfNotDuplicate(isA(Long.class), isA(Long.class));
        ErrorResponse res = controllerTestUtil.expectErrorResponseObject(post("/parties/1/wishlist"));
        assertThat(res.getStatusCode() == NOT_FOUND);
        assertThat(res.getMessage().equals(PARTY_NOT_FOUND));
    }

    @Test
    public void postPartyWishlistTest4() throws Exception {
        doThrow(new NotFoundException(MEMBER_NOT_FOUND)).when(partyWishlistService).addWishlistIfNotDuplicate(isA(Long.class), isA(Long.class));
        ErrorResponse res = controllerTestUtil.expectErrorResponseObject(post("/parties/1/wishlist"));
        assertThat(res.getStatusCode() == NOT_FOUND);
        assertThat(res.getMessage().equals(MEMBER_NOT_FOUND));
    }

    @Test
    public void deletePartyWishlistTest1() throws Exception {
        doNothing().when(partyWishlistService).deleteWishlist(isA(Long.class), isA(Long.class));
        DefaultResponse res = controllerTestUtil.expectDefaultResponseObject(post("/parties/1/wishlist"));
        assertThat(res.getStatusCode() == OK);
        assertThat(res.getMessage().equals(WISHLIST_DELETE_SUCCESS));
    }

    @Test
    public void deletePartyWishlistTest2() throws Exception {
        doThrow(new NotFoundException(WISHLIST_NOT_FOUND)).when(partyWishlistService).deleteWishlist(isA(Long.class), isA(Long.class));
        ErrorResponse res = controllerTestUtil.expectErrorResponseObject(delete("/parties/1/wishlist"));
        assertThat(res.getStatusCode() == NOT_FOUND);
        assertThat(res.getMessage().equals(WISHLIST_NOT_FOUND));
    }

}