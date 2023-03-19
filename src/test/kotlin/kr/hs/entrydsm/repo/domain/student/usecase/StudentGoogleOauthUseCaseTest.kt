package kr.hs.entrydsm.repo.domain.student.usecase

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kr.hs.entrydsm.repo.common.AnyValueObjectGenerator.anyValueObject
import kr.hs.entrydsm.repo.domain.auth.constant.Authority
import kr.hs.entrydsm.repo.domain.auth.dto.response.TokenResponse
import kr.hs.entrydsm.repo.domain.common.exception.EmailSuffixNotValidException
import kr.hs.entrydsm.repo.domain.student.exception.SignUpRequiredRedirection
import kr.hs.entrydsm.repo.domain.student.persistence.Student
import kr.hs.entrydsm.repo.domain.student.persistence.repository.StudentRepository
import kr.hs.entrydsm.repo.global.security.jwt.JwtGenerator
import kr.hs.entrydsm.repo.global.thirdparty.oauth.GoogleAuth
import kr.hs.entrydsm.repo.global.thirdparty.oauth.GoogleEmail
import kr.hs.entrydsm.repo.global.thirdparty.oauth.dto.response.GoogleAccessTokenResponse
import kr.hs.entrydsm.repo.global.thirdparty.oauth.dto.response.GoogleEmailResponse
import kr.hs.entrydsm.repo.global.thirdparty.oauth.properties.GoogleOauthProperties

internal class StudentGoogleOauthUseCaseTest : DescribeSpec({

    val studentRepository = mockk<StudentRepository>()
    val googleProperties = anyValueObject<GoogleOauthProperties>()
    val googleAuth = mockk<GoogleAuth>()
    val googleEmail = mockk<GoogleEmail>()
    val jwtGenerator = mockk<JwtGenerator>()

    val studentGoogleOauthUseCase = StudentGoogleOauthUseCase(
        studentRepository,
        googleProperties,
        googleAuth,
        googleEmail,
        jwtGenerator
    )

    describe("studentGoogleOauth") {

        val code = "code"
        val googleAccessToken = "google_access_token"
        val email = "email@dsm.hs.kr"
        val student = anyValueObject<Student>(
            "email" to email
        )

        val tokenResponse = kr.hs.entrydsm.repo.domain.auth.dto.response.TokenResponse(
            accessToken = "access_token",
            accessExpiredAt = LocalDateTime.MAX,
            refreshToken = "refresh_token",
            refreshExpiredAt = LocalDateTime.MAX
        )

        context("가입된 유저의 Oauth 코드가 주어지면") {

            every { googleAuth.queryAccessToken(code, any(), any(), any(), any()) } returns GoogleAccessTokenResponse(
                googleAccessToken
            )
            every { googleEmail.getEmail(googleAccessToken, any()) } returns GoogleEmailResponse(email)
            every { studentRepository.findByEmail(email) } returns student
            every { jwtGenerator.generateBothToken(student.id, kr.hs.entrydsm.repo.domain.auth.constant.Authority.STUDENT) } returns tokenResponse

            it("토큰을 반환한다.") {
                val response = studentGoogleOauthUseCase.signUpOrIn(code)
                response shouldBe tokenResponse
            }
        }

        context("가입되지 않은 유저의 Oauth 코드가 주어지면") {

            every { googleAuth.queryAccessToken(code, any(), any(), any(), any()) } returns GoogleAccessTokenResponse(
                googleAccessToken
            )
            every { googleEmail.getEmail(googleAccessToken, any()) } returns GoogleEmailResponse(email)
            every { studentRepository.findByEmail(email) } returns null

            it("SignUpRequiredRedirection를 던진다.") {
                shouldThrow<SignUpRequiredRedirection> {
                    studentGoogleOauthUseCase.signUpOrIn(code)
                }
            }
        }

        context("학교 이메일이 아닌 Oauth 코드가 주어지면") {

            val gmail = "email@gmail.com"

            every { googleAuth.queryAccessToken(code, any(), any(), any(), any()) } returns GoogleAccessTokenResponse(
                googleAccessToken
            )
            every { googleEmail.getEmail(googleAccessToken, any()) } returns GoogleEmailResponse(gmail)

            it("EmailSuffixNotValid 예외를 던진다.") {
                shouldThrow<EmailSuffixNotValidException> {
                    studentGoogleOauthUseCase.signUpOrIn(code)
                }
            }
        }
    }
})