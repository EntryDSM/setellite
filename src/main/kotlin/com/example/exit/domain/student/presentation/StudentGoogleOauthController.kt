package com.example.exit.domain.student.presentation

import com.example.exit.domain.auth.dto.response.GoogleLoginLinkResponse
import com.example.exit.domain.auth.dto.response.TokenResponse
import com.example.exit.domain.student.usecase.StudentGoogleOauthUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/student")
@RestController
class StudentGoogleOauthController(
    private val studentAuthService: StudentGoogleOauthUseCase
) {

    @GetMapping("/google/link")
    fun getGoogleClientId(): GoogleLoginLinkResponse {
        return studentAuthService.getLink()
    }

    @GetMapping("/google/redirect")
    fun studentSingUpOrIn(@RequestParam("code") code: String): TokenResponse {         // Post로 변경하기 TODO
        return studentAuthService.signUpOrIn(code)
    }

}