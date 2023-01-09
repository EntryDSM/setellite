package kr.hs.entrydsm.exit.domain.document.usecase

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kr.hs.entrydsm.exit.common.afterContainer
import kr.hs.entrydsm.exit.common.getTestDocument
import kr.hs.entrydsm.exit.domain.document.exception.IllegalStatusException
import kr.hs.entrydsm.exit.domain.document.persistence.Document
import kr.hs.entrydsm.exit.domain.document.persistence.enums.Status
import kr.hs.entrydsm.exit.domain.document.persistence.repository.DocumentRepository
import org.springframework.data.repository.findByIdOrNull

internal class ShareDocumentUseCaseTest : DescribeSpec({

    val documentRepository: DocumentRepository = mockk()

    val shareDocumentUseCase = ShareDocumentUseCase(documentRepository)

    describe("shareDocument") {

        val document = getTestDocument(status = Status.SUBMITTED)

        context("SUBMITTED 상태인 문서의 id가 주어지면") {

            val slot = slot<Document>()

            every { documentRepository.findByIdOrNull(document.id) } returns document
            every { documentRepository.save(capture(slot)) } returnsArgument 0

            it("해당 문서를 SHARED 상태로 변경하여 저장한다.") {

                shareDocumentUseCase.execute(document.id)

                verify(exactly = 1) { documentRepository.save(slot.captured) }
                slot.captured.status shouldBe Status.SHARED
            }
        }

        val createdDocument = getTestDocument(status = Status.CREATED)

        context("CREATED 상태인 문서의 id가 주어지면") {

            every { documentRepository.findByIdOrNull(createdDocument.id) } returns createdDocument

            it("IllegalStatus 예외를 던진다.") {

                shouldThrow<IllegalStatusException> {
                    shareDocumentUseCase.execute(createdDocument.id)
                }
                verify(exactly = 0) { documentRepository.save(any()) }
            }
        }

        val sharedDocument = getTestDocument(status = Status.SHARED)

        context("SHARED 상태인 문서의 id가 주어지면") {

            every { documentRepository.findByIdOrNull(sharedDocument.id) } returns sharedDocument

            it("IllegalStatus 예외를 던진다.") {

                shouldThrow<IllegalStatusException> {
                    shareDocumentUseCase.execute(sharedDocument.id)
                }
                verify(exactly = 0) { documentRepository.save(any()) }
            }
        }
    }

    afterContainer()
})
