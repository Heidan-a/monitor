package com.example.entity.vo.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RenameNodeVO {
    @Length(min = 1,max = 10)
    String node;
    int id;
    @Pattern(regexp = "(cn|hk|jp|kr|us|sg|de)")
    String location;
}
