package com.nhnacademy.messenger.client.domain.user.event;

import com.nhnacademy.messenger.common.message.data.user.UserInfo;
import java.util.List;

public record UserListSuccessEvent(
        List<UserInfo> userList
) {}
