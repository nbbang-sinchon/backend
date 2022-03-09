package nbbang.com.nbbang.domain.party.controller;

import nbbang.com.nbbang.domain.party.service.PartyWishlistService;
import nbbang.com.nbbang.global.error.ErrorResponse;
import nbbang.com.nbbang.global.error.exception.UserException;
import nbbang.com.nbbang.global.response.DefaultResponse;
import nbbang.com.nbbang.global.security.SecurityConfig;
import nbbang.com.nbbang.global.support.controller.ControllerTestParent;
import nbbang.com.nbbang.global.support.controller.ControllerTestUtil;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.security.config.BeanIds;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.webjars.NotFoundException;


import static nbbang.com.nbbang.domain.member.controller.MemberResponseMessage.MEMBER_NOT_FOUND;
import static nbbang.com.nbbang.domain.party.controller.PartyResponseMessage.*;
import static nbbang.com.nbbang.global.response.StatusCode.*;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(PartyWishlistController.class)
class PartyWishlistControllerTest extends ControllerTestParent {

    @Autowired private MockMvc mockMvc;
    @Autowired private ControllerTestUtil controllerTestUtil;
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