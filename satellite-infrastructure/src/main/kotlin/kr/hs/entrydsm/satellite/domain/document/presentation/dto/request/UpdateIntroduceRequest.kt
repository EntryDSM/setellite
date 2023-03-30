package kr.hs.entrydsm.satellite.domain.document.presentation.dto.request

import javax.validation.constraints.NotEmpty
import kr.hs.entrydsm.satellite.domain.document.domain.element.IntroduceElement
import org.hibernate.validator.constraints.Length

data class UpdateIntroduceRequest(

    @field:Length(max = 100)
    @field:NotEmpty
    val heading: String,

    @field:NotEmpty
    val introduce: String
) {
    fun toIntroduceElement() = IntroduceElement(
        heading = heading,
        introduce = introduce
    )
}