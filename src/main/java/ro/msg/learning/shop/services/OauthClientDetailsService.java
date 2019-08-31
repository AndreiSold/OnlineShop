package ro.msg.learning.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ro.msg.learning.shop.dtos.CustomerDto;
import ro.msg.learning.shop.entities.OauthClientDetails;
import ro.msg.learning.shop.entities.Role;
import ro.msg.learning.shop.repositories.OauthClientDetailsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OauthClientDetailsService {

    private final Environment env;
    private final OauthClientDetailsRepository oauthClientDetailsRepository;

    public void addNewOauthClient(CustomerDto customer) {

        StringBuilder authorities = new StringBuilder();

        List<Role> roles = customer.getRoles();
        if (!roles.isEmpty()) {
            for (Role role : roles) {
                if (!authorities.toString().equals("")) {
                    authorities.append(',');
                }
                authorities.append(role.getName());
            }
        }

        OauthClientDetails createdOauthClient = OauthClientDetails.builder()
            .client_id(customer.getUsername())
            .client_secret(customer.getUsername() + "-secret")
            .scope(env.getProperty("scope"))
            .authorized_grant_types(env.getProperty("authorized_grant_types"))
            .web_server_redirect_uri(null)
            .authorities(authorities.toString())
            .access_token_validity(Integer.parseInt(env.getProperty("access_token_validity_seconds")))
            .refresh_token_validity(Integer.parseInt(env.getProperty("refresh_token_validity_seconds")))
            .additional_information(null)
            .autoapprove("true").build();

        oauthClientDetailsRepository.save(createdOauthClient);
    }
}
