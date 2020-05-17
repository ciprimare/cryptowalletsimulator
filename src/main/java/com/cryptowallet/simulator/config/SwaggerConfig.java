package com.cryptowallet.simulator.config;

import com.cryptowallet.simulator.config.swagger.DocumentationPluginsManagerBootAdapter;
import com.cryptowallet.simulator.config.swagger.TypeNameExtractorBootAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * https://github.com/springfox/springfox/issues/2932#issuecomment-473911363
     */
    @Autowired
    TypeNameExtractorBootAdapter typeNameExtractorBootAdapter;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));

    }

    @Bean
    @Primary
    public DocumentationPluginsManagerBootAdapter overrideDocumentationPluginsManagerBootAdapter() {
        return new DocumentationPluginsManagerBootAdapter();
    }

    @Bean
    @Primary
    public TypeNameExtractorBootAdapter overrideTypeNameExtractorBootAdapter() {
        return typeNameExtractorBootAdapter;
    }

}
