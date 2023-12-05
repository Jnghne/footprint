package project.footprint.api.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.footprint.api.user.dto.request.UserJoinRequest;
import project.footprint.api.user.dto.request.UserLoginRequest;
import project.footprint.api.user.entity.User;
import project.footprint.api.user.repository.UserRepository;
import project.footprint.global.util.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public void join(UserJoinRequest joinRequest) {
        // 1. make joinRequest => User
        User user = UserJoinRequest.toEntity(joinRequest);

        // 2. 유효성 검증 (이미 가입된 유저 여부 , 이미 사용중인 닉네임 여부, 패스워드-패스워드 확인 값이 같은지)

        // 3. 비밀번호 암호화
        user.encryptPassword(passwordEncoder);

        // 4. 가입 처리 (service 단으로 위임처리)
        userRepository.save(user);
    }

    public String login(UserLoginRequest request) {
        String token = "test";

        // email로 사용자 조회
        User findUser = userRepository.findByEmail(request.getEmail());

        if(findUser == null) {
            throw new IllegalArgumentException("이메일이 일치하지 않습니다.");
        }

        // email로 조회한 사용자의 password와 요청의 패스워드가 같은지 확인
        if(!passwordEncoder.matches(request.getPassword(), findUser.getPassword())){
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        };

        return token;
    }
}
