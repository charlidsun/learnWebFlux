package com.sun.flux.service;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.sun.flux.domain.User;
import com.sun.flux.repository.UserRepository;

/**
 * 功能：
 * 说明：
 * @author 孙荆阁:
 * @Date 2018年8月2日 下午1:36:11
 */
@Component
public class UserService {

	UserRepository userRepository;
	
	public UserService(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	public Mono<ServerResponse> getAllUser(ServerRequest request){
		Flux<User> userList = userRepository.getAllUser();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userList, User.class);
	}
	
	public Mono<ServerResponse> getUserById(ServerRequest request){
		//获取ID
		Long id = Long.valueOf(request.pathVariable("id"));
		//未找到
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        
        Mono<User> user = userRepository.getUser(id);
        return customerMono
                .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(user)))
                .switchIfEmpty(notFound);
	}
	
	
	public Mono<ServerResponse> postUser(ServerRequest request){
		Mono<User> user = request.bodyToMono(User.class);
		return ServerResponse.ok().build(userRepository.save);
	}
	
	public Mono<ServerResponse> putUser(ServerRequest request) {
	    long customerId = Long.valueOf(request.pathVariable("id"));

	    Mono<User> customer = request.bodyToMono(User.class);

	    Mono<User> responseMono = customerRepository.putUser(customerId, customer);

	    return responseMono
	            .flatMap(cust -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromObject(cust)));
	}

	public Mono<ServerResponse> deleteUser(ServerRequest request) {
	    long id = Long.valueOf(request.pathVariable("id"));
	    Mono<String> responseMono = customerRepository.deleteUser(id);
	    return responseMono
	            .flatMap(strMono -> ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(fromObject(strMono)));
    }
}
