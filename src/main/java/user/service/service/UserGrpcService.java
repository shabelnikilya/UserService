package user.service.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import user.service.grpc.UserCrudServiceGrpc;
import user.service.grpc.UserService;
import user.service.models.Role;
import user.service.models.User;
import user.service.repository.UserRepository;

import java.util.Optional;

@Log4j
@GRpcService
public class UserGrpcService extends UserCrudServiceGrpc.UserCrudServiceImplBase {
    private final UserRepository repository;

    @Autowired
    public UserGrpcService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void findUserByName(UserService.FindUserByNameRequest request,
                               StreamObserver<UserService.FindUserResponse> responseObserver) {
        log.info("Запрос дошел до метода findUserByName()");

        User findUser = repository.findBySecondName(request.getName());
        if (findUser != null) {
            log.info("Пользователь найден!");

            UserService.User userGrpc = UserService.User.newBuilder()
                    .setId(findUser.getId())
                    .setFirstName(findUser.getFirstName())
                    .setSecondName(findUser.getSecondName())
                    .setPassword(findUser.getPassword())
                    .setAge(findUser.getAge())
                    .setRole(findUser.getRole().toString())
                    .build();
            UserService.FindUserResponse response =
                    UserService.FindUserResponse.newBuilder().setUser(userGrpc).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new IllegalArgumentException("Данный пользователь не найден!"));
        }
    }

    @Override
    public void findUserById(UserService.FindUserByIdRequest request,
                             StreamObserver<UserService.FindUserResponse> responseObserver) {
        log.info("Запрос дошел до метода findUserById()");

        Optional<User> user = repository.findById(request.getId());
        if (user.isPresent()) {
            log.info("Пользователь найден!");

            User findUser = user.get();
            UserService.User userGrpc = UserService.User.newBuilder()
                    .setId(findUser.getId())
                    .setFirstName(findUser.getFirstName())
                    .setSecondName(findUser.getSecondName())
                    .setPassword(findUser.getPassword())
                    .setAge(findUser.getAge())
                    .setRole(findUser.getRole().toString())
                    .build();
            UserService.FindUserResponse response =
                    UserService.FindUserResponse.newBuilder().setUser(userGrpc).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

        @Override
        public void createUser (UserService.CreateUserRequest
        request, StreamObserver < UserService.CreateUserResponse > responseObserver){
            UserService.User user = request.getUser();
            User saveUser = new User(
                    user.getFirstName(),
                    user.getSecondName(),
                    user.getPassword(),
                    user.getAge(),
                    Role.valueOf(user.getRole())
            );
            repository.save(saveUser);
            user = UserService.User.newBuilder().setId(saveUser.getId()).build();
            UserService.CreateUserResponse response = UserService.CreateUserResponse.newBuilder()
                    .setUser(user)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

    @Override
    public void allUser(Empty request, StreamObserver<UserService.AllUserResponse> responseObserver) {
        log.info("Запрос пришел в метод allUser() с ответом со stream");

        repository.findAll().forEach(user -> {
            UserService.User userGrpc = UserService.User.newBuilder()
                    .setId(user.getId())
                    .setFirstName(user.getFirstName())
                    .setSecondName(user.getSecondName())
                    .setPassword(user.getPassword())
                    .setAge(user.getAge())
                    .setRole(user.getRole().toString())
                    .build();

            UserService.AllUserResponse response = UserService.AllUserResponse.newBuilder()
                    .setUser(userGrpc)
                    .build();
            responseObserver.onNext(response);
        });
        responseObserver.onCompleted();
    }
}
