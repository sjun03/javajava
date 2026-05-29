package com.koreait.spring_boot_study.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data // @Data -> @ToString, @Getter, @Setter, @EqualsAndHash
public class ModifyPostReqDto {
    @NotBlank(message = "제목은 비울 수 없습니다.")
    @Size(max = 100, message = "제못은 100자 이상 불가능합니다.")
    private String title;

    @NotBlank(message = "내용은 비울 수 없습니다.")
    @Size(max = 1000, message = "내용은 1000자 이상 불가능합니다.")
    private String Content;
}
