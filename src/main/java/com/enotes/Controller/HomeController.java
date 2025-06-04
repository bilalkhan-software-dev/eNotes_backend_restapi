package com.enotes.Controller;


import com.enotes.Exception.ResourceNotFoundException;
import com.enotes.Service.HomeService;
import com.enotes.Util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final HomeService homeService;

    @PutMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam Integer userId, @RequestParam String token) throws ResourceNotFoundException {

        log.info("Home Controller : verifyEmail() Start");
        boolean isVerified = homeService.verifyEmail(userId, token);

        if (isVerified) {
            return CommonUtil.createBuildResponseMessage("Account Verified Successfully!", HttpStatus.CREATED);
        }
        log.info("Home Controller : verifyEmail() End");

        return CommonUtil.createErrorResponseMessage("Account Verification Failed!", HttpStatus.BAD_REQUEST);
    }

}
