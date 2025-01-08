package kr.devkyu.domain.usecaseImpl.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.devkyu.domain.entity.RegisterEntity
import kr.devkyu.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repo: UserRepository
) {

    operator fun invoke(request : JoinUserParam) : Flow<RegisterEntity> = flow {
        runCatching {
            repo.joinUser(request)
        }.onSuccess {
            emit(it)
        }.onFailure { e ->
            e.printStackTrace()
        }
    }

    data class JoinUserParam(
        val uuid : String,
        val nickName : String? = null,
        val birthday: Int,
        val genderTag : String,
        val jobTag : String,
        val deviceToken : String
    )
}