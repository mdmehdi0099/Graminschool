package com.GraminSchool.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnquiryUpdateDTO {
    private Long id;
    private boolean resolve;
    private String resolvedBy;
    private String closingComment;
    private String updatedByRole;

}
