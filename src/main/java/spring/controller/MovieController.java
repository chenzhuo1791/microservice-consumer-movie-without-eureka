package spring.controller;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import spring.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MovieController {

	private Logger logger = LoggerFactory.getLogger(MovieController.class);

    private String instance_provider_user = "microservice-provider-user";

	@Autowired
	private LoadBalancerClient loadBalancerClient ;

	@Autowired
	private RestTemplate restTemplate ;

	@GetMapping("/findUser/{userId}")
	public User findUserById(@PathVariable Long userId){
        String url = "http://" + instance_provider_user + "/" + userId + "/showUser";
        User user = this.restTemplate.getForObject(url, User.class);
		logger.info("  result = {} ", user);
        this.loadUserInstance();
		return user ;
	}


    @GetMapping("/log-user-instance")
    public void loadUserInstance(){
        ServiceInstance serviceInstance = this.loadBalancerClient.choose(instance_provider_user);
        String host = serviceInstance.getHost();
        Integer port = serviceInstance.getPort();
        String serviceId = serviceInstance.getServiceId() ;
        logger.info(" {}, {} ,{}", serviceId, host, port);
    }

}
