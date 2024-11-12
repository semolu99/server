package systemSecurity.weatherOfaMirror.member.entity

import jakarta.persistence.Entity
import jakarta.persistence.*
import systemSecurity.weatherOfaMirror.core.status.Area
import systemSecurity.weatherOfaMirror.core.status.ROLE
import systemSecurity.weatherOfaMirror.member.dto.MemberDtoResponse
import systemSecurity.weatherOfaMirror.member.dto.MemberMirrorDtoResponse

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "uk_member_login_id", columnNames = ["loginId"])
    ]
)
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 30, updatable = false)
    val loginId: String,

    @Column(nullable = false, length = 200)
    val password: String,

    @Column(nullable = false, length = 10)
    val name: String,

    @Column(nullable = false, length = 30)
    val email: String,

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    val area: Area,
) {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    val memberRole: List<MemberRole>? = null

    fun toDto(): MemberDtoResponse =
        MemberDtoResponse(id!!, loginId, name,email, area.des)
}

@Entity
class MemberRole(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    val role: ROLE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_user_role_member_id"))
    val member: Member,
)

@Entity
class Mirror(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 32)
    val mirrorCode : String,

    @Column(nullable = false, length = 20)
    val mirrorName :String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_mirror_member_id"))
    val member: Member
){
    fun toDto(): MemberMirrorDtoResponse =
        MemberMirrorDtoResponse(id!!, mirrorCode, mirrorName)
}