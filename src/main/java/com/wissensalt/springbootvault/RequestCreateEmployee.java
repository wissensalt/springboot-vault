package com.wissensalt.springbootvault;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCreateEmployee {
    private String firstName;
    private String lastName;
}
