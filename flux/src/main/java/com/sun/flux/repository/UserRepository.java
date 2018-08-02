package com.sun.flux.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.sun.flux.domain.User;

/**
 * 功能：
 * 说明：
 * @author 孙荆阁:
 * @Date 2018年8月2日 下午1:08:33
 */
@Repository
public class UserRepository {

	public static Map<Long,User> userMap = new HashMap<Long, User>();
	
	static {
		userMap.put(Long.valueOf(1), new User(1,"guo","jing",20));
		userMap.put(Long.valueOf(2), new User(2,"huang","rong",18));
	}
	
	//list
	public Flux<User> getAllUser(){
		return Flux.fromIterable(userMap.values());
	}
	//one
	public Mono<User> getUser(int id){
		return Mono.just(userMap.get(id));
	}
	//put
	public Mono<Void> saveUser(Mono<User> user){
		Mono<User> u = user.doOnNext(use->{
			userMap.put(use.getId(), use);
			System.out.println("post method save user");
		});
		return u.then();
	}
	//delete
	public Mono<String> deleUser(Long id){
		userMap.remove(id);
		return Mono.just("删除成功");
	}
	//update
	public Mono<User> updateUser(Long id,Mono<User> monoUser){
		Mono<User> u = monoUser.doOnNext(user -> {
			user.setId(id);
			userMap.put(id, user);
			System.out.println("update user");
		});
		return u;
	}
}
