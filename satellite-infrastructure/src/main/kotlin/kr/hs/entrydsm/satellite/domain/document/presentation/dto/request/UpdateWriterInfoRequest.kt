package kr.hs.entrydsm.satellite.domain.document.presentation.dto.request

import java.util.UUID
import kr.hs.entrydsm.satellite.domain.document.domain.element.WriterInfoElement
import kr.hs.entrydsm.satellite.domain.major.persistence.MajorJpaEntity
import kr.hs.entrydsm.satellite.common.util.RegexUtil
import org.hibernate.validator.constraints.Length
import org.intellij.lang.annotations.Pattern

data class UpdateWriterInfoRequest(

    @field:Length(max = 255)
    val profileImagePath: String,

    val majorId: UUID,

    val email: String,

    @field:Length(min = 1, max = 1)
    @field:Pattern(RegexUtil.NUMBER_EXP)
    val grade: String,

    @field:Length(min = 1, max = 1)
    @field:Pattern(RegexUtil.NUMBER_EXP)
    val classNum: String,

    @field:Length(min = 2, max = 2)
    @field:Pattern(RegexUtil.NUMBER_EXP)
    val number: String

) {
    fun toElement(writer: WriterInfoElement, majorJpaEntity: MajorJpaEntity): WriterInfoElement {
        return WriterInfoElement(
            studentId = writer.studentId,
            name = writer.name,
            profileImagePath = profileImagePath,
            grade = grade,
            classNum = classNum,
            number = number,
            email = email,
            majorId = majorJpaEntity.id,
            majorName = majorJpaEntity.name
        )
    }
}