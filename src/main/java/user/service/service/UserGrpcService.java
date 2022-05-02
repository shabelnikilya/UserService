package user.service.service;

import com.google.gson.Gson;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import user.service.grpc.UserCrudServiceGrpc;
import user.service.grpc.UserService;
import user.service.models.User;
import user.service.repository.UserRepository;

@Log4j
@Service
public class UserGrpcService extends UserCrudServiceGrpc.UserCrudServiceImplBase {
    private final UserRepository repository;
    private final Gson gson;

    @Autowired
    public UserGrpcService(UserRepository repository, Gson gson) {
        this.repository = repository;
        this.gson = gson;
    }

    /**
     * Не до конца дописан метод. Что возвращать когда пользователь не найден?
     * @param request
     * @param responseObserver
     */
    @Override
    public void findUserById(UserService.FindUserByIdRequest request,
                             StreamObserver<UserService.FindUserResponse> responseObserver) {
        log.info("Запрос дошел до метода findUserById()");
        User user = repository.findById(request.getId()).orElse(null);
        if (user != null) {
            log.info("Пользователь найден!");
            String userGson = gson.toJson(user);
            UserService.FindUserResponse response = UserService.FindUserResponse.newBuilder().setUser(userGson).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void findUserByName(UserService.FindUserByNameRequest request,
                               StreamObserver<UserService.FindUserResponse> responseObserver) {
        log.info("Запрос дошел до метода findUserByName()");
        User user = repository.findBySecondName(request.getName());
        if (user != null) {
            log.info("Пользователь найден!");
            String userGson = gson.toJson(user);
            UserService.FindUserResponse response = UserService.FindUserResponse.newBuilder().setUser(userGson).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void createUser(UserService.CreateUserRequest request,
                           StreamObserver<UserService.CreateUserResponse> responseObserver) {
        log.info("Запрос пришел в метод createUser()");
        User user = gson.fromJson(request.getUser(), User.class);
        User createUser = repository.save(user);
        UserService.CreateUserResponse response = UserService.CreateUserResponse.newBuilder()
                .setUser(gson.toJson(createUser))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void allUser(Empty request, StreamObserver<UserService.AllUserResponse> responseObserver) {
        log.info("Запрос пришел в метод allUser() с ответом без stream.");
        String users = gson.toJson(repository.findAll());
        UserService.AllUserResponse response = UserService.AllUserResponse.newBuilder()
                .setUsers(users).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void allUserStream(Empty request, StreamObserver<UserService.AllUserResponse> responseObserver) {
        log.info("Запрос пришел в метод allUser() с ответом со stream");
        repository.findAll().forEach(user -> {
            UserService.AllUserResponse response = UserService.AllUserResponse.newBuilder()
                    .setUsers(gson.toJson(user))
                    .build();
            responseObserver.onNext(response);
        });
        responseObserver.onCompleted();
    }
}
