package kr.hs.entrydsm.satellite.global.security.auth.details

import kr.hs.entrydsm.satellite.domain.auth.domain.Authority
import kr.hs.entrydsm.satellite.domain.student.persistence.StudentJpaEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class StudentDetails(
    val studentJpaEntity: StudentJpaEntity
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(Authority.STUDENT.name))
    }

    override fun getPassword(): String? = null

    override fun getUsername(): String {
        return studentJpaEntity.id.toString()
    }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}