package com.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user_register")
public class User implements Serializable {

    @Id
    private String id;
    @Field("first_name")
    private String firstName;

    private String email;

    @CreatedDate
    @Field("created_on")
    private LocalDateTime createdOn;

    @LastModifiedDate
    @Field("updated_on")
    private LocalDateTime updatedOn;

    @Field("status")
    private Boolean active;

    @Field("last_login")
    private LocalDateTime lastLogin;

    @Field("is_deleted")
    private boolean deleted;

    @Field("mobile_number")
    private String mobileNumber;
}
