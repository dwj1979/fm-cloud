package com.fm.cloud.bamboo.ribbon.loadbalancer;

import com.fm.cloud.bamboo.ribbon.BambooRibbonLoadBalancerClient;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by saleson on 2017/11/9.
 */
public class BambooZoneAvoidanceRule extends ZoneAvoidanceRule {

    private CompositePredicate compositePredicate;
    private BambooRibbonLoadBalancerClient loadBalancerClient;

    public BambooZoneAvoidanceRule() {
        this(null);
    }

    public BambooZoneAvoidanceRule(BambooRibbonLoadBalancerClient loadBalancerClient) {
        super();
        this.loadBalancerClient = loadBalancerClient;
        BambooApiVersionPredicate apiVersionPredicate = new BambooApiVersionPredicate(this);
        compositePredicate = CompositePredicate.withPredicates(super.getPredicate(),
                apiVersionPredicate).build();
    }

    @Override
    public AbstractServerPredicate getPredicate() {
        return compositePredicate;
    }


    public Map<String, String> getServerMetadata(String serviceId, Server server) {
        return loadBalancerClient.serverIntrospector(serviceId).getMetadata(server);
    }

    @Autowired
    public void setLoadBalancerClient(BambooRibbonLoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }
}
