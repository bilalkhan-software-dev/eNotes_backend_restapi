package com.enotes.Config;

import com.enotes.Entity.User;
import com.enotes.Util.CommonUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditAwareConfig implements AuditorAware<Integer> {


    @Override
    public Optional<Integer> getCurrentAuditor() {
        User user = CommonUtil.GetLoggedInUserDetails();
        Integer loggedInUserId = user.getId();
        return Optional.of(loggedInUserId);
    }
}
