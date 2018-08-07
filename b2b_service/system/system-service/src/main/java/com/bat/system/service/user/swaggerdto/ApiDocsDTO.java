package com.bat.system.service.user.swaggerdto;

import java.util.List;
import java.util.Map;

/**
 * @author: lim
 * @description: TODO
 * @date: 2018/4/23 15:02
 */
@lombok.NoArgsConstructor
@lombok.Data
public class ApiDocsDTO {

    @com.fasterxml.jackson.annotation.JsonProperty("openapi")
    private String openapi;// FIXME check this code
    @com.fasterxml.jackson.annotation.JsonProperty("info")
    private InfoDTO info;
    // @com.fasterxml.jackson.annotation.JsonProperty("servers")
    // private java.util.List<ServersDTO> servers;
    // @com.fasterxml.jackson.annotation.JsonProperty("tags")
    // private java.util.List<TagsDTO> tags;
    @com.fasterxml.jackson.annotation.JsonProperty("paths")
    private Map<String, Map<String, Content>> paths;
    // @com.fasterxml.jackson.annotation.JsonProperty("components")
    // private ComponentsDTO components;

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class InfoDTO {

        @com.fasterxml.jackson.annotation.JsonProperty("title")
        private String title;
        @com.fasterxml.jackson.annotation.JsonProperty("description")
        private String description;
        @com.fasterxml.jackson.annotation.JsonProperty("contact")
        private ContactDTO contact;
        @com.fasterxml.jackson.annotation.JsonProperty("version")
        private String version;

        @lombok.NoArgsConstructor
        @lombok.Data
        public static class ContactDTO {

            @com.fasterxml.jackson.annotation.JsonProperty("name")
            private String name;
            @com.fasterxml.jackson.annotation.JsonProperty("url")
            private String url;
            @com.fasterxml.jackson.annotation.JsonProperty("email")
            private String email;
        }
    }

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class Content {
        @com.fasterxml.jackson.annotation.JsonProperty("operationId")
        private String operationId;
        @com.fasterxml.jackson.annotation.JsonProperty("requestBody")
        private Object requestBody;
        @com.fasterxml.jackson.annotation.JsonProperty("responses")
        private Object responses;
        @com.fasterxml.jackson.annotation.JsonProperty("security")
        private Object security;
        @com.fasterxml.jackson.annotation.JsonProperty("summary")
        private String summary;
        @com.fasterxml.jackson.annotation.JsonProperty("tags")
        private List<String> tags;
    }
}
